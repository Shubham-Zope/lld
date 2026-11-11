package com.shubham.courseRegistrationSystem.services;

import com.shubham.courseRegistrationSystem.entities.Course;
import com.shubham.courseRegistrationSystem.entities.Enrollment;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.Student;
import com.shubham.courseRegistrationSystem.entities.Term;
import com.shubham.courseRegistrationSystem.entities.TimeTable;
import com.shubham.courseRegistrationSystem.entities.Transcript;
import com.shubham.courseRegistrationSystem.enums.EnrollmentStatus;
import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import com.shubham.courseRegistrationSystem.events.EnrollmentEvent;
import com.shubham.courseRegistrationSystem.events.EnrollmentEventPublisher;
import com.shubham.courseRegistrationSystem.policies.DropPolicy;
import com.shubham.courseRegistrationSystem.policies.PrerequisitePolicy;
import com.shubham.courseRegistrationSystem.repositories.CourseRepository;
import com.shubham.courseRegistrationSystem.repositories.EnrollmentRepository;
import com.shubham.courseRegistrationSystem.repositories.SectionRepository;
import com.shubham.courseRegistrationSystem.repositories.StudentRepository;
import com.shubham.courseRegistrationSystem.repositories.TermRepository;
import com.shubham.courseRegistrationSystem.results.DropResult;
import com.shubham.courseRegistrationSystem.results.RegistrationResult;
import com.shubham.courseRegistrationSystem.validators.RegistrationValidator;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Facade: register, drop, grade, and the timetable / transcript views.
 *
 * <p>Concurrency: every operation on a student runs under that student's lock, and every seat or
 * waitlist change runs under the section's lock, always in that order (student, then section).
 * Events are published only after all locks are released.
 */
public class RegistrationService {
  private final StudentRepository students;
  private final CourseRepository courses;
  private final SectionRepository sections;
  private final TermRepository terms;
  private final EnrollmentRepository enrollments;
  private final RegistrationContextLoader contextLoader;
  private final RegistrationValidator validatorChain;
  private final PrerequisitePolicy prerequisitePolicy;
  private final DropPolicy dropPolicy;
  private final StudentLocks studentLocks;
  private final EnrollmentEventPublisher publisher;
  private final Clock clock;

  public RegistrationService(
      StudentRepository students,
      CourseRepository courses,
      SectionRepository sections,
      TermRepository terms,
      EnrollmentRepository enrollments,
      RegistrationContextLoader contextLoader,
      RegistrationValidator validatorChain,
      PrerequisitePolicy prerequisitePolicy,
      DropPolicy dropPolicy,
      StudentLocks studentLocks,
      EnrollmentEventPublisher publisher,
      Clock clock) {
    this.students = Objects.requireNonNull(students, "students");
    this.courses = Objects.requireNonNull(courses, "courses");
    this.sections = Objects.requireNonNull(sections, "sections");
    this.terms = Objects.requireNonNull(terms, "terms");
    this.enrollments = Objects.requireNonNull(enrollments, "enrollments");
    this.contextLoader = Objects.requireNonNull(contextLoader, "contextLoader");
    this.validatorChain = Objects.requireNonNull(validatorChain, "validatorChain");
    this.prerequisitePolicy = Objects.requireNonNull(prerequisitePolicy, "prerequisitePolicy");
    this.dropPolicy = Objects.requireNonNull(dropPolicy, "dropPolicy");
    this.studentLocks = Objects.requireNonNull(studentLocks, "studentLocks");
    this.publisher = Objects.requireNonNull(publisher, "publisher");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  /**
   * Validates (collecting every failed rule), then takes a seat atomically or joins the waitlist.
   *
   * <p>A free seat is taken only if nobody is waiting: a seat freed by a drop belongs to the queue,
   * not to whoever registers first.
   */
  public RegistrationResult register(String studentId, String sectionId) {
    Optional<Student> student = students.findById(studentId);
    if (student.isEmpty()) {
      return RegistrationResult.rejected(List.of(RejectionReason.STUDENT_NOT_FOUND));
    }
    Optional<Section> found = sections.findById(sectionId);
    if (found.isEmpty()) {
      return RegistrationResult.rejected(List.of(RejectionReason.SECTION_NOT_FOUND));
    }
    Section section = found.get();

    List<EnrollmentEvent> events = new ArrayList<>();
    RegistrationResult result;
    ReentrantLock studentLock = studentLocks.lockFor(studentId);
    studentLock.lock();
    try {
      LocalDateTime now = now();
      List<RejectionReason> errors = new ArrayList<>();
      validatorChain.validate(contextLoader.forRegistration(student.get(), section, now), errors);
      if (!errors.isEmpty()) {
        return RegistrationResult.rejected(errors);
      }

      ReentrantLock sectionLock = section.getLock();
      sectionLock.lock();
      try {
        if (section.getWaitList().isEmpty() && section.tryReserveSeat()) {
          Enrollment enrollment = newEnrollment(studentId, section, EnrollmentStatus.ENROLLED, now);
          events.add(
              new EnrollmentEvent.StudentEnrolled(
                  studentId, sectionId, enrollment.getEnrollmentId()));
          result = RegistrationResult.enrolled(enrollment.getEnrollmentId());
        } else {
          Enrollment enrollment =
              newEnrollment(studentId, section, EnrollmentStatus.WAITLISTED, now);
          int position = section.getWaitList().add(student.get(), now);
          events.add(
              new EnrollmentEvent.StudentWaitlisted(
                  studentId, sectionId, enrollment.getEnrollmentId(), position));
          if (position < section.getWaitList().size()) {
            // ordering policy put them ahead of others (e.g. seniority): the others moved back
            events.add(
                new EnrollmentEvent.WaitlistUpdated(sectionId, section.getWaitList().snapshot()));
          }
          result = RegistrationResult.waitlisted(enrollment.getEnrollmentId(), position);
        }
      } finally {
        sectionLock.unlock();
      }
    } finally {
      studentLock.unlock();
    }
    publisher.publishAll(events);
    return result;
  }

  /**
   * Waitlisted: leave the queue (always allowed). Enrolled: drop or withdraw per the {@link
   * DropPolicy}; the freed seat goes to the waitlist via a {@code SeatFreed} event.
   */
  public DropResult drop(String studentId, String sectionId) {
    Optional<Section> found = sections.findById(sectionId);
    if (found.isEmpty()) {
      return new DropResult.Rejected(RejectionReason.SECTION_NOT_FOUND);
    }
    Section section = found.get();

    List<EnrollmentEvent> events = new ArrayList<>();
    DropResult result;
    ReentrantLock studentLock = studentLocks.lockFor(studentId);
    studentLock.lock();
    try {
      Optional<Enrollment> active = enrollments.findActive(studentId, sectionId);
      if (active.isEmpty()) {
        return new DropResult.Rejected(RejectionReason.NOT_ENROLLED);
      }
      Enrollment enrollment = active.get();

      if (enrollment.isWaitlisted()) {
        events.addAll(release(enrollment, section, false));
      } else {
        DropPolicy.Decision decision = dropPolicy.decide(termOf(section), now());
        if (decision == DropPolicy.Decision.REJECT) {
          return new DropResult.Rejected(RejectionReason.DROP_DEADLINE_PASSED);
        }
        events.addAll(release(enrollment, section, decision == DropPolicy.Decision.WITHDRAW));
      }
      result = new DropResult.Dropped(enrollment.getEnrollmentId(), enrollment.getStatus());
    } finally {
      studentLock.unlock();
    }
    publisher.publishAll(events);
    return result;
  }

  /**
   * Posts a final grade. If the course is failed and the prerequisite policy let students register
   * for later courses while this one was in progress, those conditional enrollments are dropped.
   */
  public void recordGrade(String enrollmentId, String grade, boolean passed) {
    Enrollment graded =
        enrollments
            .findById(enrollmentId)
            .orElseThrow(() -> new IllegalArgumentException("Enrollment " + enrollmentId));
    String studentId = graded.getStudentId();

    List<EnrollmentEvent> events = new ArrayList<>();
    ReentrantLock studentLock = studentLocks.lockFor(studentId);
    studentLock.lock();
    try {
      if (passed) {
        graded.complete(grade);
      } else {
        graded.fail(grade);
        if (prerequisitePolicy.allowsInProgress()) {
          events.addAll(dropConditionalDependents(graded));
        }
      }
    } finally {
      studentLock.unlock();
    }
    publisher.publishAll(events);
  }

  public TimeTable viewTimetable(String studentId, String termId) {
    return TimeTable.forStudent(
        studentId,
        termId,
        enrollments.findByStudent(studentId),
        id -> sections.findById(id).orElse(null));
  }

  public Transcript viewTranscript(String studentId) {
    return Transcript.of(
        studentId, enrollments.findByStudent(studentId), id -> courses.findById(id).orElse(null));
  }

  // ---- helpers (callers hold the student lock) ----

  private List<EnrollmentEvent> dropConditionalDependents(Enrollment failed) {
    String failedCourseId = failed.getCourseId();
    Transcript transcript = viewTranscript(failed.getStudentId());
    if (transcript.hasCompleted(failedCourseId)) {
      return List.of(); // passed it in another term, dependents are still fine
    }
    List<EnrollmentEvent> events = new ArrayList<>();
    for (Enrollment e : enrollments.findByStudent(failed.getStudentId())) {
      if (!e.isActive() || e.getTermId().equals(failed.getTermId())) {
        continue;
      }
      boolean dependsOnFailed =
          courses
              .findById(e.getCourseId())
              .map(Course::getPrerequisiteIds)
              .map(ids -> ids.contains(failedCourseId))
              .orElse(false);
      if (dependsOnFailed) {
        sections.findById(e.getSectionId()).ifPresent(s -> events.addAll(release(e, s, false)));
      }
    }
    return events;
  }

  /** Ends an active enrollment and gives back its seat or waitlist place. */
  private List<EnrollmentEvent> release(Enrollment enrollment, Section section, boolean withdraw) {
    List<EnrollmentEvent> events = new ArrayList<>();
    String sectionId = section.getSectionId();
    ReentrantLock sectionLock = section.getLock();
    sectionLock.lock();
    try {
      if (enrollment.isWaitlisted()) {
        section.getWaitList().remove(enrollment.getStudentId());
        enrollment.drop();
        events.add(dropped(enrollment, sectionId));
        events.add(
            new EnrollmentEvent.WaitlistUpdated(sectionId, section.getWaitList().snapshot()));
      } else {
        if (withdraw) {
          enrollment.withdraw();
        } else {
          enrollment.drop();
        }
        section.releaseSeat();
        events.add(dropped(enrollment, sectionId));
        events.add(new EnrollmentEvent.SeatFreed(sectionId));
      }
    } finally {
      sectionLock.unlock();
    }
    return events;
  }

  private static EnrollmentEvent dropped(Enrollment enrollment, String sectionId) {
    return new EnrollmentEvent.StudentDropped(
        enrollment.getStudentId(),
        sectionId,
        enrollment.getEnrollmentId(),
        enrollment.getStatus());
  }

  private Enrollment newEnrollment(
      String studentId, Section section, EnrollmentStatus status, LocalDateTime now) {
    Enrollment enrollment =
        new Enrollment(
            UUID.randomUUID().toString(),
            studentId,
            section.getSectionId(),
            section.getCourse().getCourseId(),
            section.getTermId(),
            status,
            now);
    enrollments.save(enrollment);
    return enrollment;
  }

  private Term termOf(Section section) {
    return terms
        .findById(section.getTermId())
        .orElseThrow(() -> new IllegalStateException("Term " + section.getTermId()));
  }

  private LocalDateTime now() {
    return LocalDateTime.now(clock);
  }
}

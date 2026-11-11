package com.shubham.courseRegistrationSystem.services;

import com.shubham.courseRegistrationSystem.entities.Enrollment;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.Student;
import com.shubham.courseRegistrationSystem.entities.WaitListEntry;
import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import com.shubham.courseRegistrationSystem.events.EnrollmentEvent;
import com.shubham.courseRegistrationSystem.events.EnrollmentEventListener;
import com.shubham.courseRegistrationSystem.events.EnrollmentEventPublisher;
import com.shubham.courseRegistrationSystem.repositories.EnrollmentRepository;
import com.shubham.courseRegistrationSystem.repositories.SectionRepository;
import com.shubham.courseRegistrationSystem.repositories.StudentRepository;
import com.shubham.courseRegistrationSystem.validators.RegistrationValidator;
import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Observer of {@code SeatFreed}: auto-enrols the next ELIGIBLE waitlisted student.
 *
 * <p>How a freed seat is kept for the queue (so a newcomer cannot jump it):
 *
 * <ol>
 *   <li>Under the section lock: if someone is waiting, reserve the free seat and take the head of
 *       the queue. The seat now counts as taken, so new registrants are waitlisted.
 *   <li>Release the section lock, take the candidate's student lock (student before section, the
 *       global lock order) and re-run the validation chain.
 *   <li>Valid: WAITLISTED to ENROLLED, the held seat becomes theirs. Invalid (e.g. now has a time
 *       clash): drop them from the waitlist, notify, and try the next student with the same seat.
 *       If the queue runs out, give the seat back.
 * </ol>
 */
public class WaitListPromoter implements EnrollmentEventListener {
  private final SectionRepository sections;
  private final StudentRepository students;
  private final EnrollmentRepository enrollments;
  private final RegistrationContextLoader contextLoader;
  private final RegistrationValidator validatorChain;
  private final StudentLocks studentLocks;
  private final EnrollmentEventPublisher publisher;
  private final Clock clock;

  public WaitListPromoter(
      SectionRepository sections,
      StudentRepository students,
      EnrollmentRepository enrollments,
      RegistrationContextLoader contextLoader,
      RegistrationValidator validatorChain,
      StudentLocks studentLocks,
      EnrollmentEventPublisher publisher,
      Clock clock) {
    this.sections = Objects.requireNonNull(sections, "sections");
    this.students = Objects.requireNonNull(students, "students");
    this.enrollments = Objects.requireNonNull(enrollments, "enrollments");
    this.contextLoader = Objects.requireNonNull(contextLoader, "contextLoader");
    this.validatorChain = Objects.requireNonNull(validatorChain, "validatorChain");
    this.studentLocks = Objects.requireNonNull(studentLocks, "studentLocks");
    this.publisher = Objects.requireNonNull(publisher, "publisher");
    this.clock = Objects.requireNonNull(clock, "clock");
  }

  @Override
  public void onEvent(EnrollmentEvent event) {
    if (event instanceof EnrollmentEvent.SeatFreed freed) {
      sections.findById(freed.sectionId()).ifPresent(this::promote);
    }
  }

  /** Fills as many free seats as possible from the waitlist. */
  public void promote(Section section) {
    List<EnrollmentEvent> events = new ArrayList<>();
    ReentrantLock sectionLock = section.getLock();
    try {
      while (true) {
        WaitListEntry candidate;
        sectionLock.lock();
        try {
          if (section.getWaitList().isEmpty() || !section.tryReserveSeat()) {
            return;
          }
          candidate = section.getWaitList().pollNext().orElseThrow();
        } finally {
          sectionLock.unlock();
        }

        // We hold one seat for the queue until someone takes it or the queue is empty.
        boolean seatTaken = false;
        try {
          while (!seatTaken && candidate != null) {
            seatTaken = tryPromote(candidate, section, events);
            if (!seatTaken) {
              sectionLock.lock();
              try {
                candidate = section.getWaitList().pollNext().orElse(null);
              } finally {
                sectionLock.unlock();
              }
            }
          }
        } finally {
          if (!seatTaken) {
            section.releaseSeat(); // queue ran out (or something failed): give the seat back
          }
        }
        if (!seatTaken) {
          return;
        }
      }
    } finally {
      if (!events.isEmpty()) {
        events.add(
            new EnrollmentEvent.WaitlistUpdated(
                section.getSectionId(), section.getWaitList().snapshot()));
        publisher.publishAll(events);
      }
    }
  }

  /** @return true if the candidate was enrolled into the held seat */
  private boolean tryPromote(WaitListEntry candidate, Section section, List<EnrollmentEvent> events) {
    ReentrantLock studentLock = studentLocks.lockFor(candidate.studentId());
    studentLock.lock();
    try {
      Optional<Enrollment> waiting =
          enrollments
              .findActive(candidate.studentId(), section.getSectionId())
              .filter(Enrollment::isWaitlisted);
      if (waiting.isEmpty()) {
        return false; // dropped from the waitlist meanwhile
      }
      Enrollment enrollment = waiting.get();
      Optional<Student> student = students.findById(candidate.studentId());
      if (student.isEmpty()) {
        enrollment.drop();
        return false;
      }

      LocalDateTime now = LocalDateTime.now(clock);
      List<RejectionReason> errors = new ArrayList<>();
      validatorChain.validate(
          contextLoader.forPromotion(student.get(), section, now, enrollment), errors);
      if (!errors.isEmpty()) {
        enrollment.drop();
        events.add(
            new EnrollmentEvent.WaitlistSkipped(
                candidate.studentId(), section.getSectionId(), errors));
        return false;
      }
      enrollment.promote();
      events.add(
          new EnrollmentEvent.StudentPromoted(
              candidate.studentId(), section.getSectionId(), enrollment.getEnrollmentId()));
      return true;
    } finally {
      studentLock.unlock();
    }
  }
}

package com.shubham.courseRegistrationSystem.services;

import com.shubham.courseRegistrationSystem.entities.Enrollment;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.Student;
import com.shubham.courseRegistrationSystem.entities.Term;
import com.shubham.courseRegistrationSystem.entities.TimeTable;
import com.shubham.courseRegistrationSystem.entities.Transcript;
import com.shubham.courseRegistrationSystem.repositories.CourseRepository;
import com.shubham.courseRegistrationSystem.repositories.EnrollmentRepository;
import com.shubham.courseRegistrationSystem.repositories.SectionRepository;
import com.shubham.courseRegistrationSystem.repositories.TermRepository;
import com.shubham.courseRegistrationSystem.validators.RegistrationContext;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/** Gathers what the validators need. Call it while holding the student's lock. */
public class RegistrationContextLoader {
  private final CourseRepository courses;
  private final SectionRepository sections;
  private final TermRepository terms;
  private final EnrollmentRepository enrollments;

  public RegistrationContextLoader(
      CourseRepository courses,
      SectionRepository sections,
      TermRepository terms,
      EnrollmentRepository enrollments) {
    this.courses = Objects.requireNonNull(courses, "courses");
    this.sections = Objects.requireNonNull(sections, "sections");
    this.terms = Objects.requireNonNull(terms, "terms");
    this.enrollments = Objects.requireNonNull(enrollments, "enrollments");
  }

  public RegistrationContext forRegistration(Student student, Section section, LocalDateTime now) {
    return build(student, section, now, false, null);
  }

  /**
   * For re-checking a waitlisted student. Their own waitlisted enrollment for this section is left
   * out, otherwise the duplicate check would reject them for waiting.
   */
  public RegistrationContext forPromotion(
      Student student, Section section, LocalDateTime now, Enrollment waitlistedEnrollment) {
    return build(student, section, now, true, waitlistedEnrollment);
  }

  private RegistrationContext build(
      Student student, Section section, LocalDateTime now, boolean promotion, Enrollment exclude) {
    Term term =
        terms
            .findById(section.getTermId())
            .orElseThrow(
                () ->
                    new IllegalStateException(
                        "Term " + section.getTermId() + " of section " + section.getSectionId()
                            + " not found"));
    String studentId = student.getStudentId();
    List<Enrollment> considered =
        enrollments.findByStudent(studentId).stream().filter(e -> e != exclude).toList();
    List<Enrollment> activeThisTerm =
        considered.stream()
            .filter(e -> e.getTermId().equals(term.getTermId()) && e.isActive())
            .toList();
    Transcript transcript =
        Transcript.of(studentId, considered, id -> courses.findById(id).orElse(null));
    TimeTable timetable =
        TimeTable.forStudent(
            studentId, term.getTermId(), considered, id -> sections.findById(id).orElse(null));
    return new RegistrationContext(
        student,
        section.getCourse(),
        section,
        term,
        now,
        activeThisTerm,
        transcript,
        timetable,
        promotion);
  }
}

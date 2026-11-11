package com.shubham.courseRegistrationSystem.validators;

import com.shubham.courseRegistrationSystem.entities.Course;
import com.shubham.courseRegistrationSystem.entities.Enrollment;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.Student;
import com.shubham.courseRegistrationSystem.entities.Term;
import com.shubham.courseRegistrationSystem.entities.TimeTable;
import com.shubham.courseRegistrationSystem.entities.Transcript;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

/**
 * Everything a validator may look at, gathered once per request so validators stay pure and cheap.
 *
 * @param activeEnrollmentsThisTerm the student's ENROLLED / WAITLISTED enrollments in the section's
 *     term (when promoting, the student's own waitlisted entry for this section is left out)
 * @param timetable the student's current timetable for the term (ENROLLED only)
 * @param promotion true when re-checking a waitlisted student for a freed seat; the window check
 *     is skipped because the student already queued inside a valid window
 */
public record RegistrationContext(
    Student student,
    Course course,
    Section section,
    Term term,
    LocalDateTime now,
    List<Enrollment> activeEnrollmentsThisTerm,
    Transcript transcript,
    TimeTable timetable,
    boolean promotion) {

  public RegistrationContext {
    Objects.requireNonNull(student, "student");
    Objects.requireNonNull(course, "course");
    Objects.requireNonNull(section, "section");
    Objects.requireNonNull(term, "term");
    Objects.requireNonNull(now, "now");
    activeEnrollmentsThisTerm = List.copyOf(activeEnrollmentsThisTerm);
    Objects.requireNonNull(transcript, "transcript");
    Objects.requireNonNull(timetable, "timetable");
  }

  /** Credits of the student's ENROLLED sections this term (waitlisted ones do not count). */
  public int creditsEnrolledThisTerm() {
    return transcript.creditsInProgress(term.getTermId());
  }
}

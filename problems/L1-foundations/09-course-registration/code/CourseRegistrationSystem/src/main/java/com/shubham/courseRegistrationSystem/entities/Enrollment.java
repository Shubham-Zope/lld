package com.shubham.courseRegistrationSystem.entities;

import com.shubham.courseRegistrationSystem.enums.EnrollmentStatus;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.Optional;

/**
 * Link between a student and a section in one term. This is the only record that changes on
 * register / drop / promote / complete; timetable, transcript and credits used are derived from it.
 *
 * <p>Status changes are synchronized and checked against {@link EnrollmentStatus#canTransitionTo},
 * so two threads cannot, for example, promote and drop the same enrollment at once.
 */
public class Enrollment {
  private final String enrollmentId;
  private final String studentId;
  private final String sectionId;
  private final String courseId;
  private final String termId;
  private final LocalDateTime createdAt;
  private volatile EnrollmentStatus status;
  private volatile String grade;

  public Enrollment(
      String enrollmentId,
      String studentId,
      String sectionId,
      String courseId,
      String termId,
      EnrollmentStatus initialStatus,
      LocalDateTime createdAt) {
    if (initialStatus != EnrollmentStatus.ENROLLED
        && initialStatus != EnrollmentStatus.WAITLISTED) {
      throw new IllegalArgumentException(
          "A new enrollment must start as ENROLLED or WAITLISTED, not " + initialStatus);
    }
    this.enrollmentId = Objects.requireNonNull(enrollmentId, "enrollmentId");
    this.studentId = Objects.requireNonNull(studentId, "studentId");
    this.sectionId = Objects.requireNonNull(sectionId, "sectionId");
    this.courseId = Objects.requireNonNull(courseId, "courseId");
    this.termId = Objects.requireNonNull(termId, "termId");
    this.createdAt = Objects.requireNonNull(createdAt, "createdAt");
    this.status = initialStatus;
    this.grade = null;
  }

  /** WAITLISTED -> ENROLLED, when a seat frees and the student passes validation again. */
  public synchronized void promote() {
    moveTo(EnrollmentStatus.ENROLLED);
  }

  /** ENROLLED or WAITLISTED -> DROPPED (before the drop deadline, or skipped on the waitlist). */
  public synchronized void drop() {
    moveTo(EnrollmentStatus.DROPPED);
  }

  /** ENROLLED -> WITHDRAWN, a drop after the deadline that shows as "W" on the transcript. */
  public synchronized void withdraw() {
    moveTo(EnrollmentStatus.WITHDRAWN);
  }

  /** ENROLLED -> COMPLETED with a passing grade. Counts as a met prerequisite. */
  public synchronized void complete(String grade) {
    moveTo(EnrollmentStatus.COMPLETED);
    this.grade = requireGrade(grade);
  }

  /** ENROLLED -> FAILED with a failing grade. Does not count as a met prerequisite. */
  public synchronized void fail(String grade) {
    moveTo(EnrollmentStatus.FAILED);
    this.grade = requireGrade(grade);
  }

  private void moveTo(EnrollmentStatus next) {
    if (!status.canTransitionTo(next)) {
      throw new IllegalStateException(
          "Enrollment " + enrollmentId + " cannot move from " + status + " to " + next);
    }
    this.status = next;
  }

  private static String requireGrade(String grade) {
    if (grade == null || grade.isBlank()) {
      throw new IllegalArgumentException("Grade is required");
    }
    return grade;
  }

  /** Holds a seat: counts toward credit cap and time clash. */
  public boolean isEnrolled() {
    return status == EnrollmentStatus.ENROLLED;
  }

  public boolean isWaitlisted() {
    return status == EnrollmentStatus.WAITLISTED;
  }

  /** Still in play this term (holding a seat or waiting for one). */
  public boolean isActive() {
    return status == EnrollmentStatus.ENROLLED || status == EnrollmentStatus.WAITLISTED;
  }

  public String getEnrollmentId() {
    return enrollmentId;
  }

  public String getStudentId() {
    return studentId;
  }

  public String getSectionId() {
    return sectionId;
  }

  public String getCourseId() {
    return courseId;
  }

  public String getTermId() {
    return termId;
  }

  public EnrollmentStatus getStatus() {
    return status;
  }

  /** Empty until the enrollment is COMPLETED or FAILED. */
  public Optional<String> getGrade() {
    return Optional.ofNullable(grade);
  }

  public LocalDateTime getCreatedAt() {
    return createdAt;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Enrollment other)) return false;
    return enrollmentId.equals(other.enrollmentId);
  }

  @Override
  public int hashCode() {
    return enrollmentId.hashCode();
  }
}

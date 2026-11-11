package com.shubham.courseRegistrationSystem.entities;

import com.shubham.courseRegistrationSystem.enums.EnrollmentStatus;
import java.util.Objects;
import java.util.Optional;

/**
 * One line of a transcript.
 *
 * @param grade null while the course is in progress or after a withdrawal
 */
public record TranscriptEntry(
    String courseId,
    String courseName,
    String sectionId,
    String termId,
    EnrollmentStatus status,
    String grade,
    int credits) {

  public TranscriptEntry {
    Objects.requireNonNull(courseId, "courseId");
    Objects.requireNonNull(courseName, "courseName");
    Objects.requireNonNull(sectionId, "sectionId");
    Objects.requireNonNull(termId, "termId");
    Objects.requireNonNull(status, "status");
  }

  public Optional<String> gradeIfAny() {
    return Optional.ofNullable(grade);
  }

  public boolean isInProgress() {
    return status == EnrollmentStatus.ENROLLED;
  }

  /** Credits counted as earned: only a COMPLETED course earns its credits. */
  public int creditsEarned() {
    return status == EnrollmentStatus.COMPLETED ? credits : 0;
  }
}

package com.shubham.courseRegistrationSystem.entities;

import com.shubham.courseRegistrationSystem.enums.EnrollmentStatus;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;

/**
 * Read model: a student's record across all terms, built on demand from their enrollments. Never
 * stored, so it cannot go out of sync.
 *
 * <p>Shows COMPLETED, FAILED, WITHDRAWN and ENROLLED (in progress). DROPPED and WAITLISTED
 * enrollments never held a seat to the end, so they are left out.
 */
public class Transcript {
  private static final Set<EnrollmentStatus> SHOWN =
      Set.of(
          EnrollmentStatus.COMPLETED,
          EnrollmentStatus.FAILED,
          EnrollmentStatus.WITHDRAWN,
          EnrollmentStatus.ENROLLED);

  private final String studentId;
  private final List<TranscriptEntry> entries;

  private Transcript(String studentId, List<TranscriptEntry> entries) {
    this.studentId = studentId;
    this.entries = List.copyOf(entries);
  }

  /**
   * @param enrollments the student's enrollments (any term, any status; filtered here)
   * @param courseLookup finds a course by ID, e.g. {@code courseRepository::getById}
   */
  public static Transcript of(
      String studentId,
      Collection<Enrollment> enrollments,
      Function<String, Course> courseLookup) {
    Objects.requireNonNull(studentId, "studentId");
    Objects.requireNonNull(courseLookup, "courseLookup");
    List<Enrollment> mine = new ArrayList<>();
    for (Enrollment enrollment : enrollments) {
      if (enrollment.getStudentId().equals(studentId) && SHOWN.contains(enrollment.getStatus())) {
        mine.add(enrollment);
      }
    }
    mine.sort(Comparator.comparing(Enrollment::getCreatedAt));

    List<TranscriptEntry> entries = new ArrayList<>(mine.size());
    for (Enrollment enrollment : mine) {
      Course course = courseLookup.apply(enrollment.getCourseId());
      if (course == null) {
        throw new IllegalStateException(
            "Course " + enrollment.getCourseId() + " of enrollment "
                + enrollment.getEnrollmentId() + " not found");
      }
      entries.add(
          new TranscriptEntry(
              course.getCourseId(),
              course.getName(),
              enrollment.getSectionId(),
              enrollment.getTermId(),
              enrollment.getStatus(),
              enrollment.getGrade().orElse(null),
              course.getCredits()));
    }
    return new Transcript(studentId, entries);
  }

  /** Passed the course in any term. This is what a prerequisite needs (strict policy). */
  public boolean hasCompleted(String courseId) {
    return entries.stream()
        .anyMatch(e -> e.courseId().equals(courseId) && e.status() == EnrollmentStatus.COMPLETED);
  }

  /** Currently taking the course (ENROLLED, no grade yet). Used by an in-progress policy. */
  public boolean isInProgress(String courseId) {
    return entries.stream().anyMatch(e -> e.courseId().equals(courseId) && e.isInProgress());
  }

  /** The prerequisite IDs from the list that this student has not completed. */
  public List<String> missingPrerequisites(Collection<String> prerequisiteIds) {
    List<String> missing = new ArrayList<>();
    for (String id : prerequisiteIds) {
      if (!hasCompleted(id)) {
        missing.add(id);
      }
    }
    return missing;
  }

  /** Credits from COMPLETED courses across all terms. */
  public int totalCreditsEarned() {
    return entries.stream().mapToInt(TranscriptEntry::creditsEarned).sum();
  }

  /** Credits of courses currently being taken in the term (what counts toward the 24 cap). */
  public int creditsInProgress(String termId) {
    return entries.stream()
        .filter(e -> e.termId().equals(termId) && e.isInProgress())
        .mapToInt(TranscriptEntry::credits)
        .sum();
  }

  public List<TranscriptEntry> entriesForTerm(String termId) {
    return entries.stream().filter(e -> e.termId().equals(termId)).toList();
  }

  public String getStudentId() {
    return studentId;
  }

  /** All lines, oldest first. */
  public List<TranscriptEntry> getEntries() {
    return entries;
  }
}

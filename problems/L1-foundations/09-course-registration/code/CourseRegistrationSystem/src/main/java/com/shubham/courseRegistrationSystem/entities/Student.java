package com.shubham.courseRegistrationSystem.entities;

import java.util.Objects;

/**
 * A student. Holds no enrolled courses or used-credit counter: those are computed from the
 * student's ENROLLED enrollments, which are the single source of truth.
 */
public class Student {
  public static final int DEFAULT_MAX_CREDITS_PER_TERM = 24;
  private static final int MIN_YEAR = 1;
  private static final int MAX_YEAR = 4;

  private final String studentId;
  private final String studentName;
  private final int year;
  private final int maxCreditsPerTerm;

  public Student(String studentId, String studentName, int year) {
    this(studentId, studentName, year, DEFAULT_MAX_CREDITS_PER_TERM);
  }

  public Student(String studentId, String studentName, int year, int maxCreditsPerTerm) {
    if (year < MIN_YEAR || year > MAX_YEAR) {
      throw new IllegalArgumentException(
          "Year can be only between " + MIN_YEAR + " and " + MAX_YEAR);
    }
    if (maxCreditsPerTerm <= 0 || maxCreditsPerTerm > DEFAULT_MAX_CREDITS_PER_TERM) {
      throw new IllegalArgumentException(
          "Max credits per term must be between 1 and " + DEFAULT_MAX_CREDITS_PER_TERM);
    }
    this.studentId = Objects.requireNonNull(studentId, "studentId");
    this.studentName = Objects.requireNonNull(studentName, "studentName");
    this.year = year;
    this.maxCreditsPerTerm = maxCreditsPerTerm;
  }

  /**
   * True if adding a course of {@code courseCredits} keeps the student within their cap.
   *
   * @param creditsUsedThisTerm credits of the student's ENROLLED sections this term
   */
  public boolean canTakeCredits(int creditsUsedThisTerm, int courseCredits) {
    return creditsUsedThisTerm + courseCredits <= maxCreditsPerTerm;
  }

  public String getStudentId() {
    return studentId;
  }

  public String getStudentName() {
    return studentName;
  }

  public int getYear() {
    return year;
  }

  public int getMaxCreditsPerTerm() {
    return maxCreditsPerTerm;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Student)) return false;
    return studentId.equals(((Student) o).studentId);
  }

  @Override
  public int hashCode() {
    return studentId.hashCode();
  }
}

package com.shubham.courseRegistrationSystem.entities;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * When students of one year may register for one term. Seniors get earlier windows, which is how
 * priority registration for seniors works without changing the registration logic.
 */
public class RegistrationWindow {
  private static final int MIN_YEAR = 1;
  private static final int MAX_YEAR = 4;

  private final String termId;
  private final int studentYear;
  private final LocalDateTime opensAt;
  private final LocalDateTime closesAt;

  public RegistrationWindow(
      String termId, int studentYear, LocalDateTime opensAt, LocalDateTime closesAt) {
    if (studentYear < MIN_YEAR || studentYear > MAX_YEAR) {
      throw new IllegalArgumentException(
          "Student year can be only between " + MIN_YEAR + " and " + MAX_YEAR);
    }
    this.termId = Objects.requireNonNull(termId, "termId");
    this.opensAt = Objects.requireNonNull(opensAt, "opensAt");
    this.closesAt = Objects.requireNonNull(closesAt, "closesAt");
    if (!opensAt.isBefore(closesAt)) {
      throw new IllegalArgumentException("Window must open before it closes");
    }
    this.studentYear = studentYear;
  }

  /**
   * True if this window applies to the given year and {@code now} falls inside it. The window is
   * open from {@code opensAt} (inclusive) until {@code closesAt} (exclusive).
   */
  public boolean isOpen(int year, LocalDateTime now) {
    Objects.requireNonNull(now, "now");
    return year == studentYear && !now.isBefore(opensAt) && now.isBefore(closesAt);
  }

  public boolean isOpenFor(Student student, LocalDateTime now) {
    return isOpen(Objects.requireNonNull(student, "student").getYear(), now);
  }

  public String getTermId() {
    return termId;
  }

  public int getStudentYear() {
    return studentYear;
  }

  public LocalDateTime getOpensAt() {
    return opensAt;
  }

  public LocalDateTime getClosesAt() {
    return closesAt;
  }

  /** One window per (term, year): two windows with the same pair are the same window. */
  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof RegistrationWindow other)) return false;
    return studentYear == other.studentYear && termId.equals(other.termId);
  }

  @Override
  public int hashCode() {
    return Objects.hash(termId, studentYear);
  }
}

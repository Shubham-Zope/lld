package com.shubham.courseRegistrationSystem.entities;

import java.time.LocalDateTime;
import java.util.Objects;

public class Term {
  private final String termId;
  private final LocalDateTime startDate;
  private final LocalDateTime endDate;
  private final LocalDateTime dropDeadLine;

  public Term(
      String termId, LocalDateTime startDate, LocalDateTime endDate, LocalDateTime dropDeadLine) {
    this.termId = Objects.requireNonNull(termId, "termId");
    this.startDate = Objects.requireNonNull(startDate, "startDate");
    this.endDate = Objects.requireNonNull(endDate, "endDate");
    this.dropDeadLine = Objects.requireNonNull(dropDeadLine, "dropDeadLine");
    if (!startDate.isBefore(endDate)) {
      throw new IllegalArgumentException("Term must start before it ends");
    }
    if (dropDeadLine.isAfter(endDate)) {
      throw new IllegalArgumentException("Drop deadline cannot be after the term ends");
    }
  }

  /** True once the drop deadline has passed (a drop at the exact deadline is still allowed). */
  public boolean isPastDropDeadline(LocalDateTime now) {
    return now.isAfter(dropDeadLine);
  }

  public String getTermId() {
    return this.termId;
  }

  public LocalDateTime getStartDate() {
    return this.startDate;
  }

  public LocalDateTime getEndDate() {
    return this.endDate;
  }

  public LocalDateTime getDropDeadLine() {
    return this.dropDeadLine;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Term other)) return false;
    return termId.equals(other.termId);
  }

  @Override
  public int hashCode() {
    return termId.hashCode();
  }
}

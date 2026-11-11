package com.shubham.courseRegistrationSystem.results;

import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import java.util.List;
import java.util.Objects;

/**
 * What {@code register()} returns: exactly one of Enrolled, Waitlisted or Rejected. Sealed, so a
 * {@code switch} over it must handle all three.
 */
public sealed interface RegistrationResult {

  /** Got a seat. */
  record Enrolled(String enrollmentId) implements RegistrationResult {
    public Enrolled {
      Objects.requireNonNull(enrollmentId, "enrollmentId");
    }
  }

  /** Section full; waiting at this 1-based position. */
  record Waitlisted(String enrollmentId, int position) implements RegistrationResult {
    public Waitlisted {
      Objects.requireNonNull(enrollmentId, "enrollmentId");
      if (position < 1) {
        throw new IllegalArgumentException("Waitlist position starts at 1");
      }
    }
  }

  /** Failed one or more rules; every failed rule is listed. */
  record Rejected(List<RejectionReason> reasons) implements RegistrationResult {
    public Rejected {
      reasons = List.copyOf(Objects.requireNonNull(reasons, "reasons"));
      if (reasons.isEmpty()) {
        throw new IllegalArgumentException("A rejection needs at least one reason");
      }
    }

    public Rejected(RejectionReason reason) {
      this(List.of(reason));
    }
  }

  static RegistrationResult enrolled(String enrollmentId) {
    return new Enrolled(enrollmentId);
  }

  static RegistrationResult waitlisted(String enrollmentId, int position) {
    return new Waitlisted(enrollmentId, position);
  }

  static RegistrationResult rejected(List<RejectionReason> reasons) {
    return new Rejected(reasons);
  }
}

package com.shubham.courseRegistrationSystem.enums;

import java.util.Set;

public enum EnrollmentStatus {
  ENROLLED,
  WAITLISTED,
  DROPPED,
  COMPLETED,
  FAILED,
  WITHDRAWN;

  /**
   * Allowed moves:
   *
   * <pre>
   * WAITLISTED -> ENROLLED (promoted), DROPPED (left queue / skipped)
   * ENROLLED   -> DROPPED (before deadline), WITHDRAWN (after deadline),
   *               COMPLETED, FAILED (grades posted)
   * DROPPED, COMPLETED, FAILED, WITHDRAWN -> nothing (final)
   * </pre>
   */
  public boolean canTransitionTo(EnrollmentStatus next) {
    return switch (this) {
      case WAITLISTED -> Set.of(ENROLLED, DROPPED).contains(next);
      case ENROLLED -> Set.of(DROPPED, WITHDRAWN, COMPLETED, FAILED).contains(next);
      case DROPPED, COMPLETED, FAILED, WITHDRAWN -> false;
    };
  }

  public boolean isFinal() {
    return this == DROPPED || this == COMPLETED || this == FAILED || this == WITHDRAWN;
  }
}

package com.shubham.courseRegistrationSystem.results;

import com.shubham.courseRegistrationSystem.enums.EnrollmentStatus;
import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import java.util.Objects;

/** What {@code drop()} returns. */
public sealed interface DropResult {

  /** status is DROPPED, or WITHDRAWN for a late drop under the withdraw policy. */
  record Dropped(String enrollmentId, EnrollmentStatus status) implements DropResult {
    public Dropped {
      Objects.requireNonNull(enrollmentId, "enrollmentId");
      if (status != EnrollmentStatus.DROPPED && status != EnrollmentStatus.WITHDRAWN) {
        throw new IllegalArgumentException("Dropped status must be DROPPED or WITHDRAWN");
      }
    }
  }

  record Rejected(RejectionReason reason) implements DropResult {
    public Rejected {
      Objects.requireNonNull(reason, "reason");
    }
  }
}

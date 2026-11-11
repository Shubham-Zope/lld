package com.shubham.courseRegistrationSystem.events;

import com.shubham.courseRegistrationSystem.enums.EnrollmentStatus;
import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import java.util.List;

/** Something that happened to an enrollment or a section. Published after locks are released. */
public sealed interface EnrollmentEvent {

  String sectionId();

  record StudentEnrolled(String studentId, String sectionId, String enrollmentId)
      implements EnrollmentEvent {}

  record StudentWaitlisted(String studentId, String sectionId, String enrollmentId, int position)
      implements EnrollmentEvent {}

  /** A waitlisted student got a freed seat. */
  record StudentPromoted(String studentId, String sectionId, String enrollmentId)
      implements EnrollmentEvent {}

  /** A waitlisted student no longer passes validation (e.g. now has a clash) and was removed. */
  record WaitlistSkipped(String studentId, String sectionId, List<RejectionReason> reasons)
      implements EnrollmentEvent {
    public WaitlistSkipped {
      reasons = List.copyOf(reasons);
    }
  }

  /** status is DROPPED or WITHDRAWN. */
  record StudentDropped(
      String studentId, String sectionId, String enrollmentId, EnrollmentStatus status)
      implements EnrollmentEvent {}

  /** A seat came back to the section; the waitlist promoter reacts to this. */
  record SeatFreed(String sectionId) implements EnrollmentEvent {}

  /** The waitlist order changed; {@code order} is student IDs, first in line first. */
  record WaitlistUpdated(String sectionId, List<String> order) implements EnrollmentEvent {
    public WaitlistUpdated {
      order = List.copyOf(order);
    }
  }
}

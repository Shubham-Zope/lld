package com.shubham.courseRegistrationSystem.validators;

import com.shubham.courseRegistrationSystem.entities.Enrollment;
import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import java.util.Optional;

/**
 * Not already holding or waiting for ANY section of this course this term, and not already passed
 * the course.
 */
public class DuplicateRegistrationValidator extends RegistrationValidator {

  @Override
  protected Optional<RejectionReason> check(RegistrationContext ctx) {
    String courseId = ctx.course().getCourseId();
    for (Enrollment e : ctx.activeEnrollmentsThisTerm()) {
      if (e.getCourseId().equals(courseId)) {
        return Optional.of(
            e.isEnrolled() ? RejectionReason.ALREADY_ENROLLED : RejectionReason.ALREADY_WAITLISTED);
      }
    }
    if (ctx.transcript().hasCompleted(courseId)) {
      return Optional.of(RejectionReason.ALREADY_COMPLETED);
    }
    return Optional.empty();
  }
}

package com.shubham.courseRegistrationSystem.validators;

import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import java.util.Optional;

/** Enrolled credits this term + this course's credits stay within the student's cap (24). */
public class CreditCapValidator extends RegistrationValidator {

  @Override
  protected Optional<RejectionReason> check(RegistrationContext ctx) {
    boolean fits =
        ctx.student().canTakeCredits(ctx.creditsEnrolledThisTerm(), ctx.course().getCredits());
    return fits ? Optional.empty() : Optional.of(RejectionReason.CREDIT_CAP_EXCEEDED);
  }
}

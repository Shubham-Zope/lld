package com.shubham.courseRegistrationSystem.validators;

import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import java.util.Optional;

/** No slot of the section overlaps a slot of a section the student is ENROLLED in this term. */
public class TimeClashValidator extends RegistrationValidator {

  @Override
  protected Optional<RejectionReason> check(RegistrationContext ctx) {
    return ctx.timetable().clashesWith(ctx.section())
        ? Optional.of(RejectionReason.TIME_CLASH)
        : Optional.empty();
  }
}

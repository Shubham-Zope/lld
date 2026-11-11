package com.shubham.courseRegistrationSystem.validators;

import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import com.shubham.courseRegistrationSystem.repositories.RegistrationWindowRepository;
import java.util.Objects;
import java.util.Optional;

/** Registration is open for the student's year in this term. No window saved = closed. */
public class RegistrationWindowValidator extends RegistrationValidator {
  private final RegistrationWindowRepository windows;

  public RegistrationWindowValidator(RegistrationWindowRepository windows) {
    this.windows = Objects.requireNonNull(windows, "windows");
  }

  @Override
  protected Optional<RejectionReason> check(RegistrationContext ctx) {
    if (ctx.promotion()) {
      return Optional.empty();
    }
    boolean open =
        windows
            .find(ctx.term().getTermId(), ctx.student().getYear())
            .map(w -> w.isOpenFor(ctx.student(), ctx.now()))
            .orElse(false);
    return open ? Optional.empty() : Optional.of(RejectionReason.WINDOW_CLOSED);
  }
}

package com.shubham.courseRegistrationSystem.validators;

import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * One link of the validation chain (Chain of Responsibility).
 *
 * <p>Each link runs its own check, records a reason if it fails, and always passes the request on,
 * so the caller gets every failed rule at once ({@code Rejected(reasons)}).
 *
 * <p>Wiring: {@code head.setNext(b).setNext(c)} — setNext returns the link it was given so the
 * chain reads left to right. Callers only ever hold the head.
 */
public abstract class RegistrationValidator {
  private RegistrationValidator next;

  public RegistrationValidator setNext(RegistrationValidator next) {
    this.next = Objects.requireNonNull(next, "next");
    return next;
  }

  /** Runs this link and every link after it, adding each failure to {@code errors}. */
  public final void validate(RegistrationContext ctx, List<RejectionReason> errors) {
    check(ctx).ifPresent(errors::add);
    if (next != null) {
      next.validate(ctx, errors);
    }
  }

  /** This link's rule: empty if it passes, otherwise the reason it fails. */
  protected abstract Optional<RejectionReason> check(RegistrationContext ctx);
}

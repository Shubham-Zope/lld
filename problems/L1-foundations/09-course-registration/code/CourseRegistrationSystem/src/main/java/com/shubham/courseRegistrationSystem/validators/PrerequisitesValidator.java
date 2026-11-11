package com.shubham.courseRegistrationSystem.validators;

import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import com.shubham.courseRegistrationSystem.policies.PrerequisitePolicy;
import java.util.Objects;
import java.util.Optional;

/** Every prerequisite of the course is met, as decided by the {@link PrerequisitePolicy}. */
public class PrerequisitesValidator extends RegistrationValidator {
  private final PrerequisitePolicy policy;

  public PrerequisitesValidator(PrerequisitePolicy policy) {
    this.policy = Objects.requireNonNull(policy, "policy");
  }

  @Override
  protected Optional<RejectionReason> check(RegistrationContext ctx) {
    for (String prerequisiteId : ctx.course().getPrerequisiteIds()) {
      if (!policy.isSatisfied(prerequisiteId, ctx)) {
        return Optional.of(RejectionReason.PREREQUISITE_MISSING);
      }
    }
    return Optional.empty();
  }
}

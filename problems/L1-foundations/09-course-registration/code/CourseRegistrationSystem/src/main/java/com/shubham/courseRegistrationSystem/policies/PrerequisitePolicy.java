package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.validators.RegistrationContext;

/** Decides whether one prerequisite counts as met (Strategy). */
public interface PrerequisitePolicy {
  boolean isSatisfied(String prerequisiteCourseId, RegistrationContext ctx);

  /**
   * True if an in-progress prerequisite may count. When it does, the dependent enrollment is
   * conditional and is dropped if the prerequisite is later failed.
   */
  boolean allowsInProgress();
}

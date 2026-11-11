package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.validators.RegistrationContext;

/** Only a COMPLETED (passed) prerequisite counts. The default. */
public class StrictPrerequisitePolicy implements PrerequisitePolicy {
  @Override
  public boolean isSatisfied(String prerequisiteCourseId, RegistrationContext ctx) {
    return ctx.transcript().hasCompleted(prerequisiteCourseId);
  }

  @Override
  public boolean allowsInProgress() {
    return false;
  }
}

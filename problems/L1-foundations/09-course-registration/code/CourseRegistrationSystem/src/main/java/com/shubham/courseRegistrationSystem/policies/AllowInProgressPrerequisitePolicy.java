package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.entities.TranscriptEntry;
import com.shubham.courseRegistrationSystem.validators.RegistrationContext;

/**
 * A prerequisite counts if it is COMPLETED, or being taken in a DIFFERENT term (registering for
 * next term while finishing the prerequisite this term). Taking the prerequisite and the course in
 * the same term is always rejected.
 */
public class AllowInProgressPrerequisitePolicy implements PrerequisitePolicy {
  @Override
  public boolean isSatisfied(String prerequisiteCourseId, RegistrationContext ctx) {
    if (ctx.transcript().hasCompleted(prerequisiteCourseId)) {
      return true;
    }
    String registeringTerm = ctx.term().getTermId();
    for (TranscriptEntry entry : ctx.transcript().getEntries()) {
      if (entry.courseId().equals(prerequisiteCourseId)
          && entry.isInProgress()
          && !entry.termId().equals(registeringTerm)) {
        return true;
      }
    }
    return false;
  }

  @Override
  public boolean allowsInProgress() {
    return true;
  }
}

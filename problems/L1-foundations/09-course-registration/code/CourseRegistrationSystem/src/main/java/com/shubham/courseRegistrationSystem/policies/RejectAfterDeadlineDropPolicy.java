package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.entities.Term;
import java.time.LocalDateTime;

/** Drops are refused after the deadline. The default. */
public class RejectAfterDeadlineDropPolicy implements DropPolicy {
  @Override
  public Decision decide(Term term, LocalDateTime now) {
    return term.isPastDropDeadline(now) ? Decision.REJECT : Decision.DROP;
  }
}

package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.entities.Term;
import java.time.LocalDateTime;

/** Drops after the deadline become a withdrawal ("W"); the seat is still freed. */
public class WithdrawAfterDeadlineDropPolicy implements DropPolicy {
  @Override
  public Decision decide(Term term, LocalDateTime now) {
    return term.isPastDropDeadline(now) ? Decision.WITHDRAW : Decision.DROP;
  }
}

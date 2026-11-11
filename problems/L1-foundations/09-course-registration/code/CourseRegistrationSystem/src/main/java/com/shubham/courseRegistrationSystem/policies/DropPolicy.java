package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.entities.Term;
import java.time.LocalDateTime;

/** What happens when an ENROLLED student drops (Strategy). Waitlisted drops are always allowed. */
public interface DropPolicy {
  enum Decision {
    /** Normal drop: status DROPPED, nothing on the transcript. */
    DROP,
    /** Late drop: status WITHDRAWN, shows as "W" on the transcript. */
    WITHDRAW,
    /** Refused: the student stays enrolled. */
    REJECT
  }

  Decision decide(Term term, LocalDateTime now);
}

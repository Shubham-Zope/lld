package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.entities.WaitListEntry;
import java.util.Comparator;

/**
 * Decides who is next on a waitlist (Strategy). The WaitList always adds arrival order as the final
 * tie-breaker, so a policy only has to say what matters most.
 */
public interface WaitListOrderingPolicy {
  Comparator<WaitListEntry> comparator();
}

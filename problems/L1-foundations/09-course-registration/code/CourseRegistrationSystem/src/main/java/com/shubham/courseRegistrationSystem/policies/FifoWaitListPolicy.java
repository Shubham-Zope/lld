package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.entities.WaitListEntry;
import java.util.Comparator;

/** First come, first served. The default. */
public class FifoWaitListPolicy implements WaitListOrderingPolicy {
  @Override
  public Comparator<WaitListEntry> comparator() {
    return Comparator.comparing(WaitListEntry::joinedAt);
  }
}

package com.shubham.courseRegistrationSystem.policies;

import com.shubham.courseRegistrationSystem.entities.WaitListEntry;
import java.util.Comparator;

/** Seniors first (higher year first), then first come, first served within a year. */
public class SeniorityWaitListPolicy implements WaitListOrderingPolicy {
  @Override
  public Comparator<WaitListEntry> comparator() {
    return Comparator.comparingInt(WaitListEntry::studentYear)
        .reversed()
        .thenComparing(WaitListEntry::joinedAt);
  }
}

package com.shubham.courseRegistrationSystem.entities;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * One student waiting for a seat in a section.
 *
 * @param studentYear kept so a seniority policy can order without looking the student up
 * @param sequence order of arrival in this waitlist; breaks ties when two students share a
 *     joinedAt, so the order is always total and stable
 */
public record WaitListEntry(
    String studentId, int studentYear, LocalDateTime joinedAt, long sequence) {

  public WaitListEntry {
    Objects.requireNonNull(studentId, "studentId");
    Objects.requireNonNull(joinedAt, "joinedAt");
  }
}

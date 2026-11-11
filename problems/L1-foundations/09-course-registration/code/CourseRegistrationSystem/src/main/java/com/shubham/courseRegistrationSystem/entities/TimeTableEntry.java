package com.shubham.courseRegistrationSystem.entities;

import java.util.Objects;

/** One class meeting: which course and section meets in which slot. */
public record TimeTableEntry(String courseId, String courseName, String sectionId, TimeSlot slot) {

  public TimeTableEntry {
    Objects.requireNonNull(courseId, "courseId");
    Objects.requireNonNull(courseName, "courseName");
    Objects.requireNonNull(sectionId, "sectionId");
    Objects.requireNonNull(slot, "slot");
  }
}

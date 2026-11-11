package com.shubham.courseRegistrationSystem.interview;

import java.time.DayOfWeek;
import java.time.LocalTime;

public record TimeSlot(DayOfWeek day, LocalTime start, LocalTime end) {
  public boolean overlaps(TimeSlot o) {
    return day == o.day && start.isBefore(o.end) && o.start.isBefore(end);
  }
}

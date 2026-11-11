package com.shubham.courseRegistrationSystem.entities;

import java.time.LocalTime;

public class TimeSlot {
  private final String day;
  private final LocalTime startTime;
  private final LocalTime endTime;

  public TimeSlot(String day, LocalTime startTime, LocalTime endTime) {
    this.day = day;
    this.startTime = startTime;
    this.endTime = endTime;
  }

  public boolean overlapSlots(TimeSlot other) {
    return sameDay(day, other.day)
        && startTime.isBefore(other.endTime)
        && other.startTime.isBefore(endTime);
  }

  /** "MON", "Mon" and "Monday" are the same day. */
  private static boolean sameDay(String a, String b) {
    if (a.equalsIgnoreCase(b)) {
      return true;
    }
    return a.length() >= 3
        && b.length() >= 3
        && isDayName(a)
        && isDayName(b)
        && a.substring(0, 3).equalsIgnoreCase(b.substring(0, 3));
  }

  private static boolean isDayName(String day) {
    return switch (day.substring(0, 3).toUpperCase()) {
      case "MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN" -> true;
      default -> false;
    };
  }

  public String getDay() {
    return this.day;
  }

  public LocalTime getStartTime() {
    return this.startTime;
  }

  public LocalTime getEndTime() {
    return this.endTime;
  }

  @Override
  public String toString() {
    return day + " " + startTime + "-" + endTime;
  }
}

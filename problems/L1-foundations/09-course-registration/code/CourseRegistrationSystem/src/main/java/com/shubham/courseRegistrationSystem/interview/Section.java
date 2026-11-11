package com.shubham.courseRegistrationSystem.interview;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/** Seats and waitlist are only touched while holding this section's monitor. */
public class Section {
  final String id;
  final Course course;
  final List<TimeSlot> slots;
  final int capacity;
  final Set<String> enrolled = new HashSet<>();
  final Deque<Student> waitlist = new ArrayDeque<>();

  public Section(String id, Course course, List<TimeSlot> slots, int capacity) {
    this.id = id;
    this.course = course;
    this.slots = slots;
    this.capacity = capacity;
  }

  boolean hasSeat() {
    return enrolled.size() < capacity;
  }

  boolean clashesWith(Section other) {
    return slots.stream().anyMatch(a -> other.slots.stream().anyMatch(a::overlaps));
  }
}

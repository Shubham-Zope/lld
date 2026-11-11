package com.shubham.courseRegistrationSystem.entities;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReentrantLock;

public class Section {
  private final String sectionId;
  private final Course course;
  private final String termId;
  private final int capacity;
  private final List<TimeSlot> timeSlots;
  private final Instructor instructor;
  private final WaitList waitList;
  private final AtomicInteger seatsTaken;
  private final ReentrantLock lock;

  public Section(
      String sectionId,
      Course course,
      String termId,
      int capacity,
      List<TimeSlot> timeSlots,
      Instructor instructor,
      WaitList waitList) {
    if (capacity <= 0) {
      throw new IllegalArgumentException("Section capacity must be positive");
    }

    this.sectionId = Objects.requireNonNull(sectionId, "sectionId");
    this.course = Objects.requireNonNull(course, "course");
    this.termId = Objects.requireNonNull(termId, "termId");
    this.timeSlots = List.copyOf(Objects.requireNonNull(timeSlots, "timeSlots"));
    this.instructor = Objects.requireNonNull(instructor, "instructor");
    this.waitList = Objects.requireNonNull(waitList, "waitList");
    this.capacity = capacity;
    this.seatsTaken = new AtomicInteger(0);
    this.lock = new ReentrantLock(true);
  }

  /**
   * Atomically takes one seat if any is free. Only one caller can win the last seat.
   *
   * @return true if a seat was reserved, false if the section is full
   */
  public boolean tryReserveSeat() {
    while (true) {
      int taken = seatsTaken.get();
      if (taken >= capacity) {
        return false;
      }
      if (seatsTaken.compareAndSet(taken, taken + 1)) {
        return true;
      }
    }
  }

  /** Atomically gives back one seat (on drop). Never lets the count go below zero. */
  public void releaseSeat() {
    while (true) {
      int taken = seatsTaken.get();
      if (taken == 0) {
        throw new IllegalStateException("No seat to release in section " + sectionId);
      }
      if (seatsTaken.compareAndSet(taken, taken - 1)) {
        return;
      }
    }
  }

  public boolean hasAvailableSeat() {
    return seatsTaken.get() < capacity;
  }

  public int getAvailableSeats() {
    return Math.max(0, capacity - seatsTaken.get());
  }

  /** True if any slot of this section overlaps any slot of the other section. */
  public boolean clashesWith(Section other) {
    for (TimeSlot mine : timeSlots) {
      for (TimeSlot theirs : other.timeSlots) {
        if (mine.overlapSlots(theirs)) {
          return true;
        }
      }
    }
    return false;
  }

  /**
   * Lock guarding seat changes together with waitlist changes, so a freed seat and a student
   * joining the waitlist cannot interleave. Callers take the student lock before this one.
   */
  public ReentrantLock getLock() {
    return this.lock;
  }

  public String getSectionId() {
    return this.sectionId;
  }

  public Course getCourse() {
    return this.course;
  }

  public String getTermId() {
    return this.termId;
  }

  public int getCapacity() {
    return this.capacity;
  }

  public int getSeatsTaken() {
    return this.seatsTaken.get();
  }

  public List<TimeSlot> getTimeSlots() {
    return this.timeSlots;
  }

  public Instructor getInstructor() {
    return this.instructor;
  }

  public WaitList getWaitList() {
    return this.waitList;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Section)) return false;
    return sectionId.equals(((Section) o).sectionId);
  }

  @Override
  public int hashCode() {
    return sectionId.hashCode();
  }
}

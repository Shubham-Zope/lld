package com.shubham.courseRegistrationSystem.entities;

import com.shubham.courseRegistrationSystem.policies.FifoWaitListPolicy;
import com.shubham.courseRegistrationSystem.policies.WaitListOrderingPolicy;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.TreeSet;

/**
 * Ordered queue of students waiting for a seat in one section. Who is next is decided by a
 * pluggable {@link WaitListOrderingPolicy} (FIFO by default).
 *
 * <p>Positions are 1-based. Methods are synchronized so the waitlist is safe on its own; in the
 * registration flow it is also used under the section lock, together with the seat counter.
 */
public class WaitList {
  private final TreeSet<WaitListEntry> queue;
  private final Map<String, WaitListEntry> byStudentId;
  private long nextSequence;

  public WaitList() {
    this(new FifoWaitListPolicy());
  }

  public WaitList(WaitListOrderingPolicy policy) {
    Objects.requireNonNull(policy, "policy");
    Comparator<WaitListEntry> order =
        policy.comparator().thenComparingLong(WaitListEntry::sequence);
    this.queue = new TreeSet<>(order);
    this.byStudentId = new HashMap<>();
    this.nextSequence = 0;
  }

  /**
   * Puts the student in the queue.
   *
   * @return the student's 1-based position right after joining
   * @throws IllegalStateException if the student is already waiting
   */
  public synchronized int add(Student student, LocalDateTime joinedAt) {
    Objects.requireNonNull(student, "student");
    if (byStudentId.containsKey(student.getStudentId())) {
      throw new IllegalStateException(
          "Student " + student.getStudentId() + " is already on this waitlist");
    }
    WaitListEntry entry =
        new WaitListEntry(student.getStudentId(), student.getYear(), joinedAt, nextSequence++);
    queue.add(entry);
    byStudentId.put(entry.studentId(), entry);
    return positionOf(entry);
  }

  /** Takes the student out of the queue (drop while waitlisted). */
  public synchronized boolean remove(String studentId) {
    WaitListEntry entry = byStudentId.remove(studentId);
    if (entry == null) {
      return false;
    }
    queue.remove(entry);
    return true;
  }

  /** Removes and returns whoever is next, or empty if no one is waiting. */
  public synchronized Optional<WaitListEntry> pollNext() {
    WaitListEntry entry = queue.pollFirst();
    if (entry != null) {
      byStudentId.remove(entry.studentId());
    }
    return Optional.ofNullable(entry);
  }

  public synchronized Optional<WaitListEntry> peekNext() {
    return queue.isEmpty() ? Optional.empty() : Optional.of(queue.first());
  }

  /**
   * Puts a polled entry back at the exact place it had (its joinedAt and sequence are unchanged).
   * Used when the promoter could not lock the student and must try again later.
   */
  public synchronized void requeue(WaitListEntry entry) {
    Objects.requireNonNull(entry, "entry");
    if (byStudentId.containsKey(entry.studentId())) {
      throw new IllegalStateException(
          "Student " + entry.studentId() + " is already on this waitlist");
    }
    queue.add(entry);
    byStudentId.put(entry.studentId(), entry);
  }

  /** The student's 1-based position, or empty if they are not waiting. */
  public synchronized OptionalInt positionOf(String studentId) {
    WaitListEntry entry = byStudentId.get(studentId);
    return entry == null ? OptionalInt.empty() : OptionalInt.of(positionOf(entry));
  }

  private int positionOf(WaitListEntry entry) {
    return queue.headSet(entry, false).size() + 1;
  }

  public synchronized boolean contains(String studentId) {
    return byStudentId.containsKey(studentId);
  }

  public synchronized int size() {
    return queue.size();
  }

  public synchronized boolean isEmpty() {
    return queue.isEmpty();
  }

  /** Student IDs in order, first in line first. A copy: changing it does not touch the queue. */
  public synchronized List<String> snapshot() {
    List<String> ids = new ArrayList<>(queue.size());
    for (WaitListEntry entry : queue) {
      ids.add(entry.studentId());
    }
    return ids;
  }
}

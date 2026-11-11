package com.shubham.courseRegistrationSystem.services;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

/**
 * One lock per student, so two requests from the same student cannot both pass the credit cap or
 * clash check. Different students never block each other.
 *
 * <p>Lock order everywhere: student lock first, then section lock. Never the other way round.
 */
public class StudentLocks {
  private final Map<String, ReentrantLock> locks = new ConcurrentHashMap<>();

  public ReentrantLock lockFor(String studentId) {
    return locks.computeIfAbsent(studentId, id -> new ReentrantLock());
  }
}

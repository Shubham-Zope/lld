package com.shubham.employeedirectory.model;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** Breadth-first, non-recursive traversal of an employee and all of their reports. */
public final class SubtreeIterator implements Iterator<Employee> {

  private final Deque<Employee> queue = new ArrayDeque<>();

  public SubtreeIterator(Employee root) {
    queue.add(root);
  }

  @Override
  public boolean hasNext() {
    return !queue.isEmpty();
  }

  @Override
  public Employee next() {
    if (!hasNext()) {
      throw new NoSuchElementException();
    }
    Employee current = queue.poll();
    assert current != null;
    queue.addAll(current.getDirectReports());
    return current;
  }
}

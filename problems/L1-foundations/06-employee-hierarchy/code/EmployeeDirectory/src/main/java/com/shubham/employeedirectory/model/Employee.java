package com.shubham.employeedirectory.model;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

/**
 * A node in the org chart (Composite): every employee uniformly holds a manager reference and a set
 * of direct reports, so the whole hierarchy is a tree of the same type. Structural fields (manager,
 * direct reports) are mutated only by {@code EmployeeDirectoryService}, which owns the tree
 * invariants.
 */
public final class Employee {

  private final String id;
  private final String name;
  private final String department;
  private final String role;
  private final String location;

  private Employee manager;
  private final Set<Employee> directReports = new LinkedHashSet<>();

  public Employee(String id, String name, String department, String role, String location) {
    this.id = id;
    this.name = name;
    this.department = department;
    this.role = role;
    this.location = location;
  }

  public String getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public String getDepartment() {
    return department;
  }

  public String getRole() {
    return role;
  }

  public String getLocation() {
    return location;
  }

  public Employee getManager() {
    return manager;
  }

  public List<Employee> getDirectReports() {
    return List.copyOf(directReports);
  }

  public void setManager(Employee manager) {
    this.manager = manager;
  }

  public void addDirectReport(Employee report) {
    directReports.add(report);
  }

  public void removeDirectReport(Employee report) {
    directReports.remove(report);
  }

  public void clearDirectReports() {
    directReports.clear();
  }

  /** Iterator pattern: lazy, non-recursive traversal of this employee and all reports. */
  public Iterable<Employee> subtree() {
    return () -> new SubtreeIterator(this);
  }

  /**
   * Visitor pattern: push this employee and every report (pre-order, depth from this node) through
   * the visitor.
   */
  public void accept(EmployeeVisitor visitor) {
    acceptInternal(visitor, 0);
  }

  private void acceptInternal(EmployeeVisitor visitor, int depth) {
    visitor.visit(this, depth);
    for (Employee report : directReports) {
      report.acceptInternal(visitor, depth + 1);
    }
  }

  @Override
  public String toString() {
    return id + " (" + name + ", " + role + ")";
  }
}

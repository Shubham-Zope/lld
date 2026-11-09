package com.shubham.employeedirectory.service;

import com.shubham.employeedirectory.exception.CycleException;
import com.shubham.employeedirectory.exception.DuplicateEmployeeException;
import com.shubham.employeedirectory.exception.EmployeeNotFoundException;
import com.shubham.employeedirectory.exception.InvalidOperationException;
import com.shubham.employeedirectory.model.Employee;
import com.shubham.employeedirectory.visitor.DepartmentHeadcountVisitor;
import com.shubham.employeedirectory.visitor.OrgChartPrinterVisitor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregate root for the org chart. Owns the single-CEO and no-cycle invariants, and caches
 * department/location headcount so those reads stay O(1) regardless of company size.
 */
public final class EmployeeDirectoryService {

  private final Map<String, Employee> employeesById = new HashMap<>();
  private final Map<String, Integer> departmentHeadcount = new HashMap<>();
  private final Map<String, Integer> locationHeadcount = new HashMap<>();
  private Employee ceo;

  public Employee addEmployee(
      String id, String name, String managerId, String department, String role, String location) {
    Objects.requireNonNull(id, "id");
    Objects.requireNonNull(name, "name");
    Objects.requireNonNull(role, "role");
    Objects.requireNonNull(location, "location");

    if (employeesById.containsKey(id)) {
      throw new DuplicateEmployeeException(id);
    }

    Employee manager = null;
    if (managerId == null) {
      if (ceo != null) {
        throw new InvalidOperationException(
            "A CEO ('" + ceo.getId() + "') already exists; new employees must report to a manager");
      }
    } else {
      manager = requireEmployee(managerId);
    }

    Employee employee = new Employee(id, name, department, role, location);
    if (manager == null) {
      ceo = employee;
    } else {
      employee.setManager(manager);
      manager.addDirectReport(employee);
    }
    employeesById.put(id, employee);
    adjustHeadcount(employee, 1);
    return employee;
  }

  public void moveEmployee(String employeeId, String newManagerId, boolean withSubtree) {
    Employee employee = requireEmployee(employeeId);
    Employee newManager = requireEmployee(newManagerId);
    if (employee == ceo) {
      throw new InvalidOperationException("Cannot move the CEO");
    }
    if (employee == newManager || isAncestorOrSelf(employee, newManager)) {
      throw new CycleException(employeeId, newManagerId);
    }

    Employee oldManager = employee.getManager();
    if (!withSubtree) {
      for (Employee child : employee.getDirectReports()) {
        child.setManager(oldManager);
        oldManager.addDirectReport(child);
      }
      employee.clearDirectReports();
    }

    oldManager.removeDirectReport(employee);
    newManager.addDirectReport(employee);
    employee.setManager(newManager);
  }

  public void removeEmployee(String employeeId) {
    Employee employee = requireEmployee(employeeId);
    if (employee == ceo) {
      throw new InvalidOperationException("Cannot remove the CEO");
    }

    Employee manager = employee.getManager();
    for (Employee child : employee.getDirectReports()) {
      child.setManager(manager);
      manager.addDirectReport(child);
    }
    employee.clearDirectReports();
    manager.removeDirectReport(employee);

    employeesById.remove(employeeId);
    adjustHeadcount(employee, -1);
  }

  public Employee getEmployee(String employeeId) {
    return requireEmployee(employeeId);
  }

  public Employee getCeo() {
    return ceo;
  }

  public List<Employee> getDirectReports(String employeeId) {
    return requireEmployee(employeeId).getDirectReports();
  }

  /** Full subtree (the employee plus every direct and indirect report), breadth-first. */
  public List<Employee> getSubtree(String employeeId) {
    Employee employee = requireEmployee(employeeId);
    List<Employee> subtree = new ArrayList<>();
    for (Employee member : employee.subtree()) {
      subtree.add(member);
    }
    return subtree;
  }

  /** Chain from this employee up to the CEO, inclusive, starting with the employee. */
  public List<Employee> getChainToCeo(String employeeId) {
    List<Employee> chain = new ArrayList<>();
    for (Employee current = requireEmployee(employeeId);
        current != null;
        current = current.getManager()) {
      chain.add(current);
    }
    return chain;
  }

  /**
   * Deepest employee that is a manager (or self) of both, found by walking both chains to the CEO.
   */
  public Employee lowestCommonManager(String employeeId1, String employeeId2) {
    List<Employee> ancestorsOf1 = getChainToCeo(employeeId1);
    Employee employee2 = requireEmployee(employeeId2);

    Set<Employee> ancestorSet = new HashSet<>(ancestorsOf1);
    for (Employee current = employee2; current != null; current = current.getManager()) {
      if (ancestorSet.contains(current)) {
        return current;
      }
    }
    throw new IllegalStateException("Unreachable: a single-CEO tree always has a common ancestor");
  }

  public Map<String, Integer> headcountByDepartment() {
    return Map.copyOf(departmentHeadcount);
  }

  public Map<String, Integer> headcountByLocation() {
    return Map.copyOf(locationHeadcount);
  }

  /** Department headcount scoped to one employee's subtree (Visitor over the Composite tree). */
  public Map<String, Long> subtreeHeadcountByDepartment(String employeeId) {
    Employee employee = requireEmployee(employeeId);
    DepartmentHeadcountVisitor visitor = new DepartmentHeadcountVisitor();
    employee.accept(visitor);
    return visitor.getCounts();
  }

  public String printOrgChart(String employeeId) {
    Employee employee = requireEmployee(employeeId);
    OrgChartPrinterVisitor visitor = new OrgChartPrinterVisitor();
    employee.accept(visitor);
    return visitor.render();
  }

  private void adjustHeadcount(Employee employee, int delta) {
    if (employee.getDepartment() != null) {
      mergeAndPrune(departmentHeadcount, employee.getDepartment(), delta);
    }
    mergeAndPrune(locationHeadcount, employee.getLocation(), delta);
  }

  private static void mergeAndPrune(Map<String, Integer> counts, String key, int delta) {
    int updated = counts.merge(key, delta, Integer::sum);
    if (updated <= 0) {
      counts.remove(key);
    }
  }

  /**
   * True if {@code candidate} is {@code node} itself or any ancestor of it (manager, manager's
   * manager, ...).
   */
  private boolean isAncestorOrSelf(Employee candidate, Employee node) {
    for (Employee current = node; current != null; current = current.getManager()) {
      if (current == candidate) {
        return true;
      }
    }
    return false;
  }

  private Employee requireEmployee(String employeeId) {
    Employee employee = employeesById.get(employeeId);
    if (employee == null) {
      throw new EmployeeNotFoundException(employeeId);
    }
    return employee;
  }
}

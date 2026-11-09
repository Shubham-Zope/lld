package com.shubham.employeedirectory.model;

import com.shubham.employeedirectory.exception.CycleException;
import com.shubham.employeedirectory.exception.DuplicateEmployeeException;
import com.shubham.employeedirectory.exception.EmployeeNotFoundException;
import com.shubham.employeedirectory.exception.InvalidOperationException;
import com.shubham.employeedirectory.visitor.DepartmentHeadcountVisitor;
import com.shubham.employeedirectory.visitor.OrgChartPrinterVisitor;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Aggregate root for the org chart. Lives in the same package as {@link Employee} so it is the
 * only class that can call its package-private {@link Employee#reparentTo}; every tree mutation
 * goes through here, which is what lets this class guarantee the single-CEO, no-cycle and max-depth
 * invariants instead of merely documenting them. It also caches department/location headcount so
 * those reads stay O(1) regardless of company size.
 */
public final class EmployeeDirectoryService {

    /** Constraint: "Depth up to 20 levels" — the CEO is depth 0, so depth may go up to 20. */
    private static final int MAX_DEPTH = 20;

    private final Map<String, Employee> employeesById = new HashMap<>();
    private final Map<String, Integer> departmentHeadcount = new HashMap<>();
    private final Map<String, Integer> locationHeadcount = new HashMap<>();
    private Employee ceo;

    public Employee addEmployee(String id, String name, String managerId, String department, String role, String location) {
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
            int newDepth = depthOf(manager) + 1;
            if (newDepth > MAX_DEPTH) {
                throw new InvalidOperationException(
                        "Cannot add '" + id + "' under '" + managerId + "': would be at depth " + newDepth
                                + ", exceeding the maximum of " + MAX_DEPTH);
            }
        }

        Employee employee = new Employee(id, name, department, role, location);
        if (manager == null) {
            ceo = employee;
        } else {
            employee.reparentTo(manager);
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

        int newDepth = depthOf(newManager) + 1;
        int movedHeight = withSubtree ? heightOf(employee) : 0;
        if (newDepth + movedHeight > MAX_DEPTH) {
            throw new InvalidOperationException(
                    "Cannot move '" + employeeId + "' under '" + newManagerId + "': would push a report to depth "
                            + (newDepth + movedHeight) + ", exceeding the maximum of " + MAX_DEPTH);
        }

        if (!withSubtree) {
            Employee oldManager = employee.getManager();
            for (Employee child : employee.getDirectReports()) {
                child.reparentTo(oldManager);
            }
        }
        employee.reparentTo(newManager);
    }

    public void removeEmployee(String employeeId) {
        Employee employee = requireEmployee(employeeId);
        if (employee == ceo) {
            throw new InvalidOperationException("Cannot remove the CEO");
        }

        Employee manager = employee.getManager();
        for (Employee child : employee.getDirectReports()) {
            child.reparentTo(manager);
        }
        employee.reparentTo(null);

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
        for (Employee current = requireEmployee(employeeId); current != null; current = current.getManager()) {
            chain.add(current);
        }
        return chain;
    }

    /** Deepest employee that is a manager (or self) of both, found by walking both chains to the CEO. */
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

    /** True if {@code candidate} is {@code node} itself or any ancestor of it (manager, manager's manager, ...). */
    private boolean isAncestorOrSelf(Employee candidate, Employee node) {
        for (Employee current = node; current != null; current = current.getManager()) {
            if (current == candidate) {
                return true;
            }
        }
        return false;
    }

    /** Number of managers above this employee; the CEO is depth 0. O(depth), capped at {@value #MAX_DEPTH}. */
    private int depthOf(Employee employee) {
        int depth = 0;
        for (Employee current = employee; current.getManager() != null; current = current.getManager()) {
            depth++;
        }
        return depth;
    }

    /**
     * Longest chain of reports below this employee (0 for a leaf). O(size of this employee's
     * subtree) — only called when moving a whole subtree, so the cost is proportional to what is
     * actually being relocated, not to the size of the company.
     */
    private int heightOf(Employee root) {
        int height = 0;
        Deque<Employee> level = new ArrayDeque<>(List.of(root));
        while (true) {
            Deque<Employee> nextLevel = new ArrayDeque<>();
            for (Employee employee : level) {
                nextLevel.addAll(employee.getDirectReports());
            }
            if (nextLevel.isEmpty()) {
                return height;
            }
            height++;
            level = nextLevel;
        }
    }

    private Employee requireEmployee(String employeeId) {
        Employee employee = employeesById.get(employeeId);
        if (employee == null) {
            throw new EmployeeNotFoundException(employeeId);
        }
        return employee;
    }
}

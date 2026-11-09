package com.shubham.employeedirectory.visitor;

import com.shubham.employeedirectory.model.Employee;
import com.shubham.employeedirectory.model.EmployeeVisitor;
import java.util.HashMap;
import java.util.Map;

/** Tallies headcount per department across whichever subtree it is applied to. */
public final class DepartmentHeadcountVisitor implements EmployeeVisitor {

    public static final String UNASSIGNED = "UNASSIGNED";

    private final Map<String, Long> counts = new HashMap<>();

    @Override
    public void visit(Employee employee, int depth) {
        String department = employee.getDepartment() == null ? UNASSIGNED : employee.getDepartment();
        counts.merge(department, 1L, Long::sum);
    }

    public Map<String, Long> getCounts() {
        return Map.copyOf(counts);
    }
}

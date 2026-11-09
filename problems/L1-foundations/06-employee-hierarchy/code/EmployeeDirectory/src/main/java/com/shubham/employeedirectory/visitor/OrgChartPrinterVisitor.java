package com.shubham.employeedirectory.visitor;

import com.shubham.employeedirectory.model.Employee;
import com.shubham.employeedirectory.model.EmployeeVisitor;

/** Renders an indented, depth-first text view of the org chart. */
public final class OrgChartPrinterVisitor implements EmployeeVisitor {

  private final StringBuilder output = new StringBuilder();

  @Override
  public void visit(Employee employee, int depth) {
    output
        .repeat("  ", depth)
        .append("- ")
        .append(employee.getName())
        .append(" [")
        .append(employee.getRole())
        .append(']')
        .append(System.lineSeparator());
  }

  public String render() {
    return output.toString();
  }
}

package com.shubham.employeedirectory.model;

public interface EmployeeVisitor {

  void visit(Employee employee, int depth);
}

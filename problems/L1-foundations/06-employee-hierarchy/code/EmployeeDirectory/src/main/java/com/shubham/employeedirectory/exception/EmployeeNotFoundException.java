package com.shubham.employeedirectory.exception;

public final class EmployeeNotFoundException extends RuntimeException {

  public EmployeeNotFoundException(String employeeId) {
    super("No employee with id '" + employeeId + "'");
  }
}

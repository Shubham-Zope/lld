package com.shubham.employeedirectory.exception;

public final class CycleException extends RuntimeException {

  public CycleException(String employeeId, String newManagerId) {
    super(
        "Cannot move '"
            + employeeId
            + "' to report to '"
            + newManagerId
            + "': '"
            + newManagerId
            + "' is '"
            + employeeId
            + "', or currently reports to them "
            + "(directly or indirectly), so this would create a cycle");
  }
}

package com.shubham.employeedirectory.exception;

public final class DuplicateEmployeeException extends RuntimeException {

    public DuplicateEmployeeException(String employeeId) {
        super("An employee with id '" + employeeId + "' already exists");
    }
}

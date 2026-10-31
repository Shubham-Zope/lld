package com.parkinglot.exception;

public class InvalidTicketException extends RuntimeException {
    public InvalidTicketException(String id) {
        super("Invalid or inactive ticket: " + id);
    }
}

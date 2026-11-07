package com.shubham.libraryManagement.service;

import com.shubham.libraryManagement.entities.Fine;

public class HighValueCustomerFineService implements Fine {

    private static final int RATE_PER_DAY = 10;
    private static final int MAX_FINE = 20;

    @Override
    public int calculateFine(long overdueDays) {
        if (overdueDays <= 0) return 0;
        return (int) Math.min(MAX_FINE, RATE_PER_DAY * overdueDays);
    }
}

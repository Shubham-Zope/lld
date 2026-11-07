package com.shubham.libraryManagement.service;

import com.shubham.libraryManagement.entities.Fine;
import com.shubham.libraryManagement.enums.MemberType;

public class FineFactory {

    public static Fine getFineService(MemberType memberType) {

        return switch (memberType) {
            case HIGH_VALUE_CUSTOMER -> new HighValueCustomerFineService();
            case NORMAL -> new NormalValueTypeFineService();
        };
    }
}
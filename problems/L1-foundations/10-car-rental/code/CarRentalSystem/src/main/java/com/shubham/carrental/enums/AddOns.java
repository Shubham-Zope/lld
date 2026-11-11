package com.shubham.carrental.enums;

public enum AddOns {
    GPS(100),
    INSURANCE(250);

    public final int addOnPrice;

    AddOns(int addOnPrice) {
        this.addOnPrice = addOnPrice;
    }
}

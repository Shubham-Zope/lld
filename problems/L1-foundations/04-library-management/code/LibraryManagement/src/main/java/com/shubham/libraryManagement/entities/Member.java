package com.shubham.libraryManagement.entities;

import com.shubham.libraryManagement.enums.MemberStatus;
import com.shubham.libraryManagement.enums.MemberType;

import java.util.HashMap;
import java.util.Map;

public class Member {

    private final String id;
    private final String name;
    private final MemberType type;
    private final Map<String, BookItem> borrowedCopies;
    private MemberStatus status;
    private int outstandingFine;

    public Member(String id, String name, MemberType type) {
        this(id, name, new HashMap<>(), MemberStatus.ACTIVE, type);
    }

    public Member(String id, String name, Map<String, BookItem> borrowedCopies, MemberStatus status, MemberType type) {

        this.id = id;
        this.name = name;
        this.borrowedCopies = new HashMap<>(borrowedCopies);
        this.status = status;
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public MemberType getType() {
        return type;
    }

    public Map<String, BookItem> getBorrowedCopies() {
        return borrowedCopies;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public int countOfBorrowedCopies() {
        return borrowedCopies.size();
    }

    public void setStatus(MemberStatus status) {
        this.status = status;
    }

    public synchronized int getOutstandingFine() {
        return outstandingFine;
    }

    public synchronized void addFine(int amount) {
        if (amount <= 0) return;
        this.outstandingFine += amount;
    }

    public synchronized void payFine(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Payment amount must be positive.");
        }
        if (amount > outstandingFine) {
            throw new IllegalArgumentException("Payment exceeds outstanding fine of Rs" + outstandingFine + ".");
        }
        this.outstandingFine -= amount;
    }
}
package com.shubham.libraryManagement.entities;

import com.shubham.libraryManagement.enums.BookStatus;

import java.util.Date;

public class BookItem {

    private final String id;
    private final String barcode;
    private final Book book;
    private BookStatus status;
    private Member member;
    private Date borrowedTime;
    private Date reservedTime;

    public BookItem(String id, String barcode, Book book) {
        this.id = id;
        this.barcode = barcode;
        this.book = book;
        this.status = BookStatus.AVAILABLE;
        this.member = null;
        this.borrowedTime = null;
        this.reservedTime = null;
    }

    public String getId() {
        return id;
    }

    public String getBarcode() {
        return barcode;
    }

    public Book getBook() {
        return book;
    }

    public BookStatus getStatus() {
        return status;
    }

    public Member getMember() {
        return member;
    }

    public Date getBorrowedTime() {
        return borrowedTime;
    }

    public Date getReservedTime() {
        return reservedTime;
    }

    public void markBorrowed(Member member, Date borrowedTime) {
        this.status = BookStatus.BORROWED;
        this.member = member;
        this.borrowedTime = borrowedTime;
        this.reservedTime = null;
    }

    public void markReturned() {
        this.status = BookStatus.AVAILABLE;
        this.member = null;
        this.borrowedTime = null;
    }

    public void markReserved(Member member, Date reservedTime) {
        this.status = BookStatus.RESERVED;
        this.member = member;
        this.reservedTime = reservedTime;
        this.borrowedTime = null;
    }
}

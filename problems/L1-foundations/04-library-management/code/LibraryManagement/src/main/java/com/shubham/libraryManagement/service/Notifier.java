package com.shubham.libraryManagement.service;

import com.shubham.libraryManagement.entities.Book;
import com.shubham.libraryManagement.entities.Member;

public interface Notifier {
    void notifyAvailable(Member member, Book book);
}

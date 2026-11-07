package com.shubham.libraryManagement.service;

import com.shubham.libraryManagement.entities.Book;
import com.shubham.libraryManagement.entities.Member;

public class ConsoleNotifier implements Notifier {

    @Override
    public void notifyAvailable(Member member, Book book) {
        System.out.printf(
                "Notify %s: your reserved copy of \"%s\" is now available.%n",
                member.getName(), book.title()
        );
    }
}

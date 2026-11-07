package com.shubham.libraryManagement.entities;

import com.shubham.libraryManagement.enums.BookStatus;
import com.shubham.libraryManagement.enums.MemberType;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryFineAndReturnTest {

    private Date daysAgo(int days) {
        return new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(days));
    }

    @Test
    void noFineWhenReturnedWithinLoanPeriod() {
        Library library = new Library();
        Book book = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(book);
        BookItem item = new BookItem("item-1", "BC-1", book);
        library.addBookItem(item);

        Member member = new Member("m1", "Normal Member", MemberType.NORMAL);
        library.registerMember(member);

        item.markBorrowed(member, daysAgo(10));
        member.getBorrowedCopies().put(item.getId(), item);

        int fine = library.returnBook(member, item);

        assertEquals(0, fine);
        assertEquals(BookStatus.AVAILABLE, item.getStatus());
        assertFalse(member.getBorrowedCopies().containsKey(item.getId()));
    }

    @Test
    void normalMemberFineIsCappedAtTen() {
        Library library = new Library();
        Book book = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(book);
        BookItem item = new BookItem("item-1", "BC-1", book);
        library.addBookItem(item);

        Member member = new Member("m1", "Normal Member", MemberType.NORMAL);
        library.registerMember(member);

        item.markBorrowed(member, daysAgo(20)); // 6 days overdue -> 10/day would be 60, capped at 10
        member.getBorrowedCopies().put(item.getId(), item);

        int fine = library.returnBook(member, item);

        assertEquals(10, fine);
    }

    @Test
    void highValueCustomerFineIsCappedAtTwenty() {
        Library library = new Library();
        Book book = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(book);
        BookItem item = new BookItem("item-1", "BC-1", book);
        library.addBookItem(item);

        Member member = new Member("m1", "HVC Member", MemberType.HIGH_VALUE_CUSTOMER);
        library.registerMember(member);

        item.markBorrowed(member, daysAgo(20)); // 6 days overdue -> 10/day would be 60, capped at 20
        member.getBorrowedCopies().put(item.getId(), item);

        int fine = library.returnBook(member, item);

        assertEquals(20, fine);
    }

    @Test
    void returnedCopyBecomesBorrowableAgain() {
        Library library = new Library();
        Book book = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(book);
        BookItem item = new BookItem("item-1", "BC-1", book);
        library.addBookItem(item);

        Member first = new Member("m1", "First", MemberType.NORMAL);
        Member second = new Member("m2", "Second", MemberType.NORMAL);
        library.registerMember(first);
        library.registerMember(second);

        library.borrowCopy(first, book);
        library.returnBook(first, item);

        library.borrowCopy(second, book);

        assertEquals(second, item.getMember());
    }

    @Test
    void sixthActiveLoanIsRejected() {
        Library library = new Library();
        Member member = new Member("m1", "Normal Member", MemberType.NORMAL);
        library.registerMember(member);

        for (int i = 0; i < 5; i++) {
            Book book = new Book("b" + i, "Title " + i, "ISBN-" + i, "Author", "Subject");
            library.addBook(book);
            library.addBookItem(new BookItem("item-" + i, "BC-" + i, book));
            library.borrowCopy(member, book);
        }

        Book sixthBook = new Book("b5", "Title 5", "ISBN-5", "Author", "Subject");
        library.addBook(sixthBook);
        library.addBookItem(new BookItem("item-5", "BC-5", sixthBook));

        assertThrows(IllegalArgumentException.class, () -> library.borrowCopy(member, sixthBook));
        assertTrue(library.isAvailable("ISBN-5"));
    }
}

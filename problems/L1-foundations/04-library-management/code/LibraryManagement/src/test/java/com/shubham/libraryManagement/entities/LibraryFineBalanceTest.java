package com.shubham.libraryManagement.entities;

import com.shubham.libraryManagement.enums.MemberType;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryFineBalanceTest {

    private Date daysAgo(int days) {
        return new Date(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(days));
    }

    @Test
    void memberWithUnpaidFineCannotBorrow() {
        Library library = new Library();

        Book overdueBook = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(overdueBook);
        BookItem overdueItem = new BookItem("item-1", "BC-1", overdueBook);
        library.addBookItem(overdueItem);

        Book otherBook = new Book("b2", "Refactoring", "ISBN-2", "Martin Fowler", "Software");
        library.addBook(otherBook);
        library.addBookItem(new BookItem("item-2", "BC-2", otherBook));

        Member member = new Member("m1", "Alice", MemberType.NORMAL);
        library.registerMember(member);

        overdueItem.markBorrowed(member, daysAgo(20));
        member.getBorrowedCopies().put(overdueItem.getId(), overdueItem);

        int fine = library.returnBook(member, overdueItem);
        assertTrue(fine > 0);
        assertEquals(fine, member.getOutstandingFine());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> library.borrowCopy(member, otherBook)
        );
        assertTrue(exception.getMessage().contains("outstanding fine"));
    }

    @Test
    void payingFineInFullRestoresBorrowingPrivilege() {
        Library library = new Library();

        Book overdueBook = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(overdueBook);
        BookItem overdueItem = new BookItem("item-1", "BC-1", overdueBook);
        library.addBookItem(overdueItem);

        Book otherBook = new Book("b2", "Refactoring", "ISBN-2", "Martin Fowler", "Software");
        library.addBook(otherBook);
        library.addBookItem(new BookItem("item-2", "BC-2", otherBook));

        Member member = new Member("m1", "Alice", MemberType.NORMAL);
        library.registerMember(member);

        overdueItem.markBorrowed(member, daysAgo(20));
        member.getBorrowedCopies().put(overdueItem.getId(), overdueItem);
        int fine = library.returnBook(member, overdueItem);

        library.payFine(member, fine);

        assertEquals(0, member.getOutstandingFine());
        assertDoesNotThrow(() -> library.borrowCopy(member, otherBook));
    }

    @Test
    void partialPaymentStillBlocksBorrowing() {
        Library library = new Library();

        Book overdueBook = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(overdueBook);
        BookItem overdueItem = new BookItem("item-1", "BC-1", overdueBook);
        library.addBookItem(overdueItem);

        Book otherBook = new Book("b2", "Refactoring", "ISBN-2", "Martin Fowler", "Software");
        library.addBook(otherBook);
        library.addBookItem(new BookItem("item-2", "BC-2", otherBook));

        Member member = new Member("m1", "Alice", MemberType.NORMAL);
        library.registerMember(member);

        overdueItem.markBorrowed(member, daysAgo(20));
        member.getBorrowedCopies().put(overdueItem.getId(), overdueItem);
        int fine = library.returnBook(member, overdueItem);

        library.payFine(member, fine - 1);

        assertEquals(1, member.getOutstandingFine());
        assertThrows(IllegalArgumentException.class, () -> library.borrowCopy(member, otherBook));
    }

    @Test
    void cannotOverpayFine() {
        Member member = new Member("m1", "Alice", MemberType.NORMAL);
        member.addFine(5);

        assertThrows(IllegalArgumentException.class, () -> member.payFine(10));
    }
}

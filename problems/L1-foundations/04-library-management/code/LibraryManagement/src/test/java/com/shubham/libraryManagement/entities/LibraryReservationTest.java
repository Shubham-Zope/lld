package com.shubham.libraryManagement.entities;

import com.shubham.libraryManagement.enums.BookStatus;
import com.shubham.libraryManagement.enums.MemberType;
import com.shubham.libraryManagement.service.Notifier;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class LibraryReservationTest {

    private record Notification(Member member, Book book) {
    }

    private static class RecordingNotifier implements Notifier {
        private final List<Notification> notifications = new ArrayList<>();

        @Override
        public void notifyAvailable(Member member, Book book) {
            notifications.add(new Notification(member, book));
        }
    }

    @Test
    void cannotReserveWhenACopyIsAvailable() {
        Library library = new Library();
        Book book = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(book);
        library.addBookItem(new BookItem("item-1", "BC-1", book));

        Member member = new Member("m1", "Alice", MemberType.NORMAL);
        library.registerMember(member);

        assertThrows(IllegalArgumentException.class, () -> library.reserveBook(member, book));
    }

    @Test
    void firstReserverIsNotifiedAndCanClaimReturnedCopy() {
        RecordingNotifier notifier = new RecordingNotifier();
        Library library = new Library(notifier);

        Book book = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(book);
        BookItem item = new BookItem("item-1", "BC-1", book);
        library.addBookItem(item);

        Member borrower = new Member("m1", "Borrower", MemberType.NORMAL);
        Member firstReserver = new Member("m2", "First Reserver", MemberType.NORMAL);
        Member secondReserver = new Member("m3", "Second Reserver", MemberType.NORMAL);
        library.registerMember(borrower);
        library.registerMember(firstReserver);
        library.registerMember(secondReserver);

        library.borrowCopy(borrower, book);

        assertFalse(library.isAvailable(book.ISBN()));
        library.reserveBook(firstReserver, book);
        library.reserveBook(secondReserver, book);

        library.returnBook(borrower, item);

        assertEquals(1, notifier.notifications.size());
        assertEquals(firstReserver, notifier.notifications.get(0).member());
        assertEquals(BookStatus.RESERVED, item.getStatus());
        assertEquals(firstReserver, item.getMember());

        assertThrows(IllegalArgumentException.class, () -> library.borrowCopy(secondReserver, book));

        library.borrowCopy(firstReserver, book);

        assertEquals(BookStatus.BORROWED, item.getStatus());
        assertEquals(firstReserver, item.getMember());
        assertTrue(firstReserver.getBorrowedCopies().containsKey(item.getId()));
    }

    @Test
    void secondReserverIsPromotedAfterFirstReserverClaimsTheirCopy() {
        RecordingNotifier notifier = new RecordingNotifier();
        Library library = new Library(notifier);

        Book book = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(book);
        BookItem item1 = new BookItem("item-1", "BC-1", book);
        BookItem item2 = new BookItem("item-2", "BC-2", book);
        library.addBookItem(item1);
        library.addBookItem(item2);

        Member borrower1 = new Member("m1", "Borrower1", MemberType.NORMAL);
        Member borrower2 = new Member("m2", "Borrower2", MemberType.NORMAL);
        Member firstReserver = new Member("m3", "First Reserver", MemberType.NORMAL);
        Member secondReserver = new Member("m4", "Second Reserver", MemberType.NORMAL);
        library.registerMember(borrower1);
        library.registerMember(borrower2);
        library.registerMember(firstReserver);
        library.registerMember(secondReserver);

        library.borrowCopy(borrower1, book);
        library.borrowCopy(borrower2, book);

        library.reserveBook(firstReserver, book);
        library.reserveBook(secondReserver, book);

        library.returnBook(borrower1, item1);
        assertEquals(firstReserver, item1.getMember());

        library.borrowCopy(firstReserver, book);
        library.returnBook(firstReserver, item1);

        assertEquals(secondReserver, item1.getMember());
        assertEquals(BookStatus.RESERVED, item1.getStatus());
        assertEquals(2, notifier.notifications.size());
    }
}

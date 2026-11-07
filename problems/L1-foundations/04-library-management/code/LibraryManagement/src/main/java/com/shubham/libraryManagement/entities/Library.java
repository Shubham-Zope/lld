package com.shubham.libraryManagement.entities;

import com.shubham.libraryManagement.enums.BookStatus;
import com.shubham.libraryManagement.enums.MemberStatus;
import com.shubham.libraryManagement.service.ConsoleNotifier;
import com.shubham.libraryManagement.service.FineFactory;
import com.shubham.libraryManagement.service.Notifier;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

public class Library {
    private static final int MAX_ACTIVE_LOANS = 5;
    private static final int LOAN_PERIOD_DAYS = 14;

    private final Map<String, Book> booksByISBN;
    private final Map<String, Set<String>> booksByTitle;
    private final Map<String, Set<String>> booksByAuthor;
    private final Map<String, Set<String>> booksBySubject;
    private final Map<String, List<BookItem>> bookItemsByISBN;
    private final List<Member> members;
    private final Map<String, Queue<Member>> reservationQueues;
    private final Notifier notifier;


    public Library() {
        this(new ConsoleNotifier());
    }

    public Library(Notifier notifier) {
        this.booksByISBN = new HashMap<>();
        this.booksByTitle = new HashMap<>();
        this.booksByAuthor = new HashMap<>();
        this.booksBySubject = new HashMap<>();
        this.bookItemsByISBN = new ConcurrentHashMap<>();
        this.members = new ArrayList<>();
        this.reservationQueues = new ConcurrentHashMap<>();
        this.notifier = notifier;
    }

    public void addBook(Book book) {
        booksByISBN.put(book.ISBN(), book);
        indexTokens(booksByTitle, book.title(), book.ISBN());
        indexTokens(booksByAuthor, book.author(), book.ISBN());
        indexTokens(booksBySubject, book.subject(), book.ISBN());
    }

    public void addBookItem(BookItem bookItem) {
        bookItemsByISBN.computeIfAbsent(bookItem.getBook().ISBN(), isbn -> new CopyOnWriteArrayList<>()).add(bookItem);
    }

    public Optional<BookItem> findAvailableCopy(String isbn) {
        return bookItemsByISBN.getOrDefault(isbn, List.of()).stream()
                .filter(item -> item.getStatus() == BookStatus.AVAILABLE)
                .findFirst();
    }

    public boolean isAvailable(String isbn) {
        return findAvailableCopy(isbn).isPresent();
    }

    public void removeBook(Book book) {
        booksByISBN.remove(book.ISBN());
        removeTokens(booksByTitle, book.title(), book.ISBN());
        removeTokens(booksByAuthor, book.author(), book.ISBN());
        removeTokens(booksBySubject, book.subject(), book.ISBN());
    }

    private void indexTokens(Map<String, Set<String>> index, String text, String isbn) {
        for (String token : tokenize(text)) {
            index.computeIfAbsent(token, t -> new HashSet<>()).add(isbn);
        }
    }

    private void removeTokens(Map<String, Set<String>> index, String text, String isbn) {
        for (String token : tokenize(text)) {
            Set<String> isbns = index.get(token);
            if (isbns == null) continue;
            isbns.remove(isbn);
            if (isbns.isEmpty()) index.remove(token);
        }
    }

    private List<String> tokenize(String text) {
        if (text == null || text.isBlank()) return List.of();
        return Arrays.stream(text.toLowerCase().split("[^a-z0-9]+"))
                .filter(token -> !token.isBlank())
                .toList();
    }

    public List<Book> searchBook(String query) {
        if (query == null || query.isBlank()) return List.of();

        Book exactIsbnMatch = booksByISBN.get(query.trim());
        if (exactIsbnMatch != null) {
            return List.of(exactIsbnMatch);
        }

        Set<String> matchingIsbns = new HashSet<>();
        matchingIsbns.addAll(matchTokens(booksByTitle, query));
        matchingIsbns.addAll(matchTokens(booksByAuthor, query));
        matchingIsbns.addAll(matchTokens(booksBySubject, query));

        List<Book> results = new ArrayList<>();
        for (String isbn : matchingIsbns) {
            results.add(booksByISBN.get(isbn));
        }
        return results;
    }

    private Set<String> matchTokens(Map<String, Set<String>> index, String query) {
        Set<String> result = new HashSet<>();
        for (String token : tokenize(query)) {
            result.addAll(index.getOrDefault(token, Set.of()));
        }
        return result;
    }

    public void borrowCopy(Member member, Book book) {
        if (!validateMember(member)) {
            throw new IllegalArgumentException(describeBorrowRejection(member));
        }

        List<BookItem> copies = bookItemsByISBN.getOrDefault(book.ISBN(), List.of());

        for (BookItem bookItem : copies) {
            synchronized (bookItem) {
                boolean freelyAvailable = bookItem.getStatus() == BookStatus.AVAILABLE;
                boolean heldForThisMember = bookItem.getStatus() == BookStatus.RESERVED
                        && member.equals(bookItem.getMember());

                if (!freelyAvailable && !heldForThisMember) {
                    continue;
                }

                bookItem.markBorrowed(member, new Date());
                member.getBorrowedCopies().put(bookItem.getId(), bookItem);
                return;
            }
        }

        throw new IllegalArgumentException(
                "No Book Copy Available for " + book.title() + " do you want to reverse this instead?"
        );
    }

    public void reserveBook(Member member, Book book) {
        if (!isEligibleMember(member)) {
            throw new IllegalArgumentException(
                    "You are not permitted to reserve this book."
            );
        }

        if (isAvailable(book.ISBN())) {
            throw new IllegalArgumentException(
                    "A copy of \"" + book.title() + "\" is available right now; borrow it instead of reserving."
            );
        }

        Queue<Member> queue = reservationQueues.computeIfAbsent(book.ISBN(), isbn -> new ConcurrentLinkedQueue<>());
        synchronized (queue) {
            if (!queue.contains(member)) {
                queue.add(member);
            }
        }
    }

    public void registerMember(Member member) {
        members.add(member);
    }

    public void blockMember(Member member) {
        member.setStatus(MemberStatus.BLOCK);
    }

    public boolean validateMember(Member member) {
        if (!isEligibleMember(member))
            return false;

        if (member.getOutstandingFine() > 0) {
            return false;
        }

        if (member.countOfBorrowedCopies() >= MAX_ACTIVE_LOANS) {
            return false;
        }

        return true;
    }

    public void payFine(Member member, int amount) {
        if (!members.contains(member)) {
            throw new IllegalArgumentException("Unknown member.");
        }
        member.payFine(amount);
    }

    private boolean isEligibleMember(Member member) {
        return members.contains(member) && !member.getStatus().equals(MemberStatus.BLOCK);
    }

    private String describeBorrowRejection(Member member) {
        if (!isEligibleMember(member)) {
            return "You are not permitted to borrow a copy of this book.";
        }
        if (member.getOutstandingFine() > 0) {
            return member.getName() + " has an outstanding fine of Rs" + member.getOutstandingFine()
                    + "; please pay it before borrowing.";
        }
        if (member.countOfBorrowedCopies() >= MAX_ACTIVE_LOANS) {
            return member.getName() + " has reached the maximum of " + MAX_ACTIVE_LOANS + " active loans.";
        }
        return "You are not permitted to borrow a copy of this book.";
    }

    public void unblockMember(Member member) {
        member.setStatus(MemberStatus.ACTIVE);
    }

    public int returnBook(Member member, BookItem bookItem) {
        synchronized (bookItem) {
            if (bookItem.getStatus() != BookStatus.BORROWED
                    || !member.getBorrowedCopies().containsKey(bookItem.getId())) {
                throw new IllegalArgumentException(
                        member.getName() + " has not borrowed this copy of " + bookItem.getBook().title() + "."
                );
            }

            long daysBorrowed = daysBetween(bookItem.getBorrowedTime(), new Date());
            long overdueDays = Math.max(0, daysBorrowed - LOAN_PERIOD_DAYS);
            int fine = FineFactory.getFineService(member.getType()).calculateFine(overdueDays);
            member.addFine(fine);

            bookItem.markReturned();
            member.getBorrowedCopies().remove(bookItem.getId());

            promoteNextReservation(bookItem);

            return fine;
        }
    }

    private void promoteNextReservation(BookItem bookItem) {
        Queue<Member> queue = reservationQueues.get(bookItem.getBook().ISBN());
        if (queue == null) return;

        Member nextReserver;
        synchronized (queue) {
            while ((nextReserver = queue.poll()) != null && !isEligibleMember(nextReserver)) {
                // skip reservers who are no longer registered or who got blocked
            }
        }

        if (nextReserver == null) return;

        bookItem.markReserved(nextReserver, new Date());
        notifier.notifyAvailable(nextReserver, bookItem.getBook());
    }

    private long daysBetween(Date start, Date end) {
        return TimeUnit.MILLISECONDS.toDays(end.getTime() - start.getTime());
    }
}

package com.shubham.libraryManagement.entities;

import com.shubham.libraryManagement.enums.MemberType;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LibraryConcurrencyTest {

    @Test
    void onlyOneMemberCanBorrowTheOnlyCopy() throws InterruptedException {
        Library library = new Library();
        Book book = new Book("b1", "Clean Code", "ISBN-1", "Robert Martin", "Software");
        library.addBook(book);
        library.addBookItem(new BookItem("item-1", "BC-1", book));

        int memberCount = 50;
        List<Member> members = new CopyOnWriteArrayList<>();
        for (int i = 0; i < memberCount; i++) {
            Member member = new Member("m" + i, "Member " + i, MemberType.NORMAL);
            library.registerMember(member);
            members.add(member);
        }

        ExecutorService executor = Executors.newFixedThreadPool(memberCount);
        CountDownLatch startGate = new CountDownLatch(1);
        AtomicInteger successCount = new AtomicInteger();
        AtomicInteger failureCount = new AtomicInteger();

        for (Member member : members) {
            executor.submit(() -> {
                try {
                    startGate.await();
                    library.borrowCopy(member, book);
                    successCount.incrementAndGet();
                } catch (IllegalArgumentException expected) {
                    failureCount.incrementAndGet();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            });
        }

        startGate.countDown();
        executor.shutdown();
        assertEquals(true, executor.awaitTermination(10, TimeUnit.SECONDS));

        assertEquals(1, successCount.get());
        assertEquals(memberCount - 1, failureCount.get());
    }
}

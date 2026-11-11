package com.shubham.courseRegistrationSystem;

import static com.shubham.courseRegistrationSystem.TestSystem.FALL;
import static com.shubham.courseRegistrationSystem.TestSystem.slot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.shubham.courseRegistrationSystem.entities.Enrollment;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.results.RegistrationResult;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;

class ConcurrencyTest {

  /** Starts every task at the same moment and returns their results. */
  private static <T> List<T> runTogether(List<Callable<T>> tasks) throws Exception {
    ExecutorService pool = Executors.newFixedThreadPool(32);
    CountDownLatch start = new CountDownLatch(1);
    try {
      List<Future<T>> futures = new ArrayList<>();
      for (Callable<T> task : tasks) {
        futures.add(
            pool.submit(
                () -> {
                  start.await();
                  return task.call();
                }));
      }
      start.countDown();
      List<T> results = new ArrayList<>();
      for (Future<T> f : futures) {
        results.add(f.get(30, TimeUnit.SECONDS));
      }
      return results;
    } finally {
      pool.shutdownNow();
    }
  }

  private static long enrolledCount(TestSystem t, Section section) {
    return t.system.enrollments().findBySection(section.getSectionId()).stream()
        .filter(Enrollment::isEnrolled)
        .count();
  }

  @RepeatedTest(5)
  void manyStudentsRacingForFewSeatsNeverOverEnrol() throws Exception {
    TestSystem t = new TestSystem();
    Section section = t.section("A", t.course("C1", 4), FALL, 5);
    List<Callable<RegistrationResult>> tasks = new ArrayList<>();
    for (int i = 0; i < 200; i++) {
      String id = "s" + i;
      t.student(id, 2);
      tasks.add(() -> t.registration().register(id, "A"));
    }

    List<RegistrationResult> results = runTogether(tasks);

    long enrolled = results.stream().filter(r -> r instanceof RegistrationResult.Enrolled).count();
    Set<Integer> positions = new HashSet<>();
    for (RegistrationResult r : results) {
      if (r instanceof RegistrationResult.Waitlisted w) {
        positions.add(w.position());
      }
    }
    assertEquals(5, enrolled);
    assertEquals(5, section.getSeatsTaken());
    assertEquals(5, enrolledCount(t, section));
    assertEquals(195, section.getWaitList().size());
    assertEquals(195, positions.size(), "every waitlist position handed out once");
  }

  @Test
  void twoStudentsTakingTheLastSeat() throws Exception {
    for (int run = 0; run < 50; run++) {
      TestSystem t = new TestSystem();
      Section section = t.section("A", t.course("C1", 4), FALL, 1);
      t.student("x", 2);
      t.student("y", 2);

      List<RegistrationResult> results =
          runTogether(
              List.of(
                  () -> t.registration().register("x", "A"),
                  () -> t.registration().register("y", "A")));

      long enrolled = results.stream().filter(r -> r instanceof RegistrationResult.Enrolled).count();
      long waitlisted =
          results.stream()
              .filter(r -> r instanceof RegistrationResult.Waitlisted w && w.position() == 1)
              .count();
      assertEquals(1, enrolled);
      assertEquals(1, waitlisted);
      assertEquals(1, section.getSeatsTaken());
    }
  }

  @RepeatedTest(5)
  void sameStudentCannotSlipPastClashOrCapWithParallelRequests() throws Exception {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    List<Callable<RegistrationResult>> tasks = new ArrayList<>();
    for (int i = 0; i < 20; i++) {
      t.section("S" + i, t.course("C" + i, 4), FALL, 50, slot("THU", 9)); // all clash
      String sectionId = "S" + i;
      tasks.add(() -> t.registration().register("s1", sectionId));
    }

    List<RegistrationResult> results = runTogether(tasks);

    assertEquals(
        1, results.stream().filter(r -> r instanceof RegistrationResult.Enrolled).count());
    assertEquals(1, t.registration().viewTimetable("s1", FALL).getEntries().size());
  }

  @RepeatedTest(5)
  void dropsAndRegistrationsAtOnceKeepSeatsConsistent() throws Exception {
    TestSystem t = new TestSystem();
    Section section = t.section("A", t.course("C1", 4), FALL, 10);
    for (int i = 0; i < 10; i++) {
      t.student("e" + i, 2);
      t.registration().register("e" + i, "A");
    }
    for (int i = 0; i < 30; i++) {
      t.student("w" + i, 2);
      t.registration().register("w" + i, "A");
    }
    List<Callable<Object>> tasks = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      String id = "e" + i;
      tasks.add(() -> t.registration().drop(id, "A"));
    }
    for (int i = 0; i < 30; i++) {
      String id = "n" + i;
      t.student(id, 2);
      tasks.add(() -> t.registration().register(id, "A"));
    }

    runTogether(tasks);

    assertEquals(10, section.getSeatsTaken());
    assertEquals(10, enrolledCount(t, section));
    assertFalse(section.hasAvailableSeat() && !section.getWaitList().isEmpty());
    // the 10 freed seats went to the first 10 who were already waiting, not to newcomers
    for (int i = 0; i < 10; i++) {
      String id = "w" + i;
      assertTrue(
          t.system.enrollments().findActive(id, "A").orElseThrow().isEnrolled(),
          id + " should have been promoted");
    }
  }
}

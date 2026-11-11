package com.shubham.courseRegistrationSystem.interview;

import com.shubham.courseRegistrationSystem.interview.RegistrationResult.Reason;
import com.shubham.courseRegistrationSystem.interview.Validator.WindowValidator;
import com.shubham.courseRegistrationSystem.interview.Validator.WindowValidator.Window;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Main {
  public static void main(String[] args) throws Exception {
    Map<Integer, Window> windows = new HashMap<>();
    LocalDateTime now = LocalDateTime.now();
    for (int year = 1; year <= 4; year++) {
      windows.put(year, new Window(now.minusDays(1), now.plusDays(7)));
    }
    RegistrationService service = new RegistrationService(new WindowValidator(windows));
    service.addListener(
        new WaitlistListener() {
          public void onPromoted(Student s, Section sec) {
            System.out.println("  notify " + s.id + ": enrolled in " + sec.id);
          }

          public void onSkipped(Student s, Section sec, List<Reason> reasons) {
            System.out.println("  notify " + s.id + ": skipped for " + sec.id + " " + reasons);
          }
        });

    TimeSlot mon9 = new TimeSlot(DayOfWeek.MONDAY, LocalTime.of(9, 0), LocalTime.of(10, 0));
    Course algo = new Course("CS201", 4, List.of("CS101"));
    Course db = new Course("CS301", 4, List.of());
    Section algoA = new Section("CS201-A", algo, List.of(mon9), 1);
    Section dbA = new Section("CS301-A", db, List.of(mon9), 10);

    Student alice = new Student("alice", 2);
    Student bob = new Student("bob", 2);
    Student carol = new Student("carol", 2);
    Student dave = new Student("dave", 2);
    for (Student s : List.of(alice, bob, carol)) {
      s.transcript.add(new Student.TranscriptEntry("CS101", "CS101-A", "B", 4)); // last term
    }

    System.out.println("alice: " + service.register(alice, algoA)); // Enrolled
    System.out.println("bob:   " + service.register(bob, algoA)); // Waitlisted(1)
    System.out.println("carol: " + service.register(carol, algoA)); // Waitlisted(2)
    System.out.println("dave:  " + service.register(dave, algoA)); // Rejected[PREREQ]
    System.out.println("bob takes clashing DB: " + service.register(bob, dbA));
    System.out.println("alice drops:");
    service.drop(alice, algoA); // bob skipped (clash), carol promoted
    System.out.println("bob timetable: " + service.viewTimetable(bob));
    service.recordGrade(carol, algoA, "A");
    System.out.println("carol transcript: " + service.viewTranscript(carol));

    // Two hundred students, five seats, all at once
    Section hot = new Section("HOT", new Course("HOT", 3, List.of()), List.of(mon9), 5);
    ExecutorService pool = Executors.newFixedThreadPool(32);
    List<RegistrationResult> results = java.util.Collections.synchronizedList(new ArrayList<>());
    for (int i = 0; i < 200; i++) {
      Student s = new Student("s" + i, 1);
      pool.submit(() -> results.add(service.register(s, hot)));
    }
    pool.shutdown();
    pool.awaitTermination(10, TimeUnit.SECONDS);
    long enrolled = results.stream().filter(r -> r instanceof RegistrationResult.Enrolled).count();
    System.out.println("race: enrolled=" + enrolled + " waitlisted=" + hot.waitlist.size());
  }
}

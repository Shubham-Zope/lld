package com.shubham.courseRegistrationSystem;

import com.shubham.courseRegistrationSystem.entities.Course;
import com.shubham.courseRegistrationSystem.entities.Instructor;
import com.shubham.courseRegistrationSystem.entities.RegistrationWindow;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.Student;
import com.shubham.courseRegistrationSystem.entities.Term;
import com.shubham.courseRegistrationSystem.entities.TimeSlot;
import com.shubham.courseRegistrationSystem.entities.WaitList;
import com.shubham.courseRegistrationSystem.policies.DropPolicy;
import com.shubham.courseRegistrationSystem.policies.PrerequisitePolicy;
import com.shubham.courseRegistrationSystem.policies.RejectAfterDeadlineDropPolicy;
import com.shubham.courseRegistrationSystem.policies.StrictPrerequisitePolicy;
import com.shubham.courseRegistrationSystem.policies.WaitListOrderingPolicy;
import com.shubham.courseRegistrationSystem.services.RegistrationService;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Test fixture. Two terms: FALL (in progress, drop deadline Aug 20) and SPRING. Registration for
 * both opens Jul 20 for 4th years and Jul 25 for everyone else, and closes Aug 10. The clock starts
 * at Aug 1, 10:00, so every window is open.
 */
public class TestSystem {
  public static final String FALL = "FALL";
  public static final String SPRING = "SPRING";

  public final MutableClock clock = new MutableClock(LocalDateTime.of(2026, 8, 1, 10, 0));
  public final Queue<String> notifications = new ConcurrentLinkedQueue<>();
  public final CourseRegistrationSystem system;
  private final AtomicInteger slotCounter = new AtomicInteger();

  public TestSystem() {
    this(new StrictPrerequisitePolicy(), new RejectAfterDeadlineDropPolicy());
  }

  public TestSystem(PrerequisitePolicy prerequisitePolicy, DropPolicy dropPolicy) {
    system = new CourseRegistrationSystem(clock, prerequisitePolicy, dropPolicy, notifications::add);
    system
        .catalog()
        .addTerm(
            new Term(
                FALL,
                LocalDateTime.of(2026, 8, 1, 0, 0),
                LocalDateTime.of(2026, 12, 15, 0, 0),
                LocalDateTime.of(2026, 8, 20, 23, 59)));
    system
        .catalog()
        .addTerm(
            new Term(
                SPRING,
                LocalDateTime.of(2027, 1, 10, 0, 0),
                LocalDateTime.of(2027, 5, 15, 0, 0),
                LocalDateTime.of(2027, 1, 31, 23, 59)));
    for (String term : List.of(FALL, SPRING)) {
      for (int year = 1; year <= 4; year++) {
        LocalDateTime opens =
            year == 4 ? LocalDateTime.of(2026, 7, 20, 0, 0) : LocalDateTime.of(2026, 7, 25, 0, 0);
        system
            .catalog()
            .addRegistrationWindow(
                new RegistrationWindow(term, year, opens, LocalDateTime.of(2026, 8, 10, 0, 0)));
      }
    }
  }

  public RegistrationService registration() {
    return system.registration();
  }

  public Student student(String id, int year) {
    Student student = new Student(id, id, year);
    system.catalog().addStudent(student);
    return student;
  }

  public Course course(String id, int credits, String... prerequisites) {
    Course course = new Course(id, id, credits, List.of(prerequisites));
    system.catalog().addCourse(course);
    return course;
  }

  public Section section(String id, Course course, String term, int capacity, TimeSlot... slots) {
    return section(id, course, term, capacity, new WaitList(), slots);
  }

  public Section section(
      String id, Course course, String term, int capacity, WaitListOrderingPolicy policy) {
    return section(id, course, term, capacity, new WaitList(policy));
  }

  private Section section(
      String id, Course course, String term, int capacity, WaitList waitList, TimeSlot... slots) {
    List<TimeSlot> slotList = slots.length > 0 ? List.of(slots) : List.of(uniqueSlot());
    // own instructor per section so tests are never blocked by the instructor clash rule
    Instructor instructor = new Instructor("I-" + id, "Instructor " + id);
    Section section = new Section(id, course, term, capacity, slotList, instructor, waitList);
    system.catalog().addSection(section);
    return section;
  }

  /** A one-hour slot that overlaps nothing else made by this fixture. */
  public TimeSlot uniqueSlot() {
    int n = slotCounter.getAndIncrement();
    String[] days = {"MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN"};
    return slot(days[n % 7], 6 + n / 7);
  }

  public static TimeSlot slot(String day, int startHour) {
    return new TimeSlot(day, LocalTime.of(startHour, 0), LocalTime.of(startHour + 1, 0));
  }

  /** A clock tests can move. */
  public static final class MutableClock extends Clock {
    private volatile Instant instant;

    MutableClock(LocalDateTime start) {
      set(start);
    }

    public void set(LocalDateTime time) {
      this.instant = time.toInstant(ZoneOffset.UTC);
    }

    @Override
    public ZoneId getZone() {
      return ZoneOffset.UTC;
    }

    @Override
    public Clock withZone(ZoneId zone) {
      return this;
    }

    @Override
    public Instant instant() {
      return instant;
    }
  }
}

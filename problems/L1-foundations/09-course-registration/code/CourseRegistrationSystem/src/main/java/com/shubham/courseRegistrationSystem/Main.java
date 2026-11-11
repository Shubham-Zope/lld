package com.shubham.courseRegistrationSystem;

import com.shubham.courseRegistrationSystem.entities.Course;
import com.shubham.courseRegistrationSystem.entities.Instructor;
import com.shubham.courseRegistrationSystem.entities.RegistrationWindow;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.Student;
import com.shubham.courseRegistrationSystem.entities.Term;
import com.shubham.courseRegistrationSystem.entities.TimeSlot;
import com.shubham.courseRegistrationSystem.entities.WaitList;
import com.shubham.courseRegistrationSystem.services.CatalogService;
import com.shubham.courseRegistrationSystem.services.RegistrationService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

/** Small demo: last seat, waitlist, drop, auto-enrol. */
public class Main {
  public static void main(String[] args) {
    CourseRegistrationSystem system = new CourseRegistrationSystem();
    CatalogService catalog = system.catalog();
    RegistrationService registration = system.registration();

    LocalDateTime now = LocalDateTime.now();
    catalog.addTerm(new Term("FALL", now.minusDays(1), now.plusMonths(4), now.plusWeeks(2)));
    for (int year = 1; year <= 4; year++) {
      catalog.addRegistrationWindow(
          new RegistrationWindow("FALL", year, now.minusHours(1), now.plusDays(7)));
    }

    Course algorithms = new Course("CS201", "Algorithms", 4, List.of());
    catalog.addCourse(algorithms);
    Instructor prof = new Instructor("I1", "Prof. Rao");
    Section section =
        new Section(
            "CS201-A",
            algorithms,
            "FALL",
            1,
            List.of(new TimeSlot("MON", LocalTime.of(9, 0), LocalTime.of(10, 30))),
            prof,
            new WaitList());
    catalog.addSection(section);

    catalog.addStudent(new Student("alice", "Alice", 2));
    catalog.addStudent(new Student("bob", "Bob", 3));

    System.out.println("alice -> " + registration.register("alice", "CS201-A"));
    System.out.println("bob   -> " + registration.register("bob", "CS201-A"));
    System.out.println("alice drops -> " + registration.drop("alice", "CS201-A"));
    System.out.println("bob's timetable: " + registration.viewTimetable("bob", "FALL").byDay());
  }
}

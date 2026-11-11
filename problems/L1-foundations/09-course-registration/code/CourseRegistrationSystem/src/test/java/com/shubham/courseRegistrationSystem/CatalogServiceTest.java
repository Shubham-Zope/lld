package com.shubham.courseRegistrationSystem;

import static com.shubham.courseRegistrationSystem.TestSystem.FALL;
import static com.shubham.courseRegistrationSystem.TestSystem.SPRING;
import static com.shubham.courseRegistrationSystem.TestSystem.slot;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.shubham.courseRegistrationSystem.entities.Course;
import com.shubham.courseRegistrationSystem.entities.Instructor;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.WaitList;
import com.shubham.courseRegistrationSystem.services.CatalogService;
import java.util.List;
import org.junit.jupiter.api.Test;

class CatalogServiceTest {

  @Test
  void prerequisiteMustAlreadyExist() {
    CatalogService catalog = new TestSystem().system.catalog();
    assertThrows(
        IllegalArgumentException.class,
        () -> catalog.addCourse(new Course("ADV", "Adv", 4, List.of("MISSING"))));
  }

  @Test
  void courseCannotBeReplaced() {
    TestSystem t = new TestSystem();
    t.course("A", 4);
    assertThrows(
        IllegalArgumentException.class,
        () -> t.system.catalog().addCourse(new Course("A", "A again", 3, List.of())));
  }

  @Test
  void instructorCannotTeachTwoOverlappingSectionsInOneTerm() {
    TestSystem t = new TestSystem();
    Course c1 = t.course("C1", 4);
    Course c2 = t.course("C2", 4);
    Instructor prof = new Instructor("P", "Prof");
    CatalogService catalog = t.system.catalog();
    catalog.addSection(new Section("A", c1, FALL, 10, List.of(slot("MON", 9)), prof, new WaitList()));

    assertThrows(
        IllegalArgumentException.class,
        () ->
            catalog.addSection(
                new Section("B", c2, FALL, 10, List.of(slot("MON", 9)), prof, new WaitList())));
    assertDoesNotThrow(
        () ->
            catalog.addSection(
                new Section("C", c2, SPRING, 10, List.of(slot("MON", 9)), prof, new WaitList())));
  }

  @Test
  void sectionNeedsKnownTermAndRegisteredCourse() {
    TestSystem t = new TestSystem();
    Course unregistered = new Course("X", "X", 4, List.of());
    Instructor prof = new Instructor("P", "Prof");
    CatalogService catalog = t.system.catalog();
    assertThrows(
        IllegalArgumentException.class,
        () ->
            catalog.addSection(
                new Section("A", unregistered, FALL, 10, List.of(slot("MON", 9)), prof, new WaitList())));
    Course c1 = t.course("C1", 4);
    assertThrows(
        IllegalArgumentException.class,
        () ->
            catalog.addSection(
                new Section("B", c1, "WINTER", 10, List.of(slot("MON", 9)), prof, new WaitList())));
  }
}

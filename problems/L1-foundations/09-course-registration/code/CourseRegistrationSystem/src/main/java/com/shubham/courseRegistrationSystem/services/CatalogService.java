package com.shubham.courseRegistrationSystem.services;

import com.shubham.courseRegistrationSystem.entities.Course;
import com.shubham.courseRegistrationSystem.entities.RegistrationWindow;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.Student;
import com.shubham.courseRegistrationSystem.entities.Term;
import com.shubham.courseRegistrationSystem.repositories.CourseRepository;
import com.shubham.courseRegistrationSystem.repositories.RegistrationWindowRepository;
import com.shubham.courseRegistrationSystem.repositories.SectionRepository;
import com.shubham.courseRegistrationSystem.repositories.StudentRepository;
import com.shubham.courseRegistrationSystem.repositories.TermRepository;
import java.util.Objects;

/**
 * Admin side: sets up terms, windows, students, courses and sections, and keeps the catalog valid.
 * Low traffic, so methods are simply synchronized.
 */
public class CatalogService {
  private final TermRepository terms;
  private final RegistrationWindowRepository windows;
  private final StudentRepository students;
  private final CourseRepository courses;
  private final SectionRepository sections;

  public CatalogService(
      TermRepository terms,
      RegistrationWindowRepository windows,
      StudentRepository students,
      CourseRepository courses,
      SectionRepository sections) {
    this.terms = Objects.requireNonNull(terms, "terms");
    this.windows = Objects.requireNonNull(windows, "windows");
    this.students = Objects.requireNonNull(students, "students");
    this.courses = Objects.requireNonNull(courses, "courses");
    this.sections = Objects.requireNonNull(sections, "sections");
  }

  public synchronized void addTerm(Term term) {
    if (terms.findById(term.getTermId()).isPresent()) {
      throw new IllegalArgumentException("Term " + term.getTermId() + " already exists");
    }
    terms.save(term);
  }

  public synchronized void addRegistrationWindow(RegistrationWindow window) {
    if (terms.findById(window.getTermId()).isEmpty()) {
      throw new IllegalArgumentException("Unknown term " + window.getTermId());
    }
    windows.save(window);
  }

  public synchronized void addStudent(Student student) {
    if (students.findById(student.getStudentId()).isPresent()) {
      throw new IllegalArgumentException("Student " + student.getStudentId() + " already exists");
    }
    students.save(student);
  }

  /**
   * Every prerequisite must already exist and a course can never be replaced. A new course can then
   * only point at older courses, so a prerequisite cycle (A needs B, B needs A) cannot be built.
   */
  public synchronized void addCourse(Course course) {
    if (courses.findById(course.getCourseId()).isPresent()) {
      throw new IllegalArgumentException("Course " + course.getCourseId() + " already exists");
    }
    for (String prerequisiteId : course.getPrerequisiteIds()) {
      if (courses.findById(prerequisiteId).isEmpty()) {
        throw new IllegalArgumentException(
            "Prerequisite " + prerequisiteId + " of " + course.getCourseId() + " does not exist");
      }
    }
    courses.save(course);
  }

  /**
   * Adds a section to its course and instructor. Rejects it if the instructor already teaches an
   * overlapping slot in the same term.
   */
  public synchronized void addSection(Section section) {
    Course course = section.getCourse();
    if (courses.findById(course.getCourseId()).orElse(null) != course) {
      throw new IllegalArgumentException("Course " + course.getCourseId() + " is not registered");
    }
    if (terms.findById(section.getTermId()).isEmpty()) {
      throw new IllegalArgumentException("Unknown term " + section.getTermId());
    }
    if (sections.findById(section.getSectionId()).isPresent()) {
      throw new IllegalArgumentException("Section " + section.getSectionId() + " already exists");
    }
    for (Section other : section.getInstructor().getTeaches()) {
      if (other.getTermId().equals(section.getTermId()) && other.clashesWith(section)) {
        throw new IllegalArgumentException(
            "Instructor "
                + section.getInstructor().getInstructorId()
                + " already teaches "
                + other.getSectionId()
                + " at that time");
      }
    }
    course.addSection(section);
    section.getInstructor().assignSection(section);
    sections.save(section);
  }
}

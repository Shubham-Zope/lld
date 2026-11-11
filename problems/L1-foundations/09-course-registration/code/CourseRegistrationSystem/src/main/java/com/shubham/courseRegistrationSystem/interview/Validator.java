package com.shubham.courseRegistrationSystem.interview;

import com.shubham.courseRegistrationSystem.interview.RegistrationResult.Reason;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Chain of Responsibility. Every link runs and adds its reason, so Rejected lists all failures.
 * New rule = new subclass + one setNext() link.
 */
public abstract class Validator {
  private Validator next;

  public Validator setNext(Validator next) {
    this.next = next;
    return next;
  }

  public void validate(Student student, Section section, List<Reason> errors) {
    check(student, section, errors);
    if (next != null) {
      next.validate(student, section, errors);
    }
  }

  protected abstract void check(Student student, Section section, List<Reason> errors);

  // ---- rules ----

  /** Registration windows by student year. Seniors get earlier windows = priority. */
  public static class WindowValidator extends Validator {
    public record Window(LocalDateTime opens, LocalDateTime closes) {}

    private final Map<Integer, Window> windowByYear;

    public WindowValidator(Map<Integer, Window> windowByYear) {
      this.windowByYear = windowByYear;
    }

    @Override
    protected void check(Student student, Section section, List<Reason> errors) {
      Window w = windowByYear.get(student.year);
      LocalDateTime now = LocalDateTime.now();
      if (w == null || now.isBefore(w.opens()) || now.isAfter(w.closes())) {
        errors.add(Reason.WINDOW_CLOSED);
      }
    }
  }

  public static class PrerequisiteValidator extends Validator {
    @Override
    protected void check(Student student, Section section, List<Reason> errors) {
      if (!section.course.prereqIds().stream().allMatch(student::hasCompleted)) {
        errors.add(Reason.PREREQUISITE_MISSING);
      }
    }
  }

  public static class CreditCapValidator extends Validator {
    @Override
    protected void check(Student student, Section section, List<Reason> errors) {
      if (student.creditsThisTerm() + section.course.credits() > Student.MAX_CREDITS) {
        errors.add(Reason.CREDIT_CAP_EXCEEDED);
      }
    }
  }

  public static class TimeClashValidator extends Validator {
    @Override
    protected void check(Student student, Section section, List<Reason> errors) {
      if (student.enrolledSections.stream().anyMatch(section::clashesWith)) {
        errors.add(Reason.TIME_CLASH);
      }
    }
  }
}

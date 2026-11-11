package com.shubham.courseRegistrationSystem.interview;

import com.shubham.courseRegistrationSystem.interview.RegistrationResult.Reason;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Locking: always student first, then section. Never take a student lock while holding a section
 * lock, so there is no deadlock.
 *
 * <ul>
 *   <li>student lock: two requests from one student can't both pass credit cap / clash
 *   <li>section lock: two students can't both take the last seat
 * </ul>
 */
public class RegistrationService {
  private final Validator chain; // all rules, for new registrations
  private final Validator promotionChain; // skips the window: student queued inside it
  private final List<WaitlistListener> listeners = new CopyOnWriteArrayList<>();

  public RegistrationService(Validator.WindowValidator window) {
    Validator prereq = new Validator.PrerequisiteValidator();
    window.setNext(prereq)
        .setNext(new Validator.CreditCapValidator())
        .setNext(new Validator.TimeClashValidator());
    this.chain = window;
    this.promotionChain = prereq;
  }

  public void addListener(WaitlistListener listener) {
    listeners.add(listener);
  }

  public RegistrationResult register(Student student, Section section) {
    synchronized (student) {
      List<Reason> errors = new ArrayList<>();
      chain.validate(student, section, errors);
      if (!errors.isEmpty()) {
        return new RegistrationResult.Rejected(errors);
      }
      synchronized (section) {
        if (section.enrolled.contains(student.id) || section.waitlist.contains(student)) {
          return new RegistrationResult.Rejected(List.of(Reason.ALREADY_REGISTERED));
        }
        // a free seat goes to the queue first, not to whoever arrives now
        if (section.hasSeat() && section.waitlist.isEmpty()) {
          enrol(student, section);
          return new RegistrationResult.Enrolled();
        }
        section.waitlist.addLast(student);
        return new RegistrationResult.Waitlisted(section.waitlist.size());
      }
    }
  }

  /** Drop a seat or a waitlist place. A freed seat is offered to the waitlist. */
  public boolean drop(Student student, Section section) {
    synchronized (student) {
      synchronized (section) {
        if (section.waitlist.remove(student)) {
          return true;
        }
        if (!section.enrolled.remove(student.id)) {
          return false;
        }
        student.enrolledSections.remove(section);
      }
    }
    promoteFromWaitlist(section); // no locks held here
    return true;
  }

  /**
   * Take students off the front of the queue until one still passes the rules (edge case: a
   * waitlisted student may now have a clash or be over the cap).
   */
  private void promoteFromWaitlist(Section section) {
    while (true) {
      Student next;
      synchronized (section) {
        if (!section.hasSeat() || section.waitlist.isEmpty()) {
          return;
        }
        next = section.waitlist.pollFirst();
      }
      synchronized (next) { // student lock taken with no section lock held
        List<Reason> errors = new ArrayList<>();
        promotionChain.validate(next, section, errors);
        if (!errors.isEmpty()) {
          listeners.forEach(l -> l.onSkipped(next, section, errors));
          continue;
        }
        synchronized (section) {
          if (!section.hasSeat()) { // seat taken meanwhile: keep their place
            section.waitlist.addFirst(next);
            return;
          }
          enrol(next, section);
        }
      }
      listeners.forEach(l -> l.onPromoted(next, section));
    }
  }

  /** End of term: the section moves from the timetable to the transcript. */
  public void recordGrade(Student student, Section section, String grade) {
    synchronized (student) {
      if (student.enrolledSections.remove(section)) {
        student.transcript.add(
            new Student.TranscriptEntry(
                section.course.id(), section.id, grade, section.course.credits()));
      }
    }
  }

  /** This term's classes, ordered by day then start time. */
  public List<TimeSlotEntry> viewTimetable(Student student) {
    synchronized (student) {
      List<TimeSlotEntry> entries = new ArrayList<>();
      for (Section s : student.enrolledSections) {
        for (TimeSlot slot : s.slots) {
          entries.add(new TimeSlotEntry(s.course.id(), s.id, slot));
        }
      }
      entries.sort(
          Comparator.comparing((TimeSlotEntry e) -> e.slot().day())
              .thenComparing(e -> e.slot().start()));
      return entries;
    }
  }

  public record TimeSlotEntry(String courseId, String sectionId, TimeSlot slot) {}

  public List<Student.TranscriptEntry> viewTranscript(Student student) {
    synchronized (student) {
      return List.copyOf(student.transcript);
    }
  }

  private void enrol(Student student, Section section) {
    section.enrolled.add(student.id);
    student.enrolledSections.add(section);
  }
}

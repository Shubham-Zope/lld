package com.shubham.courseRegistrationSystem.entities;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

/**
 * Read model: a weekly timetable built on demand from sections. Never stored, so it cannot go out
 * of sync with enrollments.
 */
public class TimeTable {
  private static final List<String> DAY_ORDER =
      List.of("MON", "TUE", "WED", "THU", "FRI", "SAT", "SUN");

  private final List<TimeTableEntry> entries;

  private TimeTable(Collection<Section> sections) {
    List<TimeTableEntry> built = new ArrayList<>();
    for (Section section : sections) {
      Course course = section.getCourse();
      for (TimeSlot slot : section.getTimeSlots()) {
        built.add(
            new TimeTableEntry(
                course.getCourseId(), course.getName(), section.getSectionId(), slot));
      }
    }
    built.sort(
        Comparator.comparingInt((TimeTableEntry e) -> dayIndex(e.slot().getDay()))
            .thenComparing(e -> e.slot().getStartTime()));
    this.entries = List.copyOf(built);
  }

  /**
   * A student's timetable for one term: only ENROLLED enrollments (waitlisted ones hold no seat).
   *
   * @param enrollments the student's enrollments (any term, any status; filtered here)
   * @param sectionLookup finds a section by ID, e.g. {@code sectionRepository::getById}
   */
  public static TimeTable forStudent(
      String studentId,
      String termId,
      Collection<Enrollment> enrollments,
      Function<String, Section> sectionLookup) {
    Objects.requireNonNull(studentId, "studentId");
    Objects.requireNonNull(termId, "termId");
    Objects.requireNonNull(sectionLookup, "sectionLookup");
    List<Section> sections = new ArrayList<>();
    for (Enrollment enrollment : enrollments) {
      if (enrollment.getStudentId().equals(studentId)
          && enrollment.getTermId().equals(termId)
          && enrollment.isEnrolled()) {
        Section section = sectionLookup.apply(enrollment.getSectionId());
        if (section == null) {
          throw new IllegalStateException(
              "Section "
                  + enrollment.getSectionId()
                  + " of enrollment "
                  + enrollment.getEnrollmentId()
                  + " not found");
        }
        sections.add(section);
      }
    }
    return new TimeTable(sections);
  }

  /** An instructor's timetable, from the sections they teach. */
  public static TimeTable forInstructor(Instructor instructor) {
    return new TimeTable(Objects.requireNonNull(instructor, "instructor").getTeaches());
  }

  /** True if any slot of the section overlaps a slot already on this timetable. */
  public boolean clashesWith(Section section) {
    for (TimeSlot candidate : section.getTimeSlots()) {
      for (TimeTableEntry entry : entries) {
        if (entry.slot().overlapSlots(candidate)) {
          return true;
        }
      }
    }
    return false;
  }

  /** All meetings, ordered by day then start time. */
  public List<TimeTableEntry> getEntries() {
    return entries;
  }

  /** Meetings grouped by day (Mon..Sun), each day ordered by start time. */
  public Map<String, List<TimeTableEntry>> byDay() {
    Map<String, List<TimeTableEntry>> grouped = new LinkedHashMap<>();
    for (TimeTableEntry entry : entries) {
      grouped.computeIfAbsent(dayKey(entry.slot().getDay()), d -> new ArrayList<>()).add(entry);
    }
    grouped.replaceAll((day, list) -> Collections.unmodifiableList(list));
    return Collections.unmodifiableMap(grouped);
  }

  public boolean isEmpty() {
    return entries.isEmpty();
  }

  /** "MON", "Mon" and "Monday" all become "MON"; unknown names are kept upper-cased. */
  private static String dayKey(String day) {
    String upper = day.toUpperCase();
    String prefix = upper.length() >= 3 ? upper.substring(0, 3) : upper;
    return DAY_ORDER.contains(prefix) ? prefix : upper;
  }

  /** Position in Mon..Sun; unknown names sort after Sunday. */
  private static int dayIndex(String day) {
    int index = DAY_ORDER.indexOf(dayKey(day));
    return index >= 0 ? index : DAY_ORDER.size();
  }
}

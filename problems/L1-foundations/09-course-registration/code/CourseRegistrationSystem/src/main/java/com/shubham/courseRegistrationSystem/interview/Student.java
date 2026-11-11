package com.shubham.courseRegistrationSystem.interview;

import java.util.ArrayList;
import java.util.List;

/** Enrolled sections and transcript are only touched while holding this student's monitor. */
public class Student {
  static final int MAX_CREDITS = 24;

  /** One finished course on the report card. */
  public record TranscriptEntry(String courseId, String sectionId, String grade, int credits) {}

  final String id;
  final int year;
  final List<Section> enrolledSections = new ArrayList<>(); // this term = timetable
  final List<TranscriptEntry> transcript = new ArrayList<>(); // past terms

  public Student(String id, int year) {
    this.id = id;
    this.year = year;
  }

  int creditsThisTerm() {
    return enrolledSections.stream().mapToInt(s -> s.course.credits()).sum();
  }

  boolean hasCompleted(String courseId) {
    return transcript.stream()
        .anyMatch(e -> e.courseId().equals(courseId) && !e.grade().equals("F"));
  }
}

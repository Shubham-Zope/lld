package com.shubham.courseRegistrationSystem.entities;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;

public class Course {
  private static final int MAX_CREDITS_PER_TERM = 24;

  private final String courseId;
  private final String name;
  private final int credits;
  private final List<String> prerequisiteIds;
  private final List<Section> sections;

  /**
   * Sections are not passed here: a Section needs its Course to be built, so sections are attached
   * afterwards with {@link #addSection(Section)}.
   */
  public Course(String courseId, String name, int credits, List<String> prerequisiteIds) {
    if (credits <= 0 || credits > MAX_CREDITS_PER_TERM) {
      throw new IllegalArgumentException("Credits must be between 1 and " + MAX_CREDITS_PER_TERM);
    }

    this.courseId = Objects.requireNonNull(courseId, "courseId");
    this.name = Objects.requireNonNull(name, "name");
    this.credits = credits;
    this.prerequisiteIds = List.copyOf(Objects.requireNonNull(prerequisiteIds, "prerequisiteIds"));
    if (this.prerequisiteIds.contains(courseId)) {
      throw new IllegalArgumentException("Course " + courseId + " cannot be its own prerequisite");
    }
    this.sections = new CopyOnWriteArrayList<>();
  }

  public void addSection(Section section) {
    Objects.requireNonNull(section, "section");
    if (section.getCourse() != this) {
      throw new IllegalArgumentException(
          "Section " + section.getSectionId() + " does not belong to course " + courseId);
    }
    if (getSection(section.getSectionId()).isPresent()) {
      throw new IllegalArgumentException(
          "Section " + section.getSectionId() + " already exists in course " + courseId);
    }
    sections.add(section);
  }

  public Optional<Section> getSection(String sectionId) {
    return sections.stream().filter(s -> s.getSectionId().equals(sectionId)).findFirst();
  }

  public boolean hasPrerequisites() {
    return !prerequisiteIds.isEmpty();
  }

  public String getCourseId() {
    return this.courseId;
  }

  public String getName() {
    return this.name;
  }

  public int getCredits() {
    return this.credits;
  }

  public List<String> getPrerequisiteIds() {
    return this.prerequisiteIds;
  }

  public List<Section> getSections() {
    return Collections.unmodifiableList(this.sections);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Course)) return false;
    return courseId.equals(((Course) o).courseId);
  }

  @Override
  public int hashCode() {
    return courseId.hashCode();
  }
}

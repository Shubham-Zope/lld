package com.shubham.courseRegistrationSystem.entities;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class Instructor {
  private final String instructorId;
  private final String instructorName;
  private final List<Section> teaches;

  public Instructor(String instructorId, String instructorName) {
    this.instructorId = instructorId;
    this.instructorName = instructorName;
    this.teaches = new CopyOnWriteArrayList<>();
  }

  public String getInstructorId() {
    return instructorId;
  }

  public String getInstructorName() {
    return instructorName;
  }

  public void assignSection(Section section) {
    teaches.add(section);
  }

  public List<Section> getTeaches() {
    return teaches;
  }
}

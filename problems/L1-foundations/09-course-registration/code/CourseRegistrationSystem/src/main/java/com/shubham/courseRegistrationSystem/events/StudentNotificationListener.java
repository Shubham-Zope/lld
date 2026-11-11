package com.shubham.courseRegistrationSystem.events;

import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Tells students what happened to them. Messages go to a sink (console by default; email / in-app
 * in a real system).
 */
public class StudentNotificationListener implements EnrollmentEventListener {
  private final Consumer<String> sink;

  public StudentNotificationListener() {
    this(System.out::println);
  }

  public StudentNotificationListener(Consumer<String> sink) {
    this.sink = Objects.requireNonNull(sink, "sink");
  }

  @Override
  public void onEvent(EnrollmentEvent event) {
    switch (event) {
      case EnrollmentEvent.StudentEnrolled e ->
          send(e.studentId(), "you are enrolled in section " + e.sectionId());
      case EnrollmentEvent.StudentWaitlisted e ->
          send(e.studentId(), "section " + e.sectionId() + " is full, you are #" + e.position());
      case EnrollmentEvent.StudentPromoted e ->
          send(e.studentId(), "a seat freed up, you are now enrolled in " + e.sectionId());
      case EnrollmentEvent.WaitlistSkipped e ->
          send(
              e.studentId(),
              "removed from the waitlist of " + e.sectionId() + " because " + e.reasons());
      case EnrollmentEvent.StudentDropped e ->
          send(e.studentId(), e.status() + " from section " + e.sectionId());
      case EnrollmentEvent.WaitlistUpdated e -> {
        List<String> order = e.order();
        for (int i = 0; i < order.size(); i++) {
          send(order.get(i), "you are now #" + (i + 1) + " on the waitlist of " + e.sectionId());
        }
      }
      case EnrollmentEvent.SeatFreed e -> {
        // internal event, nobody to tell
      }
    }
  }

  private void send(String studentId, String message) {
    sink.accept("[to " + studentId + "] " + message);
  }
}

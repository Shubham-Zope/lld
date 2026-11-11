package com.shubham.courseRegistrationSystem.events;

/** Observer. */
public interface EnrollmentEventListener {
  void onEvent(EnrollmentEvent event);
}

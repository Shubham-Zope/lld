package com.shubham.courseRegistrationSystem.events;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Subject of the Observer pattern. Delivers each event to every listener synchronously, in
 * subscription order. A listener that throws does not stop the others or fail the registration.
 */
public class EnrollmentEventPublisher {
  private final List<EnrollmentEventListener> listeners = new CopyOnWriteArrayList<>();

  public void subscribe(EnrollmentEventListener listener) {
    listeners.add(Objects.requireNonNull(listener, "listener"));
  }

  public void unsubscribe(EnrollmentEventListener listener) {
    listeners.remove(listener);
  }

  public void publish(EnrollmentEvent event) {
    Objects.requireNonNull(event, "event");
    for (EnrollmentEventListener listener : listeners) {
      try {
        listener.onEvent(event);
      } catch (RuntimeException e) {
        System.err.println(
            "Listener " + listener.getClass().getSimpleName() + " failed on " + event + ": " + e);
      }
    }
  }

  public void publishAll(List<EnrollmentEvent> events) {
    events.forEach(this::publish);
  }
}

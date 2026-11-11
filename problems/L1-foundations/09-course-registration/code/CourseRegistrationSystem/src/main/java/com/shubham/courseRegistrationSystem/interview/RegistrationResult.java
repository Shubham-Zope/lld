package com.shubham.courseRegistrationSystem.interview;

import java.util.List;

public sealed interface RegistrationResult {
  enum Reason {
    WINDOW_CLOSED,
    PREREQUISITE_MISSING,
    CREDIT_CAP_EXCEEDED,
    TIME_CLASH,
    ALREADY_REGISTERED
  }

  record Enrolled() implements RegistrationResult {}

  record Waitlisted(int position) implements RegistrationResult {}

  record Rejected(List<Reason> reasons) implements RegistrationResult {}
}

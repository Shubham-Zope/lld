package com.shubham.courseRegistrationSystem.interview;

import com.shubham.courseRegistrationSystem.interview.RegistrationResult.Reason;
import java.util.List;

/** Observer: told when a waitlisted student is auto-enrolled or skipped. */
public interface WaitlistListener {
  void onPromoted(Student student, Section section);

  void onSkipped(Student student, Section section, List<Reason> reasons);
}

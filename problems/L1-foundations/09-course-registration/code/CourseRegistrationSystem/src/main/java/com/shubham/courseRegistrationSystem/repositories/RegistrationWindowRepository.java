package com.shubham.courseRegistrationSystem.repositories;

import com.shubham.courseRegistrationSystem.entities.RegistrationWindow;
import java.util.Optional;

public interface RegistrationWindowRepository {
  /** Replaces any window already saved for the same term and year. */
  void save(RegistrationWindow window);

  Optional<RegistrationWindow> find(String termId, int studentYear);
}

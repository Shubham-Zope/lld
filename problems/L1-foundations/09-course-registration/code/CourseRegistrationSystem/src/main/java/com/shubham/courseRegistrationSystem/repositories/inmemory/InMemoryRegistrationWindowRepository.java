package com.shubham.courseRegistrationSystem.repositories.inmemory;

import com.shubham.courseRegistrationSystem.entities.RegistrationWindow;
import com.shubham.courseRegistrationSystem.repositories.RegistrationWindowRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryRegistrationWindowRepository implements RegistrationWindowRepository {
  private final Map<String, RegistrationWindow> windows = new ConcurrentHashMap<>();

  @Override
  public void save(RegistrationWindow window) {
    windows.put(key(window.getTermId(), window.getStudentYear()), window);
  }

  @Override
  public Optional<RegistrationWindow> find(String termId, int studentYear) {
    return Optional.ofNullable(windows.get(key(termId, studentYear)));
  }

  private static String key(String termId, int studentYear) {
    return termId + "#" + studentYear;
  }
}

package com.shubham.courseRegistrationSystem.repositories.inmemory;

import com.shubham.courseRegistrationSystem.entities.Enrollment;
import com.shubham.courseRegistrationSystem.repositories.EnrollmentRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Indexed by student and by section so lookups do not scan every enrollment (20k students). The
 * indexes hold the same Enrollment objects, so status changes need no re-indexing.
 */
public class InMemoryEnrollmentRepository implements EnrollmentRepository {
  private final Map<String, Enrollment> byId = new ConcurrentHashMap<>();
  private final Map<String, List<Enrollment>> byStudent = new ConcurrentHashMap<>();
  private final Map<String, List<Enrollment>> bySection = new ConcurrentHashMap<>();

  @Override
  public void save(Enrollment enrollment) {
    if (byId.putIfAbsent(enrollment.getEnrollmentId(), enrollment) != null) {
      return; // already stored; it is updated in place
    }
    byStudent
        .computeIfAbsent(enrollment.getStudentId(), k -> new CopyOnWriteArrayList<>())
        .add(enrollment);
    bySection
        .computeIfAbsent(enrollment.getSectionId(), k -> new CopyOnWriteArrayList<>())
        .add(enrollment);
  }

  @Override
  public Optional<Enrollment> findById(String enrollmentId) {
    return Optional.ofNullable(byId.get(enrollmentId));
  }

  @Override
  public List<Enrollment> findByStudent(String studentId) {
    return List.copyOf(byStudent.getOrDefault(studentId, List.of()));
  }

  @Override
  public List<Enrollment> findByStudentAndTerm(String studentId, String termId) {
    return byStudent.getOrDefault(studentId, List.of()).stream()
        .filter(e -> e.getTermId().equals(termId))
        .toList();
  }

  @Override
  public Optional<Enrollment> findActive(String studentId, String sectionId) {
    return byStudent.getOrDefault(studentId, List.of()).stream()
        .filter(e -> e.getSectionId().equals(sectionId) && e.isActive())
        .findFirst();
  }

  @Override
  public List<Enrollment> findBySection(String sectionId) {
    return List.copyOf(bySection.getOrDefault(sectionId, List.of()));
  }
}

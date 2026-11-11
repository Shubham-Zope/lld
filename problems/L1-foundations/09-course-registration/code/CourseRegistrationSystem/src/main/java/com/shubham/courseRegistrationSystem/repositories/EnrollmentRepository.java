package com.shubham.courseRegistrationSystem.repositories;

import com.shubham.courseRegistrationSystem.entities.Enrollment;
import java.util.List;
import java.util.Optional;

/**
 * Enrollments are mutable (their status changes in place), so saving once is enough; later status
 * changes are seen by every query.
 */
public interface EnrollmentRepository {
  void save(Enrollment enrollment);

  Optional<Enrollment> findById(String enrollmentId);

  /** All of the student's enrollments, any term, any status. */
  List<Enrollment> findByStudent(String studentId);

  List<Enrollment> findByStudentAndTerm(String studentId, String termId);

  /** The student's ENROLLED or WAITLISTED enrollment in the section, if any. */
  Optional<Enrollment> findActive(String studentId, String sectionId);

  List<Enrollment> findBySection(String sectionId);
}

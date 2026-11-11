package com.shubham.courseRegistrationSystem.repositories;

import com.shubham.courseRegistrationSystem.entities.Student;
import java.util.Optional;

public interface StudentRepository {
  void save(Student student);

  Optional<Student> findById(String studentId);
}

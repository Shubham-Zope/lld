package com.shubham.courseRegistrationSystem.repositories.inmemory;

import com.shubham.courseRegistrationSystem.entities.Student;
import com.shubham.courseRegistrationSystem.repositories.StudentRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryStudentRepository implements StudentRepository {
  private final Map<String, Student> students = new ConcurrentHashMap<>();

  @Override
  public void save(Student student) {
    students.put(student.getStudentId(), student);
  }

  @Override
  public Optional<Student> findById(String studentId) {
    return Optional.ofNullable(students.get(studentId));
  }
}

package com.shubham.courseRegistrationSystem.repositories;

import com.shubham.courseRegistrationSystem.entities.Course;
import java.util.Collection;
import java.util.Optional;

public interface CourseRepository {
  void save(Course course);

  Optional<Course> findById(String courseId);

  Collection<Course> findAll();
}

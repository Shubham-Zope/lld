package com.shubham.courseRegistrationSystem.repositories.inmemory;

import com.shubham.courseRegistrationSystem.entities.Course;
import com.shubham.courseRegistrationSystem.repositories.CourseRepository;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCourseRepository implements CourseRepository {
  private final Map<String, Course> courses = new ConcurrentHashMap<>();

  @Override
  public void save(Course course) {
    courses.put(course.getCourseId(), course);
  }

  @Override
  public Optional<Course> findById(String courseId) {
    return Optional.ofNullable(courses.get(courseId));
  }

  @Override
  public Collection<Course> findAll() {
    return List.copyOf(courses.values());
  }
}

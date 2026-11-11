package com.shubham.courseRegistrationSystem.repositories;

import com.shubham.courseRegistrationSystem.entities.Section;
import java.util.List;
import java.util.Optional;

public interface SectionRepository {
  void save(Section section);

  Optional<Section> findById(String sectionId);

  List<Section> findByCourseAndTerm(String courseId, String termId);
}

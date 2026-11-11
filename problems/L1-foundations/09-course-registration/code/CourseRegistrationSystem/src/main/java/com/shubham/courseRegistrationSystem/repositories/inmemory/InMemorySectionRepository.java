package com.shubham.courseRegistrationSystem.repositories.inmemory;

import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.repositories.SectionRepository;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemorySectionRepository implements SectionRepository {
  private final Map<String, Section> sections = new ConcurrentHashMap<>();

  @Override
  public void save(Section section) {
    sections.put(section.getSectionId(), section);
  }

  @Override
  public Optional<Section> findById(String sectionId) {
    return Optional.ofNullable(sections.get(sectionId));
  }

  @Override
  public List<Section> findByCourseAndTerm(String courseId, String termId) {
    return sections.values().stream()
        .filter(
            s -> s.getCourse().getCourseId().equals(courseId) && s.getTermId().equals(termId))
        .toList();
  }
}

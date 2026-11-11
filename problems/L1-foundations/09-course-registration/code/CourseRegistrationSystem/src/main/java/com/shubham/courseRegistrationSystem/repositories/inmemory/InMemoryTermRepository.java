package com.shubham.courseRegistrationSystem.repositories.inmemory;

import com.shubham.courseRegistrationSystem.entities.Term;
import com.shubham.courseRegistrationSystem.repositories.TermRepository;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryTermRepository implements TermRepository {
  private final Map<String, Term> terms = new ConcurrentHashMap<>();

  @Override
  public void save(Term term) {
    terms.put(term.getTermId(), term);
  }

  @Override
  public Optional<Term> findById(String termId) {
    return Optional.ofNullable(terms.get(termId));
  }
}

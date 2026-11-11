package com.shubham.courseRegistrationSystem.repositories;

import com.shubham.courseRegistrationSystem.entities.Term;
import java.util.Optional;

public interface TermRepository {
  void save(Term term);

  Optional<Term> findById(String termId);
}

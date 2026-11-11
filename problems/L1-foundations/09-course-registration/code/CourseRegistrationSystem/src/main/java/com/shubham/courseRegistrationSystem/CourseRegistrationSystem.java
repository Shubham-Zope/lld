package com.shubham.courseRegistrationSystem;

import com.shubham.courseRegistrationSystem.events.EnrollmentEventPublisher;
import com.shubham.courseRegistrationSystem.events.StudentNotificationListener;
import com.shubham.courseRegistrationSystem.policies.DropPolicy;
import com.shubham.courseRegistrationSystem.policies.PrerequisitePolicy;
import com.shubham.courseRegistrationSystem.policies.RejectAfterDeadlineDropPolicy;
import com.shubham.courseRegistrationSystem.policies.StrictPrerequisitePolicy;
import com.shubham.courseRegistrationSystem.repositories.CourseRepository;
import com.shubham.courseRegistrationSystem.repositories.EnrollmentRepository;
import com.shubham.courseRegistrationSystem.repositories.RegistrationWindowRepository;
import com.shubham.courseRegistrationSystem.repositories.SectionRepository;
import com.shubham.courseRegistrationSystem.repositories.StudentRepository;
import com.shubham.courseRegistrationSystem.repositories.TermRepository;
import com.shubham.courseRegistrationSystem.repositories.inmemory.InMemoryCourseRepository;
import com.shubham.courseRegistrationSystem.repositories.inmemory.InMemoryEnrollmentRepository;
import com.shubham.courseRegistrationSystem.repositories.inmemory.InMemoryRegistrationWindowRepository;
import com.shubham.courseRegistrationSystem.repositories.inmemory.InMemorySectionRepository;
import com.shubham.courseRegistrationSystem.repositories.inmemory.InMemoryStudentRepository;
import com.shubham.courseRegistrationSystem.repositories.inmemory.InMemoryTermRepository;
import com.shubham.courseRegistrationSystem.services.CatalogService;
import com.shubham.courseRegistrationSystem.services.RegistrationContextLoader;
import com.shubham.courseRegistrationSystem.services.RegistrationService;
import com.shubham.courseRegistrationSystem.services.StudentLocks;
import com.shubham.courseRegistrationSystem.services.WaitListPromoter;
import com.shubham.courseRegistrationSystem.validators.CreditCapValidator;
import com.shubham.courseRegistrationSystem.validators.DuplicateRegistrationValidator;
import com.shubham.courseRegistrationSystem.validators.PrerequisitesValidator;
import com.shubham.courseRegistrationSystem.validators.RegistrationValidator;
import com.shubham.courseRegistrationSystem.validators.RegistrationWindowValidator;
import com.shubham.courseRegistrationSystem.validators.TimeClashValidator;
import java.time.Clock;
import java.util.Objects;
import java.util.function.Consumer;

/** Wires the whole system together with in-memory repositories. */
public class CourseRegistrationSystem {
  private final TermRepository terms = new InMemoryTermRepository();
  private final RegistrationWindowRepository windows = new InMemoryRegistrationWindowRepository();
  private final StudentRepository students = new InMemoryStudentRepository();
  private final CourseRepository courses = new InMemoryCourseRepository();
  private final SectionRepository sections = new InMemorySectionRepository();
  private final EnrollmentRepository enrollments = new InMemoryEnrollmentRepository();

  private final EnrollmentEventPublisher publisher = new EnrollmentEventPublisher();
  private final CatalogService catalogService;
  private final RegistrationService registrationService;
  private final WaitListPromoter waitListPromoter;

  /** Defaults: system clock, strict prerequisites, no drops after the deadline, console notices. */
  public CourseRegistrationSystem() {
    this(
        Clock.systemDefaultZone(),
        new StrictPrerequisitePolicy(),
        new RejectAfterDeadlineDropPolicy(),
        System.out::println);
  }

  public CourseRegistrationSystem(
      Clock clock,
      PrerequisitePolicy prerequisitePolicy,
      DropPolicy dropPolicy,
      Consumer<String> notificationSink) {
    Objects.requireNonNull(clock, "clock");

    // Chain of Responsibility, cheapest rule first. To add a rule: subclass RegistrationValidator
    // and link it here.
    RegistrationValidator validatorChain = new RegistrationWindowValidator(windows);
    validatorChain
        .setNext(new DuplicateRegistrationValidator())
        .setNext(new PrerequisitesValidator(prerequisitePolicy))
        .setNext(new CreditCapValidator())
        .setNext(new TimeClashValidator());

    StudentLocks studentLocks = new StudentLocks();
    RegistrationContextLoader contextLoader =
        new RegistrationContextLoader(courses, sections, terms, enrollments);

    this.catalogService = new CatalogService(terms, windows, students, courses, sections);
    this.registrationService =
        new RegistrationService(
            students,
            courses,
            sections,
            terms,
            enrollments,
            contextLoader,
            validatorChain,
            prerequisitePolicy,
            dropPolicy,
            studentLocks,
            publisher,
            clock);
    this.waitListPromoter =
        new WaitListPromoter(
            sections,
            students,
            enrollments,
            contextLoader,
            validatorChain,
            studentLocks,
            publisher,
            clock);

    // Observers
    publisher.subscribe(waitListPromoter);
    publisher.subscribe(new StudentNotificationListener(notificationSink));
  }

  public CatalogService catalog() {
    return catalogService;
  }

  public RegistrationService registration() {
    return registrationService;
  }

  public EnrollmentEventPublisher events() {
    return publisher;
  }

  public EnrollmentRepository enrollments() {
    return enrollments;
  }
}

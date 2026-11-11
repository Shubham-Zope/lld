package com.shubham.courseRegistrationSystem;

import static com.shubham.courseRegistrationSystem.TestSystem.FALL;
import static com.shubham.courseRegistrationSystem.TestSystem.SPRING;
import static com.shubham.courseRegistrationSystem.TestSystem.slot;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.shubham.courseRegistrationSystem.entities.Course;
import com.shubham.courseRegistrationSystem.entities.Section;
import com.shubham.courseRegistrationSystem.entities.TimeSlot;
import com.shubham.courseRegistrationSystem.enums.EnrollmentStatus;
import com.shubham.courseRegistrationSystem.enums.RejectionReason;
import com.shubham.courseRegistrationSystem.policies.AllowInProgressPrerequisitePolicy;
import com.shubham.courseRegistrationSystem.policies.RejectAfterDeadlineDropPolicy;
import com.shubham.courseRegistrationSystem.policies.SeniorityWaitListPolicy;
import com.shubham.courseRegistrationSystem.policies.StrictPrerequisitePolicy;
import com.shubham.courseRegistrationSystem.policies.WithdrawAfterDeadlineDropPolicy;
import com.shubham.courseRegistrationSystem.results.DropResult;
import com.shubham.courseRegistrationSystem.results.RegistrationResult;
import com.shubham.courseRegistrationSystem.services.RegistrationService;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import org.junit.jupiter.api.Test;

class RegistrationServiceTest {

  private static RegistrationResult.Enrolled assertEnrolled(RegistrationResult result) {
    return assertInstanceOf(RegistrationResult.Enrolled.class, result, result.toString());
  }

  private static RegistrationResult.Waitlisted assertWaitlisted(RegistrationResult result) {
    return assertInstanceOf(RegistrationResult.Waitlisted.class, result, result.toString());
  }

  private static List<RejectionReason> assertRejected(RegistrationResult result) {
    return assertInstanceOf(RegistrationResult.Rejected.class, result, result.toString()).reasons();
  }

  private static EnrollmentStatus statusOf(TestSystem t, String enrollmentId) {
    return t.system.enrollments().findById(enrollmentId).orElseThrow().getStatus();
  }

  // ---- basic register ----

  @Test
  void enrollsWhenSeatIsFree() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    Section section = t.section("A", t.course("C1", 4), FALL, 2);

    assertEnrolled(t.registration().register("s1", "A"));
    assertEquals(1, section.getSeatsTaken());
  }

  @Test
  void waitlistsWithPositionWhenFull() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.student("s2", 2);
    t.student("s3", 2);
    t.section("A", t.course("C1", 4), FALL, 1);
    RegistrationService r = t.registration();

    assertEnrolled(r.register("s1", "A"));
    assertEquals(1, assertWaitlisted(r.register("s2", "A")).position());
    assertEquals(2, assertWaitlisted(r.register("s3", "A")).position());
  }

  @Test
  void unknownStudentOrSectionIsRejected() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    assertEquals(
        List.of(RejectionReason.STUDENT_NOT_FOUND), assertRejected(t.registration().register("x", "A")));
    assertEquals(
        List.of(RejectionReason.SECTION_NOT_FOUND),
        assertRejected(t.registration().register("s1", "nope")));
  }

  // ---- validation chain ----

  @Test
  void rejectionListsEveryFailedRule() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.course("BASE", 4);
    t.section("A", t.course("ADV", 4, "BASE"), FALL, 5);
    t.clock.set(LocalDateTime.of(2026, 8, 11, 9, 0)); // window closed Aug 10

    assertEquals(
        List.of(RejectionReason.WINDOW_CLOSED, RejectionReason.PREREQUISITE_MISSING),
        assertRejected(t.registration().register("s1", "A")));
  }

  @Test
  void seniorsCanRegisterBeforeOthers() {
    TestSystem t = new TestSystem();
    t.student("senior", 4);
    t.student("junior", 2);
    t.section("A", t.course("C1", 4), FALL, 5);
    t.clock.set(LocalDateTime.of(2026, 7, 22, 9, 0)); // only the 4th-year window is open

    assertEnrolled(t.registration().register("senior", "A"));
    assertEquals(
        List.of(RejectionReason.WINDOW_CLOSED), assertRejected(t.registration().register("junior", "A")));
  }

  @Test
  void creditCapOf24IsEnforced() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    for (int i = 0; i < 7; i++) {
      t.section("S" + i, t.course("C" + i, 4), FALL, 5);
    }
    for (int i = 0; i < 6; i++) {
      assertEnrolled(t.registration().register("s1", "S" + i)); // 6 x 4 = 24
    }
    assertEquals(
        List.of(RejectionReason.CREDIT_CAP_EXCEEDED),
        assertRejected(t.registration().register("s1", "S6")));
  }

  @Test
  void waitlistedCoursesDoNotCountTowardTheCap() {
    TestSystem t = new TestSystem();
    t.student("other", 2);
    t.student("s1", 2);
    t.section("FULL", t.course("BIG", 20), FALL, 1);
    t.section("SMALL", t.course("SMALL", 4), FALL, 1);
    assertEnrolled(t.registration().register("other", "FULL"));

    assertWaitlisted(t.registration().register("s1", "FULL")); // 20 credits, only waiting
    assertEnrolled(t.registration().register("s1", "SMALL")); // 4 enrolled, fine
  }

  @Test
  void timeClashIsRejected() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.section("A", t.course("C1", 4), FALL, 5, slot("MON", 9));
    t.section(
        "B",
        t.course("C2", 4),
        FALL,
        5,
        new TimeSlot("Monday", LocalTime.of(9, 30), LocalTime.of(10, 30)));

    assertEnrolled(t.registration().register("s1", "A"));
    assertEquals(
        List.of(RejectionReason.TIME_CLASH), assertRejected(t.registration().register("s1", "B")));
  }

  @Test
  void sameTimeInAnotherTermIsNotAClash() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.section("A", t.course("C1", 4), FALL, 5, slot("MON", 9));
    t.section("B", t.course("C2", 4), SPRING, 5, slot("MON", 9));

    assertEnrolled(t.registration().register("s1", "A"));
    assertEnrolled(t.registration().register("s1", "B"));
  }

  @Test
  void duplicateRegistrationsAreRejected() {
    TestSystem t = new TestSystem();
    t.student("other", 2);
    t.student("s1", 2);
    Course c1 = t.course("C1", 4);
    t.section("A", c1, FALL, 5);
    t.section("B", c1, FALL, 5);
    Course c2 = t.course("C2", 4);
    t.section("FULL", c2, FALL, 1);
    t.section("FULL2", c2, FALL, 5);
    RegistrationService r = t.registration();

    assertEnrolled(r.register("s1", "A"));
    assertEquals(List.of(RejectionReason.ALREADY_ENROLLED), assertRejected(r.register("s1", "B")));

    assertEnrolled(r.register("other", "FULL"));
    assertWaitlisted(r.register("s1", "FULL"));
    assertEquals(
        List.of(RejectionReason.ALREADY_WAITLISTED), assertRejected(r.register("s1", "FULL2")));
  }

  @Test
  void passedCourseCannotBeTakenAgain() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    Course c1 = t.course("C1", 4);
    t.section("FALL-A", c1, FALL, 5);
    t.section("SPRING-A", c1, SPRING, 5);
    String id = assertEnrolled(t.registration().register("s1", "FALL-A")).enrollmentId();
    t.registration().recordGrade(id, "A", true);

    assertEquals(
        List.of(RejectionReason.ALREADY_COMPLETED),
        assertRejected(t.registration().register("s1", "SPRING-A")));
  }

  // ---- prerequisites ----

  @Test
  void strictPolicyNeedsACompletedPrerequisite() {
    TestSystem t = new TestSystem(new StrictPrerequisitePolicy(), new RejectAfterDeadlineDropPolicy());
    t.student("s1", 2);
    t.section("BASE-F", t.course("BASE", 4), FALL, 5);
    t.section("ADV-S", t.course("ADV", 4, "BASE"), SPRING, 5);
    String base = assertEnrolled(t.registration().register("s1", "BASE-F")).enrollmentId();

    assertEquals(
        List.of(RejectionReason.PREREQUISITE_MISSING),
        assertRejected(t.registration().register("s1", "ADV-S")));

    t.registration().recordGrade(base, "B", true);
    assertEnrolled(t.registration().register("s1", "ADV-S"));
  }

  @Test
  void inProgressPrerequisiteCountsForNextTermOnly() {
    TestSystem t =
        new TestSystem(new AllowInProgressPrerequisitePolicy(), new RejectAfterDeadlineDropPolicy());
    t.student("s1", 2);
    Course base = t.course("BASE", 4);
    Course adv = t.course("ADV", 4, "BASE");
    t.section("BASE-FALL", base, FALL, 5);
    t.section("ADV-FALL", adv, FALL, 5);
    t.section("ADV-SPRING", adv, SPRING, 5);

    assertEnrolled(t.registration().register("s1", "BASE-FALL"));
    assertEquals(
        List.of(RejectionReason.PREREQUISITE_MISSING),
        assertRejected(t.registration().register("s1", "ADV-FALL")),
        "same term as the prerequisite");
    assertEnrolled(t.registration().register("s1", "ADV-SPRING"));
  }

  @Test
  void failingAnInProgressPrerequisiteDropsTheConditionalEnrollment() {
    TestSystem t =
        new TestSystem(new AllowInProgressPrerequisitePolicy(), new RejectAfterDeadlineDropPolicy());
    t.student("s1", 2);
    t.student("s2", 2);
    t.section("BASE-FALL", t.course("BASE", 4), FALL, 5);
    Section advSpring = t.section("ADV-SPRING", t.course("ADV", 4, "BASE"), SPRING, 1);
    RegistrationService r = t.registration();

    String base = assertEnrolled(r.register("s1", "BASE-FALL")).enrollmentId();
    String adv = assertEnrolled(r.register("s1", "ADV-SPRING")).enrollmentId();
    // s2 already passed BASE (recorded), and waits for ADV
    String s2Base = assertEnrolled(r.register("s2", "BASE-FALL")).enrollmentId();
    r.recordGrade(s2Base, "A", true);
    String s2Adv = assertWaitlisted(r.register("s2", "ADV-SPRING")).enrollmentId();

    r.recordGrade(base, "F", false);

    assertEquals(EnrollmentStatus.DROPPED, statusOf(t, adv));
    assertEquals(EnrollmentStatus.ENROLLED, statusOf(t, s2Adv), "freed seat went to the waitlist");
    assertEquals(1, advSpring.getSeatsTaken());
  }

  // ---- drop + waitlist ----

  @Test
  void dropFreesSeatAndHeadOfWaitlistIsAutoEnrolled() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.student("s2", 2);
    t.student("s3", 2);
    Section a = t.section("A", t.course("C1", 4), FALL, 1);
    RegistrationService r = t.registration();
    r.register("s1", "A");
    String s2 = assertWaitlisted(r.register("s2", "A")).enrollmentId();
    String s3 = assertWaitlisted(r.register("s3", "A")).enrollmentId();

    assertInstanceOf(DropResult.Dropped.class, r.drop("s1", "A"));

    assertEquals(EnrollmentStatus.ENROLLED, statusOf(t, s2));
    assertEquals(EnrollmentStatus.WAITLISTED, statusOf(t, s3));
    assertEquals(1, a.getWaitList().positionOf("s3").getAsInt());
    assertEquals(1, a.getSeatsTaken());
    assertTrue(t.notifications.contains("[to s2] a seat freed up, you are now enrolled in A"));
    assertTrue(t.notifications.contains("[to s3] you are now #1 on the waitlist of A"));
  }

  @Test
  void waitlistedStudentWhoNowHasAClashIsSkipped() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.student("s2", 2);
    t.student("s3", 2);
    t.section("A", t.course("C1", 4), FALL, 1, slot("TUE", 9));
    t.section("B", t.course("C2", 4), FALL, 5, slot("TUE", 9));
    RegistrationService r = t.registration();
    r.register("s1", "A");
    String s2 = assertWaitlisted(r.register("s2", "A")).enrollmentId();
    String s3 = assertWaitlisted(r.register("s3", "A")).enrollmentId();
    assertEnrolled(r.register("s2", "B")); // s2 now clashes with A

    r.drop("s1", "A");

    assertEquals(EnrollmentStatus.DROPPED, statusOf(t, s2));
    assertEquals(EnrollmentStatus.ENROLLED, statusOf(t, s3));
    assertTrue(
        t.notifications.contains("[to s2] removed from the waitlist of A because [TIME_CLASH]"));
  }

  @Test
  void seatGoesBackWhenNobodyOnTheWaitlistIsEligible() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.student("s2", 2);
    t.student("s3", 2);
    Section a = t.section("A", t.course("C1", 4), FALL, 1, slot("TUE", 9));
    t.section("B", t.course("C2", 4), FALL, 5, slot("TUE", 9));
    RegistrationService r = t.registration();
    r.register("s1", "A");
    r.register("s2", "A");
    r.register("s2", "B");

    r.drop("s1", "A");

    assertEquals(0, a.getSeatsTaken());
    assertTrue(a.getWaitList().isEmpty());
    assertEnrolled(r.register("s3", "A"));
  }

  @Test
  void droppingWhileWaitlistedShiftsOthersUp() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.student("s2", 2);
    t.student("s3", 2);
    Section a = t.section("A", t.course("C1", 4), FALL, 1);
    RegistrationService r = t.registration();
    r.register("s1", "A");
    r.register("s2", "A");
    r.register("s3", "A");

    DropResult result = r.drop("s2", "A");

    assertEquals(EnrollmentStatus.DROPPED, assertInstanceOf(DropResult.Dropped.class, result).status());
    assertEquals(1, a.getWaitList().positionOf("s3").getAsInt());
    assertEquals(1, a.getSeatsTaken(), "s1 keeps the seat");
  }

  @Test
  void dropWithoutEnrollmentIsRejected() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    t.section("A", t.course("C1", 4), FALL, 1);
    assertEquals(
        new DropResult.Rejected(RejectionReason.NOT_ENROLLED), t.registration().drop("s1", "A"));
  }

  @Test
  void dropAfterDeadlineIsRejectedByDefault() {
    TestSystem t = new TestSystem();
    t.student("s1", 2);
    Section a = t.section("A", t.course("C1", 4), FALL, 1);
    String id = assertEnrolled(t.registration().register("s1", "A")).enrollmentId();
    t.clock.set(LocalDateTime.of(2026, 8, 21, 9, 0));

    assertEquals(
        new DropResult.Rejected(RejectionReason.DROP_DEADLINE_PASSED),
        t.registration().drop("s1", "A"));
    assertEquals(EnrollmentStatus.ENROLLED, statusOf(t, id));
    assertEquals(1, a.getSeatsTaken());
  }

  @Test
  void dropAfterDeadlineBecomesWithdrawalUnderWithdrawPolicy() {
    TestSystem t =
        new TestSystem(new StrictPrerequisitePolicy(), new WithdrawAfterDeadlineDropPolicy());
    t.student("s1", 2);
    t.student("s2", 2);
    Section a = t.section("A", t.course("C1", 4), FALL, 1);
    t.registration().register("s1", "A");
    String s2 = assertWaitlisted(t.registration().register("s2", "A")).enrollmentId();
    t.clock.set(LocalDateTime.of(2026, 8, 21, 9, 0));

    DropResult result = t.registration().drop("s1", "A");

    assertEquals(
        EnrollmentStatus.WITHDRAWN, assertInstanceOf(DropResult.Dropped.class, result).status());
    assertEquals(EnrollmentStatus.ENROLLED, statusOf(t, s2), "seat still freed for the waitlist");
    assertEquals(1, a.getSeatsTaken());
    assertEquals(
        EnrollmentStatus.WITHDRAWN,
        t.registration().viewTranscript("s1").getEntries().get(0).status());
  }

  @Test
  void seniorityWaitlistPutsSeniorsFirst() {
    TestSystem t = new TestSystem();
    t.student("s1", 1);
    t.student("junior", 2);
    t.student("senior", 4);
    Section a = t.section("A", t.course("C1", 4), FALL, 1, new SeniorityWaitListPolicy());
    RegistrationService r = t.registration();
    r.register("s1", "A");

    assertEquals(1, assertWaitlisted(r.register("junior", "A")).position());
    assertEquals(1, assertWaitlisted(r.register("senior", "A")).position());
    assertEquals(List.of("senior", "junior"), a.getWaitList().snapshot());
    assertTrue(t.notifications.contains("[to junior] you are now #2 on the waitlist of A"));
  }

  // ---- views ----

  @Test
  void timetableAndTranscriptReflectEnrollments() {
    TestSystem t = new TestSystem();
    t.student("other", 2);
    t.student("s1", 2);
    t.section("A", t.course("C1", 4), FALL, 5, slot("WED", 9));
    t.section("B", t.course("C2", 3), FALL, 5, slot("MON", 9));
    t.section("FULL", t.course("C3", 3), FALL, 1, slot("FRI", 9));
    RegistrationService r = t.registration();
    String a = assertEnrolled(r.register("s1", "A")).enrollmentId();
    r.register("s1", "B");
    r.register("other", "FULL");
    r.register("s1", "FULL");

    assertEquals(
        List.of("MON", "WED"), List.copyOf(r.viewTimetable("s1", FALL).byDay().keySet()));
    r.recordGrade(a, "A", true);
    var transcript = r.viewTranscript("s1");
    assertEquals(2, transcript.getEntries().size(), "waitlisted course not on transcript");
    assertEquals(4, transcript.totalCreditsEarned());
    assertFalse(transcript.hasCompleted("C2"));
  }
}

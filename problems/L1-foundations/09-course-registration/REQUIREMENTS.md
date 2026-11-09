# 09 · Course Registration System — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 40 min
> **Patterns usually relevant:** Chain of Responsibility (validators), Observer, Repository

## Problem statement

Design a university course registration system. Students register for course sections each term subject to prerequisites, seat limits, schedule clashes and credit limits. Full sections offer a waitlist. Admins manage courses and sections.

## Functional requirements

- [ ] **FR1** Courses with prerequisites and credits; sections with instructor, time slots, capacity
- [ ] **FR2** Register/drop a section
- [ ] **FR3** Validate prerequisites, credit cap, time clash
- [ ] **FR4** Waitlist with auto-enrol when a seat frees
- [ ] **FR5** Registration windows by student year
- [ ] **FR6** View timetable and transcript

## Non-functional requirements

- [ ] **NFR1** No over-enrolment under concurrent registration
- [ ] **NFR2** Validation rules pluggable

## Constraints & assumptions

- Max 24 credits per term
- Up to 20k students

## Edge cases to handle

- [ ] **EC1** Prerequisite in progress this term
- [ ] **EC2** Drop after deadline
- [ ] **EC3** Waitlisted student now has a clash
- [ ] **EC4** Two students taking the last seat

## Expected behaviour

- register returns Enrolled, Waitlisted(position) or Rejected(reasons)

## Design concepts this problem exercises

- Validation chain
- Waitlist queues
- Atomic seat counters

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Waitlist? | Yes, FIFO. |
| Schedule clash check? | Yes. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Co-requisites
- Priority registration for seniors

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

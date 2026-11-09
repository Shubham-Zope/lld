# 13 · Online Exam / Quiz System — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 40 min
> **Patterns usually relevant:** Strategy, Factory, Template method, Builder

## Problem statement

Design an online examination system: question banks, question types (MCQ single/multi, true/false, short answer, coding), exams assembled from banks with rules, timed attempts, auto-grading where possible and results.

## Functional requirements

- [ ] **FR1** Question bank with tags and difficulty
- [ ] **FR2** Question types with type-specific grading
- [ ] **FR3** Exam: sections, duration, random selection rules, negative marking
- [ ] **FR4** Candidate attempt with autosave and resume
- [ ] **FR5** Auto-submit on timeout
- [ ] **FR6** Results and per-section analytics

## Non-functional requirements

- [ ] **NFR1** New question type without changing exam code
- [ ] **NFR2** Attempts never lose answers

## Constraints & assumptions

- Up to 5000 concurrent candidates per exam

## Edge cases to handle

- [ ] **EC1** Connection drop mid-attempt
- [ ] **EC2** Question edited after exam published
- [ ] **EC3** Partial credit on multi-select

## Expected behaviour

- submit returns score breakdown; manual-grade questions pending

## Design concepts this problem exercises

- Polymorphic questions
- Snapshotting exam content
- Strategy for grading

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Negative marking? | Configurable per exam. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Proctoring events
- Adaptive difficulty

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

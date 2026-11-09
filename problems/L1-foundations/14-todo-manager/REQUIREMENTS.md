# 14 · Task Manager with Reminders — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 35 min
> **Patterns usually relevant:** Composite, Specification, Observer, Strategy

## Problem statement

Design a personal task manager: tasks with priority, due date, tags, subtasks and recurrence; lists and filters; reminders that fire before due time.

## Functional requirements

- [ ] **FR1** Create/update/complete tasks
- [ ] **FR2** Subtasks; parent completes when all subtasks complete (optional rule)
- [ ] **FR3** Recurring tasks (daily, weekly, custom)
- [ ] **FR4** Filters: by tag, priority, due range; saved smart lists
- [ ] **FR5** Reminders N minutes before due
- [ ] **FR6** Sort by multiple keys

## Non-functional requirements

- [ ] **NFR1** Recurrence rules extensible
- [ ] **NFR2** Time-zone correct

## Constraints & assumptions

- 10k tasks per user

## Edge cases to handle

- [ ] **EC1** Completing a recurring task early
- [ ] **EC2** Due date in the past
- [ ] **EC3** Reminder when device offline

## Expected behaviour

- Completing a recurring task creates the next occurrence

## Design concepts this problem exercises

- Composite (subtasks)
- Specification filters
- Recurrence calculation

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Recurrence? | Yes. |
| Sharing? | Not in v1. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Shared lists
- Natural-language input

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

# 06 · Employee Directory & Org Chart — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Core · **Difficulty:** Easy · **Interview time-box:** 35 min
> **Patterns usually relevant:** Composite, Visitor (reports over the tree), Iterator

## Problem statement

Design an employee directory that models an organisation's reporting hierarchy, departments and roles. HR can add, move and remove employees; anyone can query reporting chains, direct reports, headcount per department and the lowest common manager of two employees.

## Functional requirements

- [ ] **FR1** Add employee with manager, department, role, location
- [ ] **FR2** Move an employee (and optionally their subtree) to a new manager
- [ ] **FR3** Remove an employee and reassign their reports
- [ ] **FR4** Query direct reports, full subtree, and chain up to the CEO
- [ ] **FR5** Lowest common manager of two employees
- [ ] **FR6** Headcount by department or location

## Non-functional requirements

- [ ] **NFR1** Queries efficient for 100k employees
- [ ] **NFR2** Hierarchy must never contain a cycle
- [ ] **NFR3** Read-heavy; optimise for reads

## Constraints & assumptions

- Single CEO
- Depth up to 20 levels

## Edge cases to handle

- [ ] **EC1** Moving a manager under their own report (cycle)
- [ ] **EC2** Removing the CEO
- [ ] **EC3** Employee with no department
- [ ] **EC4** Duplicate employee id

## Expected behaviour

- move() rejects cycles with a clear error
- queries reflect writes immediately

## Design concepts this problem exercises

- Tree modelling
- Composite
- Invariants
- Caching derived data

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Can someone have two managers? | Not in v1. |
| What happens to reports when a manager leaves? | They move to the leaver's manager by default. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Dotted-line (matrix) reporting
- Historical org chart as of a date

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

# 15 · Gym Membership & Class Booking — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 40 min
> **Patterns usually relevant:** Strategy, Observer, State

## Problem statement

Design a system for a gym chain: membership plans, check-ins, group classes with capacity and booking, trainers, and freeze/cancel rules.

## Functional requirements

- [ ] **FR1** Plans (monthly, quarterly, annual) with access rules per branch
- [ ] **FR2** Check-in validating active membership
- [ ] **FR3** Classes with trainer, slot, capacity; book and cancel
- [ ] **FR4** Waitlist for full classes
- [ ] **FR5** Freeze membership for N days
- [ ] **FR6** Penalties for no-shows

## Non-functional requirements

- [ ] **NFR1** Access rules configurable
- [ ] **NFR2** No class overbooking

## Constraints & assumptions

- 50 branches

## Edge cases to handle

- [ ] **EC1** Expired membership tries to book
- [ ] **EC2** Trainer cancels class
- [ ] **EC3** Freeze overlapping a booked class

## Expected behaviour

- book returns Booked, Waitlisted or Rejected(reason)

## Design concepts this problem exercises

- Validity periods
- Capacity management
- Strategy for plans

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Multi-branch access? | Depends on plan. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Personal training packages
- Family plans

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

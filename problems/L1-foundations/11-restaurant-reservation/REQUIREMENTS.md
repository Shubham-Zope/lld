# 11 · Restaurant Table Reservation — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 35 min
> **Patterns usually relevant:** Strategy, Observer, State

## Problem statement

Design a reservation system for a restaurant with tables of different sizes. Guests book a time slot for a party size; the system assigns suitable tables (possibly combining adjacent ones), handles walk-ins, waitlist and no-shows.

## Functional requirements

- [ ] **FR1** Tables with capacity and combinable groups
- [ ] **FR2** Book by date, time slot and party size
- [ ] **FR3** Assign smallest suitable table or table combination
- [ ] **FR4** Walk-in queue with estimated wait
- [ ] **FR5** Mark seated, completed, no-show
- [ ] **FR6** Cancel and modify

## Non-functional requirements

- [ ] **NFR1** Allocation strategy pluggable
- [ ] **NFR2** No double booking of a table slot

## Constraints & assumptions

- Slots of 90 minutes
- Up to 60 tables

## Edge cases to handle

- [ ] **EC1** Party larger than any combination
- [ ] **EC2** Booking overlaps a slot that runs late
- [ ] **EC3** No-show after 15 minutes

## Expected behaviour

- book returns Booking with tables or alternative times

## Design concepts this problem exercises

- Interval allocation
- Strategy
- Waitlist

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Combine tables? | Yes, only predefined combinations. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Deposits for large parties
- Multiple outlets

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

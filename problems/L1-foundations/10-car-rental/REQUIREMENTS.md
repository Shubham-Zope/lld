# 10 · Car Rental System — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 45 min
> **Patterns usually relevant:** Strategy, Decorator, State, Factory

## Problem statement

Design a car rental system across branches. Customers search vehicles by type, branch and dates, reserve, pick up, and return possibly at another branch. Pricing depends on vehicle type, duration and add-ons. Late returns and damage are charged.

## Functional requirements

- [ ] **FR1** Branches, vehicles (type, plate, status), customers with licence
- [ ] **FR2** Search availability by branch, type, date range
- [ ] **FR3** Reserve, modify, cancel
- [ ] **FR4** Pick-up and return (one-way allowed with fee)
- [ ] **FR5** Pricing: daily rate × days + add-ons (GPS, insurance, child seat)
- [ ] **FR6** Late fee and damage charges on return

## Non-functional requirements

- [ ] **NFR1** No double reservation of a vehicle
- [ ] **NFR2** Pricing extensible

## Constraints & assumptions

- Up to 200 branches

## Edge cases to handle

- [ ] **EC1** Return earlier than booked
- [ ] **EC2** Vehicle unavailable at pick-up (upgrade)
- [ ] **EC3** Licence expires before trip end

## Expected behaviour

- reserve returns Reservation or NoAvailability

## Design concepts this problem exercises

- Availability over date ranges
- Strategy pricing
- Decorator add-ons

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Reserve a specific car or a type? | A type; car assigned at pick-up. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Loyalty tiers
- Fleet maintenance scheduling

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

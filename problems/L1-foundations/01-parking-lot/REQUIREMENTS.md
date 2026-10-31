# 01 · Parking Lot — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Core · **Difficulty:** Easy · **Interview time-box:** 45 min
> **Patterns usually relevant:** Strategy, Factory, Observer, Singleton

## Problem statement

Design the software for a multi-floor parking lot. Vehicles of different sizes enter through entry gates, are assigned a compatible free spot, receive a ticket, and pay on exit based on time parked. Operators need to see live availability per floor and spot type. The lot should be able to add new vehicle types, spot types and pricing schemes without rewriting the core.

## Functional requirements

- [ ] **FR1** Support multiple floors, each with spots of types Compact, Regular, Large and EV-charging
- [ ] **FR2** Support vehicle types Motorcycle, Car, Van/Truck, with a defined compatibility matrix (e.g. a car fits Regular or Large)
- [ ] **FR3** Issue a ticket on entry with ticket id, spot, vehicle number and entry time
- [ ] **FR4** Allocate the nearest compatible free spot using a pluggable allocation strategy
- [ ] **FR5** Compute fee on exit using a pluggable pricing strategy (hourly, flat first-hour, weekend rates)
- [ ] **FR6** Accept payment (cash, card, UPI) and free the spot once paid
- [ ] **FR7** Show live free-spot counts per floor and per spot type on a display board
- [ ] **FR8** Reject entry with a clear reason when the lot is full for that vehicle type

## Non-functional requirements

- [ ] **NFR1** Spot allocation must be O(log n) or better per request
- [ ] **NFR2** Two entry gates must never assign the same spot (thread-safe allocation)
- [ ] **NFR3** Adding a new vehicle or spot type should not modify existing allocation code (OCP)
- [ ] **NFR4** Core logic must be unit-testable with an injected clock

## Constraints & assumptions

- Up to 10 floors, 500 spots per floor
- Up to 5 entry and 5 exit gates operating concurrently
- In-memory storage is acceptable; persistence behind an interface
- Fees in INR, rounded up to the next started hour by default

## Edge cases to handle

- [ ] **EC1** Vehicle tries to exit with a lost or invalid ticket
- [ ] **EC2** Same vehicle number tries to enter twice
- [ ] **EC3** Payment fails after the fee is computed
- [ ] **EC4** A spot goes out of service while occupied
- [ ] **EC5** Two gates request the last free spot at the same instant
- [ ] **EC6** Vehicle parks for under one minute or across midnight / multiple days

## Expected behaviour

- park(vehicle) returns a Ticket or throws LotFullException(vehicleType)
- unpark(ticketId, payment) returns a Receipt with duration, fee and payment status; spot becomes free only after successful payment
- displayBoard.snapshot() returns counts per floor × spot type that reflect every completed park/unpark

## Design concepts this problem exercises

- Entity modelling
- Enums vs class hierarchy
- Composition
- Strategy pattern
- Singleton (only if justified)
- Thread-safe allocation
- Observer for display boards

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Is it multi-floor? | Yes, multiple floors; each floor has a mix of spot types. |
| How is pricing calculated? | Hourly by vehicle type; first hour flat. Assume pricing rules will change often. |
| Do we need reservations? | Not in scope for v1, but don't make it impossible. |
| Which spot should be assigned? | Nearest to the entry gate by default; the business may want other strategies later. |
| Do we handle payments ourselves? | Model a Payment abstraction; actual gateway integration is out of scope. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Add reservations made in advance from a mobile app
- Support EV charging billed per kWh in addition to parking time
- Make it work across 50 lots in a city with a central dashboard

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

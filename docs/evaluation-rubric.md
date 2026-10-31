# Evaluation rubric (100 points)

The practice tool scores against this same rubric.

| Dimension | Weight | 0–40% | 40–75% | 75–100% |
|---|---|---|---|---|
| Requirements & clarification | 10 | Started without questions | Asked basic scope questions | Identified actors, invariants, scale, change axes |
| Correctness | 20 | Core flows broken | Happy path works | Happy path and listed edge cases handled |
| OOP fundamentals | 15 | Anaemic or god classes | Mostly cohesive | Clear ownership, encapsulation, value objects |
| SOLID | 15 | Several violations | Minor violations | Each principle visibly applied where it matters |
| Design patterns | 10 | None or misapplied | Some appropriate | Chosen for stated reasons, no over-engineering |
| Extensibility | 10 | Follow-ups need rewrites | Some local changes | Follow-ups are additive |
| Maintainability & code quality | 10 | Hard to read | Readable | Naming, small methods, tests, errors typed |
| Concurrency & scalability | 10 | Ignored | Mentioned | Correct strategy for shared state, scaling path explained |

## Level expectations

| Level | Pass mark | What raises the bar |
|---|---|---|
| L1–L2 | 65 | Clean modelling and one or two well-placed patterns |
| L3 | 70 | Layering, concurrency on shared resources |
| L4 | 75 | Provable thread safety, failure modes |
| L5 | 80 | Invariants under retries and partial failure, auditability |
| L6 | 80 | API ergonomics, compatibility, extension points for other teams |

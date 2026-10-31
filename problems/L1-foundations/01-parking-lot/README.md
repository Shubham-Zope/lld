# 01 · Parking Lot

`L1 · Foundations` · `Core` · `Easy` · `45 min` · Patterns: `Strategy`, `Factory`, `Observer`, `Singleton`

Design the software for a multi-floor parking lot. Vehicles of different sizes enter through entry gates, are assigned a compatible free spot, receive a ticket, and pay on exit based on time parked. Operators need to see live availability per floor and spot type. The lot should be able to add new vehicle types, spot types and pricing schemes without rewriting the core.

## Files

| File | What goes in it |
|---|---|
| [REQUIREMENTS.md](./REQUIREMENTS.md) | Full spec: FR, NFR, constraints, edge cases, expected behaviour |
| [DESIGN.md](./DESIGN.md) | Your class diagram, patterns, SOLID check, trade-offs |
| `src/main/java/com/lld/parkinglot/` | Implementation |
| `src/test/java/com/lld/parkinglot/` | JUnit 5 tests, one per FR and edge case at minimum |

## Status

- [ ] Attempt 1 — timed (45 min), no references
- [ ] Self-review against `docs/evaluation-rubric.md`
- [ ] Mock interview in the practice tool — score: __/100
- [ ] Refactor based on feedback
- [ ] Revision (after 7 days) — redo design only in 15 min
- [ ] Revision (after 30 days)

## My key learnings

-

## Run

```bash
./gradlew :parking-lot:test
```

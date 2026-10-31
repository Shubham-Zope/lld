# 04 · Library Management System

`L1 · Foundations` · `Core` · `Easy` · `45 min` · Patterns: `Observer`, `Strategy`, `Repository`

Design a system for a public library to manage its catalogue, members, borrowing, returns, reservations and fines. A title can have many physical copies. Members borrow copies, can reserve titles that are all checked out, and are fined for late returns. Librarians manage the catalogue.

## Files

| File | What goes in it |
|---|---|
| [REQUIREMENTS.md](./REQUIREMENTS.md) | Full spec: FR, NFR, constraints, edge cases, expected behaviour |
| [DESIGN.md](./DESIGN.md) | Your class diagram, patterns, SOLID check, trade-offs |
| `src/main/java/com/lld/librarymanagement/` | Implementation |
| `src/test/java/com/lld/librarymanagement/` | JUnit 5 tests, one per FR and edge case at minimum |

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
./gradlew :library-management:test
```

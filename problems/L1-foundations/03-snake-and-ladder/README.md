# 03 · Snake and Ladder

`L1 · Foundations` · `Core` · `Easy` · `40 min` · Patterns: `Strategy`, `Builder (board)`, `Observer (turn events)`

Design a Snake and Ladder game supporting multiple players, a configurable board with snakes and ladders, and configurable dice. Players roll in turn, move, and are moved by snakes or ladders; the first to reach the last cell exactly wins. The rules vary between families, so rules must be pluggable.

## Files

| File | What goes in it |
|---|---|
| [REQUIREMENTS.md](./REQUIREMENTS.md) | Full spec: FR, NFR, constraints, edge cases, expected behaviour |
| [DESIGN.md](./DESIGN.md) | Your class diagram, patterns, SOLID check, trade-offs |
| `src/main/java/com/lld/snakeandladder/` | Implementation |
| `src/test/java/com/lld/snakeandladder/` | JUnit 5 tests, one per FR and edge case at minimum |

## Status

- [ ] Attempt 1 — timed (40 min), no references
- [ ] Self-review against `docs/evaluation-rubric.md`
- [ ] Mock interview in the practice tool — score: __/100
- [ ] Refactor based on feedback
- [ ] Revision (after 7 days) — redo design only in 15 min
- [ ] Revision (after 30 days)

## My key learnings

-

## Run

```bash
./gradlew :snake-and-ladder:test
```

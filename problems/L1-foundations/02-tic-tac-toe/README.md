# 02 · Tic-Tac-Toe (N×N)

`L1 · Foundations` · `Core` · `Easy` · `40 min` · Patterns: `Strategy (bot difficulty, win rules)`, `Command (undo)`, `Factory (players)`

Design a Tic-Tac-Toe game engine that works on an N×N board for two or more players. The engine validates moves, detects wins and draws efficiently, and supports both human and bot players. It should be easy to plug in a new win rule or a new player type.

## Files

| File | What goes in it |
|---|---|
| [REQUIREMENTS.md](./REQUIREMENTS.md) | Full spec: FR, NFR, constraints, edge cases, expected behaviour |
| [DESIGN.md](./DESIGN.md) | Your class diagram, patterns, SOLID check, trade-offs |
| `src/main/java/com/lld/tictactoe/` | Implementation |
| `src/test/java/com/lld/tictactoe/` | JUnit 5 tests, one per FR and edge case at minimum |

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
./gradlew :tic-tac-toe:test
```

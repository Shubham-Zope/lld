# 07 · Snake Game — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 35 min
> **Patterns usually relevant:** Strategy (boundary rules), Observer (score/UI), State (Running, Paused, Over)

## Problem statement

Design the engine for the classic Snake game on a W×H grid. The snake moves one cell per tick in its current direction, grows when it eats food, and dies on hitting a wall or itself. Food appears at random free cells. The engine must be UI-agnostic and deterministic under test.

## Functional requirements

- [ ] **FR1** Grid of configurable width and height
- [ ] **FR2** Snake moves one cell per tick; direction changes via input
- [ ] **FR3** Eating food grows the snake by one and increases score
- [ ] **FR4** Food spawns on a random free cell
- [ ] **FR5** Game over on wall or self collision
- [ ] **FR6** Optional wrap-around mode instead of walls

## Non-functional requirements

- [ ] **NFR1** Each tick O(1) (deque body + occupied-cell set)
- [ ] **NFR2** Randomness injectable for tests
- [ ] **NFR3** Engine has no rendering code

## Constraints & assumptions

- Grid up to 1000×1000
- One input processed per tick

## Edge cases to handle

- [ ] **EC1** Reverse direction input (180° turn)
- [ ] **EC2** Moving into the cell the tail is leaving this tick
- [ ] **EC3** No free cell left for food (win)
- [ ] **EC4** Two inputs between ticks

## Expected behaviour

- tick() returns TickResult: MOVED, ATE, GAME_OVER(reason)
- changeDirection ignores direct reversal

## Design concepts this problem exercises

- Deque modelling
- Separating state from rendering
- Deterministic randomness

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Walls or wrap? | Walls by default; wrap is a mode. |
| What if the snake fills the board? | Player wins. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Obstacles and levels
- Two-player snakes on one grid
- Speed increases with score

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

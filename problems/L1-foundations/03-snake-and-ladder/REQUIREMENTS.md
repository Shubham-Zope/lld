# 03 · Snake and Ladder — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Core · **Difficulty:** Easy · **Interview time-box:** 40 min
> **Patterns usually relevant:** Strategy, Builder (board), Observer (turn events)

## Problem statement

Design a Snake and Ladder game supporting multiple players, a configurable board with snakes and ladders, and configurable dice. Players roll in turn, move, and are moved by snakes or ladders; the first to reach the last cell exactly wins. The rules vary between families, so rules must be pluggable.

## Functional requirements

- [ ] **FR1** Board of configurable size (default 100) with configurable snakes (head → tail) and ladders (start → end)
- [ ] **FR2** Validate board config: no cycles, no snake/ladder on start or end cell, no cell with two jumps
- [ ] **FR3** One or more dice; configurable faces
- [ ] **FR4** Players move in round-robin; first to land exactly on the last cell wins
- [ ] **FR5** Support rule variants: extra turn on six, three sixes cancels, overshoot bounce-back
- [ ] **FR6** Game continues for remaining players to rank 2nd, 3rd, etc. (optional mode)

## Non-functional requirements

- [ ] **NFR1** Deterministic tests via an injectable dice
- [ ] **NFR2** New rule variants added without editing the game loop

## Constraints & assumptions

- 2-8 players
- Board size 25-400
- Single process

## Edge cases to handle

- [ ] **EC1** Roll that overshoots the final cell
- [ ] **EC2** Snake tail landing on a ladder start (chains)
- [ ] **EC3** All players finished
- [ ] **EC4** Invalid board with snake head on cell 100

## Expected behaviour

- play() returns ranking of players
- Each turn emits a TurnResult with roll, from, to and any jump taken

## Design concepts this problem exercises

- Configuration validation
- Strategy for rules
- Dependency injection for randomness

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Exact landing needed to win? | Yes by default, but some variants allow overshoot. |
| Fixed snakes and ladders? | Configurable per game. |
| How many dice? | One by default, configurable. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Add power-ups on cells
- Save and resume a game
- Online multiplayer with reconnection

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

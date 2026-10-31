# 02 · Tic-Tac-Toe (N×N) — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Core · **Difficulty:** Easy · **Interview time-box:** 40 min
> **Patterns usually relevant:** Strategy (bot difficulty, win rules), Command (undo), Factory (players)

## Problem statement

Design a Tic-Tac-Toe game engine that works on an N×N board for two or more players. The engine validates moves, detects wins and draws efficiently, and supports both human and bot players. It should be easy to plug in a new win rule or a new player type.

## Functional requirements

- [ ] **FR1** Create a game with board size N (N ≥ 3) and K players with unique symbols
- [ ] **FR2** Players take turns in a fixed order; reject moves out of turn
- [ ] **FR3** Validate a move: inside the board, cell empty, game not over
- [ ] **FR4** Detect a winner after each move (full row, column or either diagonal)
- [ ] **FR5** Detect a draw when the board is full with no winner
- [ ] **FR6** Support human players and bot players behind one Player interface
- [ ] **FR7** Support undo of the last move

## Non-functional requirements

- [ ] **NFR1** Win detection in O(1) per move, not O(N²)
- [ ] **NFR2** Engine independent of any UI (console, web, API)
- [ ] **NFR3** Easily testable with scripted moves

## Constraints & assumptions

- 3 ≤ N ≤ 100
- 2 ≤ K ≤ N-1 players
- Single process, single game per engine instance

## Edge cases to handle

- [ ] **EC1** Move on an occupied cell
- [ ] **EC2** Move after the game has ended
- [ ] **EC3** Undo when no moves were made
- [ ] **EC4** Undo after a winning move
- [ ] **EC5** Bot with no legal move
- [ ] **EC6** Duplicate player symbols

## Expected behaviour

- makeMove(player, row, col) returns GameState IN_PROGRESS, WON(player) or DRAW
- Invalid moves throw InvalidMoveException with a reason and do not advance the turn
- undo() restores the previous board, the turn and the win counters

## Design concepts this problem exercises

- Separation of game logic and IO
- Polymorphism (Player)
- O(1) win check using row/col/diagonal counters
- Command pattern for undo

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| Board size fixed at 3? | No, N×N where N is configurable. |
| How many players? | Two by default but design for more. |
| Do we need a UI? | No UI; expose an engine API. A console driver is enough to demo. |
| Bots? | Yes, at least a random bot; smarter bots later. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Win condition becomes K-in-a-row instead of full row
- Add spectators who get notified on each move
- Replay a finished game

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

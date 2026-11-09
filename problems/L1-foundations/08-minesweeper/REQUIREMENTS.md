# 08 · Minesweeper — Requirements

> **Level:** L1 Foundations (Junior / SDE1) · **Track:** Extended · **Difficulty:** Easy · **Interview time-box:** 40 min
> **Patterns usually relevant:** State, Factory (presets), Observer

## Problem statement

Design a Minesweeper engine: a board with hidden mines, numbered cells, reveal and flag actions, flood-fill reveal of empty regions, and win/lose detection. The first click must never be a mine.

## Functional requirements

- [ ] **FR1** Board R×C with M mines
- [ ] **FR2** Mines placed after the first click, excluding that cell (and optionally its neighbours)
- [ ] **FR3** reveal(r,c): mine loses; number shows count; zero flood-fills
- [ ] **FR4** flag/unflag cells
- [ ] **FR5** Chord: reveal neighbours when flag count matches number
- [ ] **FR6** Win when all non-mine cells revealed
- [ ] **FR7** Difficulty presets

## Non-functional requirements

- [ ] **NFR1** Flood fill iterative (no stack overflow on large boards)
- [ ] **NFR2** Seeded RNG for tests

## Constraints & assumptions

- Up to 500×500
- M < R×C − 9

## Edge cases to handle

- [ ] **EC1** Reveal a flagged cell
- [ ] **EC2** Chord with wrong flags (lose)
- [ ] **EC3** Reveal after game over
- [ ] **EC4** Mine count too high

## Expected behaviour

- reveal returns the list of cells newly revealed and game state

## Design concepts this problem exercises

- BFS flood fill
- Lazy initialisation
- Cell state enum

## Clarifying questions an interviewer expects (with typical answers)

Try to come up with these yourself before reading the table.

<details>
<summary>Reveal</summary>

| Question | Typical interviewer answer |
|---|---|
| First click safe? | Yes, always. |
| Chording? | Yes. |

</details>

## Follow-up extensions (asked in the last 10 minutes)

- Undo last reveal (practice mode)
- Hint system

For each follow-up, write in `DESIGN.md` which classes change and which stay untouched. If an extension forces you to edit many existing classes, your design is not open for extension.

# Tic-Tac-Toe Game Engine (N×N)

## Problem Statement

Design and implement a Tic-Tac-Toe game engine that supports an N×N board and multiple players (K ≥ 2).

The engine should handle turn management, move validation, winner detection, draw detection, and undo functionality. It should support both human and bot players while keeping the game logic independent of the user interface.

The design should also be extensible enough to support new player types and winning strategies without significantly modifying the existing implementation.

## Requirements

### Functional Requirements

- Create a game with board size N (N ≥ 3).
- Support K players with unique symbols (2 ≤ K ≤ N-1).
- Players take turns in a predefined order.
- Validate moves before updating the board.
- Reject moves outside the board boundaries.
- Reject moves on already occupied cells.
- Reject moves from players who are not currently playing.
- Detect a winner when a player completes an entire row, column, or diagonal.
- Detect a draw when the board is full and there is no winner.
- Support human and bot players.
- Support undo of the last move.

### Non-Functional Requirements

- Winner detection should take O(1) time per move.
- Game logic should remain independent of UI and IO.
- New player types and winning strategies should be easy to introduce.
- The implementation should be easy to test using scripted moves.

---

## Design Approach & Thought Process

### 1. Identify the Core Entities

The first step is identifying the major entities and defining their responsibilities.

| Entity | Responsibility |
|---|---|
| Game | Coordinates the game lifecycle, turns, moves and game state. |
| Board | Maintains the N×N grid and validates cell occupancy. |
| Player | Defines the common contract for all players. |
| HumanPlayer | Represents a human participant. |
| BotPlayer | Represents an automated participant. |
| TrackerService | Maintains player-specific row, column and diagonal counters. |
| WinningStrategy | Defines the winning condition. |
| PlayerFactory | Creates the appropriate player implementation. |
| MoveCommand | Encapsulates move execution and undo. |

### 2. Separate Game Logic from Player Types

The game engine should not depend on whether a move originates from a human or a bot.

Both player types implement the same Player interface.

- Human players receive their moves from an external driver.
- Bot players generate their moves using a BotMoveStrategy.
- Game validates and executes every move.

This ensures that the game engine remains independent of input mechanisms.

### 3. Efficient Winner Detection

A straightforward implementation would scan the entire row, column and diagonals after every move.

However, this would require O(N) time per move.

To optimize winner detection, maintain counters for each player.

For every player, maintain:

- Row counters: `int[N]`
- Column counters: `int[N]`
- Main diagonal counter
- Anti-diagonal counter

These counters are maintained independently for each player using a TrackerService.

#### Example

Consider a 4×4 board:

```text
X X X X
O O . .
. . . .
. . . .
```

Player X has completed the first row.

The tracker for X will contain:

```text
rows = [4, 0, 0, 0]
cols = [1, 1, 1, 1]
mainDiagonal = 1
antiDiagonal = 1
```

Since the first row counter equals N, X wins.

#### Counter Update Rules

For a move at `(row, col)`:

1. Increment the corresponding row counter.
2. Increment the corresponding column counter.
3. If `row == col`, increment the main diagonal counter.
4. If `row + col == N - 1`, increment the anti-diagonal counter.
5. Check whether any applicable counter equals N.

Each operation takes constant time.

**Time Complexity: O(1) per move for winner detection.**

### 4. Multiple Player Tracking

Each player maintains an independent tracker.

For example, with three players X, O and A, their counters are stored separately.

This prevents one player's moves from contributing to another player's winning condition.

An opponent occupying a cell naturally prevents the current player from completing that line.

### 5. Undo Functionality

Undo is implemented using the Command pattern.

Every valid move is represented by a MoveCommand.

The command stores:

- Player who made the move.
- Row and column.
- Previous turn index.
- Execution status.

When undo is called:

1. Retrieve the latest executed command.
2. Remove the player's symbol from the board.
3. Decrement the corresponding row counter.
4. Decrement the corresponding column counter.
5. Decrement applicable diagonal counters.
6. Restore the previous turn.
7. Restore the game state.

This avoids recalculating the entire board after undo.

---

## Class Design

```text
Game
 ├── Board
 ├── Player[]
 ├── TrackerService
 ├── WinningStrategy
 ├── MoveCommand
 └── GameStatus

Player (Interface)
 ├── HumanPlayer
 └── BotPlayer

PlayerFactory

WinningStrategy (Interface)
 └── FullLineWinningStrategy

BotMoveStrategy (Interface)
 └── RandomBotStrategy

TrackerService
 └── PlayerTracker

MoveCommand
 ├── execute()
 └── undo()
```

## Design Patterns Used

### 1. Strategy Pattern

Used for winning conditions and bot move generation.

**WinningStrategy**

Defines how a winning move is identified.

Current implementation:
- FullLineWinningStrategy

Possible extensions:
- KInARowWinningStrategy

**BotMoveStrategy**

Defines how a bot chooses its next move.

Current implementation:
- RandomBotStrategy

Possible extensions:
- EasyBotStrategy
- MediumBotStrategy
- MinimaxBotStrategy

### 2. Factory Pattern

PlayerFactory is responsible for creating the appropriate player implementation.

```java
Player player = PlayerFactory.createPlayer(
    "1",
    "Player 1",
    'X',
    PlayerType.HUMAN,
    null
);
```

This keeps object creation separate from game execution.

### 3. Command Pattern

MoveCommand encapsulates the execution and reversal of a move.

It provides:

- `execute()`
- `undo()`

The Game maintains a history of executed commands and reverses the latest move when requested.

### 4. Observer Pattern — Extension

Observer is suitable for notifying external components when game events occur.

Potential subscribers:

- Spectators
- UI components
- Game event listeners

Tracker updates remain part of the core game execution rather than being handled through observers.

---

## Project Structure

```text
src/main/java/com/tictactoe/
├── enums/
│   ├── GameState.java
│   └── PlayerType.java
├── exception/
│   └── InvalidMoveException.java
├── model/
│   ├── Board.java
│   ├── Player.java
│   ├── HumanPlayer.java
│   ├── BotPlayer.java
│   ├── Position.java
│   └── GameStatus.java
├── factory/
│   └── PlayerFactory.java
├── strategy/
│   ├── WinningStrategy.java
│   ├── FullLineWinningStrategy.java
│   ├── BotMoveStrategy.java
│   └── RandomBotStrategy.java
├── service/
│   └── TrackerService.java
├── command/
│   └── MoveCommand.java
└── Game.java
```

## Complexity Analysis

Let:
- N = Board dimension.
- K = Number of players.

| Operation | Time Complexity |
|---|---|
| Initialize board | O(N²) |
| Initialize player trackers | O(K × N) |
| Validate move | O(1) |
| Execute move | O(1) |
| Update tracker | O(1) |
| Detect winner | O(1) |
| Detect draw | O(1) |
| Undo last move | O(1) |
| Retrieve current player | O(1) |
| Random bot move selection | O(N²) |

### Space Complexity

| Component | Space |
|---|---|
| Board | O(N²) |
| Player trackers | O(K × N) |
| Move history | O(N²) |

Overall space complexity:

**O(N² + K × N)**

---

## Future Enhancements

The current implementation focuses on the full-row, full-column and full-diagonal winning conditions.

Potential extensions include:

1. **K-in-a-row:** Support configurable winning conditions such as three consecutive symbols on a larger board.
2. **Spectators:** Notify registered observers whenever a move occurs or a game finishes.
3. **Game Replay:** Store and replay the sequence of moves.
4. **Advanced Bots:** Implement Minimax or other intelligent move-selection strategies.
5. **Additional Winning Strategies:** Support custom winning conditions without changing the Game class.

## Key Learnings

- Separating game orchestration from individual entities.
- Applying polymorphism to support different player types.
- Using counters to optimize winner detection from O(N) to O(1).
- Applying Strategy Pattern for configurable game rules.
- Applying Factory Pattern for player creation.
- Applying Command Pattern for undo functionality.
- Designing an extensible and testable game engine.

---

**Design Focus:** Object-Oriented Design, SOLID Principles, Design Patterns, Extensibility and Algorithmic Optimization.
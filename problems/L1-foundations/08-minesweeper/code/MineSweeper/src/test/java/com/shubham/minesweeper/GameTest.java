package com.shubham.minesweeper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.shubham.minesweeper.entities.Board;
import com.shubham.minesweeper.entities.Cell;
import com.shubham.minesweeper.entities.Game;
import com.shubham.minesweeper.entities.GameConfig;
import com.shubham.minesweeper.entities.RevealResult;
import com.shubham.minesweeper.enums.CellState;
import com.shubham.minesweeper.enums.Difficulty;
import com.shubham.minesweeper.enums.GameState;
import com.shubham.minesweeper.service.DefaultRandomSource;
import com.shubham.minesweeper.service.GameConfigFactory;
import com.shubham.minesweeper.service.GameListener;
import com.shubham.minesweeper.service.MinePlacer;
import com.shubham.minesweeper.service.RandomMinePlacer;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

class GameTest {

  /** Places mines exactly at the given (row, col) pairs, ignoring the excluded set. */
  private static MinePlacer fixed(int[]... mines) {
    return (board, count, excluded) -> {
      for (int[] m : mines) {
        board.getCell(m[0], m[1]).setMine(true);
      }
    };
  }

  /** 5x5 board with mines in two opposite corners: (0,0) and (4,4). */
  private static Game cornersGame() {
    return new Game(new GameConfig(5, 5, 2, true, 0), fixed(new int[] {0, 0}, new int[] {4, 4}));
  }

  private static Game randomGame(GameConfig config) {
    return new Game(config, new RandomMinePlacer(new DefaultRandomSource(config.seed())));
  }

  private static int countMines(Board board) {
    int mines = 0;
    for (int r = 0; r < board.getRows(); r++) {
      for (int c = 0; c < board.getCols(); c++) {
        if (board.getCell(r, c).isMine()) {
          mines++;
        }
      }
    }
    return mines;
  }

  // ---- mine placement --------------------------------------------------

  @Test
  void minesArePlacedLazilyOnFirstReveal() {
    Game game = randomGame(GameConfigFactory.fromDifficulty(Difficulty.BEGINNER, 1));
    assertEquals(0, countMines(game.getBoard()));
    assertEquals(GameState.READY, game.getState());

    game.reveal(4, 4);

    assertEquals(10, countMines(game.getBoard()));
    assertNotEquals(GameState.READY, game.getState());
  }

  @Test
  void firstClickAndItsNeighboursAreNeverMines() {
    for (long seed = 0; seed < 200; seed++) {
      Game game = randomGame(GameConfigFactory.fromDifficulty(Difficulty.EXPERT, seed));
      RevealResult result = game.reveal(0, 0);

      Board board = game.getBoard();
      Cell first = board.getCell(0, 0);
      assertFalse(first.isMine());
      assertEquals(0, first.getAdjacentMines(), "first click should open a region");
      for (Cell n : board.getNeighbours(first)) {
        assertFalse(n.isMine());
      }
      assertNotEquals(GameState.LOST, result.getState());
    }
  }

  @Test
  void withoutSafeNeighboursOnlyTheClickedCellIsSafe() {
    for (long seed = 0; seed < 200; seed++) {
      Game game = randomGame(new GameConfig(4, 4, 6, false, seed));
      game.reveal(1, 1);
      assertFalse(game.getBoard().getCell(1, 1).isMine());
      assertEquals(6, countMines(game.getBoard()));
    }
  }

  @Test
  void sameSeedGivesSameLayout() {
    Game a = randomGame(GameConfigFactory.fromDifficulty(Difficulty.INTERMEDIATE, 42));
    Game b = randomGame(GameConfigFactory.fromDifficulty(Difficulty.INTERMEDIATE, 42));
    a.reveal(8, 8);
    b.reveal(8, 8);
    for (int r = 0; r < 16; r++) {
      for (int c = 0; c < 16; c++) {
        assertEquals(a.getBoard().getCell(r, c).isMine(), b.getBoard().getCell(r, c).isMine());
      }
    }
  }

  // ---- reveal ----------------------------------------------------------

  @Test
  void numberedCellRevealsOnlyItself() {
    Game game = cornersGame();
    RevealResult result = game.reveal(0, 1);

    assertEquals(1, result.getRevealedCells().size());
    assertEquals(1, result.getRevealedCells().get(0).getAdjacentMines());
    assertEquals(GameState.PLAYING, result.getState());
  }

  @Test
  void zeroCellFloodFillsAndRevealingAllSafeCellsWins() {
    Game game = cornersGame();
    RevealResult result = game.reveal(2, 2);

    assertEquals(23, result.getRevealedCells().size());
    assertEquals(GameState.WON, result.getState());
    assertFalse(game.getBoard().getCell(0, 0).isRevealed());
    assertFalse(game.getBoard().getCell(4, 4).isRevealed());
  }

  @Test
  void floodFillDoesNotRevealFlaggedCells() {
    Game game = cornersGame();
    game.toggleFlag(0, 3);
    RevealResult result = game.reveal(2, 2);

    assertEquals(22, result.getRevealedCells().size());
    assertTrue(game.getBoard().getCell(0, 3).isFlagged());
    assertEquals(GameState.PLAYING, result.getState());
  }

  @Test
  void revealingAMineLoses() {
    Game game = cornersGame();
    game.reveal(0, 1);
    RevealResult result = game.reveal(0, 0);

    assertEquals(GameState.LOST, result.getState());
    assertEquals(List.of(game.getBoard().getCell(0, 0)), result.getRevealedCells());
  }

  @Test
  void revealingAFlaggedCellDoesNothing() {
    Game game = cornersGame();
    game.reveal(0, 1);
    game.toggleFlag(0, 0);
    RevealResult result = game.reveal(0, 0);

    assertTrue(result.getRevealedCells().isEmpty());
    assertEquals(GameState.PLAYING, result.getState());
    assertEquals(CellState.FLAGGED, game.getBoard().getCell(0, 0).getState());
  }

  @Test
  void revealingAnAlreadyRevealedCellDoesNothing() {
    Game game = cornersGame();
    game.reveal(0, 1);
    assertTrue(game.reveal(0, 1).getRevealedCells().isEmpty());
  }

  @Test
  void actionsAfterGameOverAreIgnored() {
    Game game = cornersGame();
    game.reveal(0, 1);
    game.reveal(0, 0);

    RevealResult result = game.reveal(2, 2);
    assertTrue(result.getRevealedCells().isEmpty());
    assertEquals(GameState.LOST, result.getState());
    assertFalse(game.toggleFlag(3, 3));
    assertTrue(game.chord(0, 1).getRevealedCells().isEmpty());
  }

  @Test
  void outOfBoundsThrows() {
    Game game = cornersGame();
    assertThrows(IllegalArgumentException.class, () -> game.reveal(5, 0));
    assertThrows(IllegalArgumentException.class, () -> game.toggleFlag(-1, 0));
  }

  @Test
  void largeBoardFloodFillDoesNotOverflowTheStack() {
    Game game = randomGame(new GameConfig(500, 500, 0, true, 7));
    RevealResult result = game.reveal(250, 250);

    assertEquals(500 * 500, result.getRevealedCells().size());
    assertEquals(GameState.WON, result.getState());
  }

  // ---- flags -----------------------------------------------------------

  @Test
  void flagToggleUpdatesCounts() {
    Game game = cornersGame();
    assertTrue(game.toggleFlag(3, 3));
    assertEquals(1, game.getFlagCount());
    assertEquals(1, game.getRemainingMineCount());

    assertTrue(game.toggleFlag(3, 3));
    assertEquals(0, game.getFlagCount());
    assertEquals(CellState.HIDDEN, game.getBoard().getCell(3, 3).getState());
  }

  @Test
  void cannotFlagARevealedCell() {
    Game game = cornersGame();
    game.reveal(0, 1);
    assertFalse(game.toggleFlag(0, 1));
    assertEquals(0, game.getFlagCount());
  }

  // ---- chord -----------------------------------------------------------

  @Test
  void chordWithCorrectFlagsRevealsNeighbours() {
    Game game = cornersGame();
    game.reveal(0, 1);
    game.toggleFlag(0, 0);
    RevealResult result = game.chord(0, 1);

    assertFalse(result.getRevealedCells().isEmpty());
    assertEquals(GameState.WON, result.getState());
  }

  @Test
  void chordWithWrongFlagsLoses() {
    Game game = cornersGame();
    game.reveal(0, 1);
    game.toggleFlag(1, 1); // wrong: the mine is at (0,0)
    RevealResult result = game.chord(0, 1);

    assertEquals(GameState.LOST, result.getState());
    assertTrue(result.getRevealedCells().contains(game.getBoard().getCell(0, 0)));
  }

  @Test
  void chordWithMismatchedFlagCountDoesNothing() {
    Game game = cornersGame();
    game.reveal(0, 1);
    RevealResult result = game.chord(0, 1);

    assertTrue(result.getRevealedCells().isEmpty());
    assertEquals(GameState.PLAYING, result.getState());
  }

  // ---- config / presets ------------------------------------------------

  @Test
  void mineCountTooHighIsRejected() {
    assertThrows(IllegalArgumentException.class, () -> new GameConfig(9, 9, 72, true, 0));
    assertThrows(IllegalArgumentException.class, () -> new GameConfig(9, 9, -1, true, 0));
    assertThrows(IllegalArgumentException.class, () -> new GameConfig(501, 9, 1, true, 0));
    new GameConfig(9, 9, 71, true, 0); // the largest allowed count works
  }

  @Test
  void difficultyPresets() {
    GameConfig expert = GameConfigFactory.fromDifficulty(Difficulty.EXPERT, 0);
    assertEquals(16, expert.rows());
    assertEquals(30, expert.cols());
    assertEquals(99, expert.mineCount());
    assertEquals(40, GameConfigFactory.fromDifficulty(Difficulty.INTERMEDIATE, 0).mineCount());
    assertEquals(10, GameConfigFactory.fromDifficulty(Difficulty.BEGINNER, 0).mineCount());
  }

  // ---- observer --------------------------------------------------------

  @Test
  void listenersAreNotified() {
    List<String> events = new ArrayList<>();
    Game game = cornersGame();
    game.addListener(
        new GameListener() {
          @Override
          public void onCellsRevealed(List<Cell> cells) {
            events.add("revealed:" + cells.size());
          }

          @Override
          public void onFlagToggled(Cell cell) {
            events.add("flag:" + cell);
          }

          @Override
          public void onGameOver(GameState state) {
            events.add("over:" + state);
          }
        });

    game.toggleFlag(0, 0);
    game.reveal(2, 2);

    assertEquals(List.of("flag:(0,0)", "revealed:23", "over:WON"), events);
  }
}

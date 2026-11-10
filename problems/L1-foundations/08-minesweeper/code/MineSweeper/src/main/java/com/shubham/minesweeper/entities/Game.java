package com.shubham.minesweeper.entities;

import com.shubham.minesweeper.enums.CellState;
import com.shubham.minesweeper.enums.GameState;
import com.shubham.minesweeper.service.FloodFiller;
import com.shubham.minesweeper.service.GameListener;
import com.shubham.minesweeper.service.MinePlacer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Set;

public class Game {
  private final GameConfig config;
  private final Board board;
  private final MinePlacer minePlacer;
  private final FloodFiller floodFiller;
  private final List<GameListener> listeners;

  private GameState state;
  private int revealedSafeCount;
  private int flagCount;

  public Game(GameConfig config, MinePlacer minePlacer) {
    this.config = config;
    this.board = new Board(config.rows(), config.cols());
    this.minePlacer = minePlacer;
    this.floodFiller = new FloodFiller();
    this.listeners = new ArrayList<>();
    this.state = GameState.READY;
    this.revealedSafeCount = 0;
    this.flagCount = 0;
  }

  public void addListener(GameListener listener) {
    listeners.add(listener);
  }

  public RevealResult reveal(int row, int col) {
    Cell cell = board.getCell(row, col);
    if (!state.canReveal() || cell.isHidden()) {
      return RevealResult.nothing(state);
    }

    if (state == GameState.READY) {
      placeMinesAround(cell);
      state = GameState.PLAYING;
    }

    if (cell.isMine()) {
      return explode(List.of(), cell);
    }

    List<Cell> revealed = floodFiller.reveal(board, cell);
    return finishReveal(revealed);
  }

  public boolean toggleFlag(int row, int col) {
    Cell cell = board.getCell(row, col);
    if (!state.canFlag() || cell.isRevealed()) {
      return false;
    }

    if (cell.isFlagged()) {
      cell.setState(CellState.HIDDEN);
      flagCount--;
    } else {
      cell.setState(CellState.FLAGGED);
      flagCount++;
    }
    for (GameListener listener : listeners) {
      listener.onFlagToggled(cell);
    }
    return true;
  }

  public RevealResult chord(int row, int col) {
    Cell cell = board.getCell(row, col);
    if (state != GameState.PLAYING || !cell.isRevealed() || cell.getAdjacentMines() == 0) {
      return RevealResult.nothing(state);
    }

    List<Cell> neighbours = board.getNeighbours(cell);
    int flagged = 0;
    for (Cell neighbour : neighbours) {
      if (neighbour.isFlagged()) {
        flagged++;
      }
    }
    if (flagged != cell.getAdjacentMines()) {
      return RevealResult.nothing(state);
    }

    List<Cell> revealed = new ArrayList<>();
    for (Cell neighbour : neighbours) {
      if (neighbour.isHidden()) {
        continue; // flagged, or already revealed by an earlier flood in this loop
      }
      if (neighbour.isMine()) {
        return explode(revealed, neighbour);
      }
      revealed.addAll(floodFiller.reveal(board, neighbour));
    }
    return finishReveal(revealed);
  }

  private void placeMinesAround(Cell firstClick) {
    Set<Cell> excluded = Collections.newSetFromMap(new IdentityHashMap<>());
    excluded.add(firstClick);
    if (config.safeNeighbours()) {
      excluded.addAll(board.getNeighbours(firstClick));
    }
    minePlacer.placeMines(board, config.mineCount(), excluded);
    board.computeAdjacentCounts();
  }

  private RevealResult explode(List<Cell> revealedSoFar, Cell mine) {
    mine.setState(CellState.REVEALED);
    List<Cell> revealed = new ArrayList<>(revealedSoFar);
    revealed.add(mine);
    revealedSafeCount += revealedSoFar.size();
    state = GameState.LOST;
    notifyRevealed(revealed);
    notifyGameOver();
    return new RevealResult(revealed, state);
  }

  private RevealResult finishReveal(List<Cell> revealed) {
    revealedSafeCount += revealed.size();
    if (revealedSafeCount == board.totalCells() - config.mineCount()) {
      state = GameState.WON;
    }
    if (!revealed.isEmpty()) {
      notifyRevealed(revealed);
    }
    if (state == GameState.WON) {
      notifyGameOver();
    }
    return new RevealResult(revealed, state);
  }

  private void notifyRevealed(List<Cell> revealed) {
    List<Cell> view = Collections.unmodifiableList(revealed);
    for (GameListener listener : listeners) {
      listener.onCellsRevealed(view);
    }
  }

  private void notifyGameOver() {
    for (GameListener listener : listeners) {
      listener.onGameOver(state);
    }
  }

  public Board getBoard() {
    return board;
  }

  public GameConfig getConfig() {
    return config;
  }

  public GameState getState() {
    return state;
  }

  public int getFlagCount() {
    return flagCount;
  }

  public int getRemainingMineCount() {
    return config.mineCount() - flagCount;
  }
}

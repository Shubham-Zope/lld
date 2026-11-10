package com.shubham.minesweeper.entities;

import com.shubham.minesweeper.enums.GameState;
import java.util.List;

public class RevealResult {
  private final List<Cell> revealedCells;
  private final GameState state;

  public RevealResult(List<Cell> revealedCells, GameState state) {
    this.revealedCells = List.copyOf(revealedCells);
    this.state = state;
  }

  public static RevealResult nothing(GameState state) {
    return new RevealResult(List.of(), state);
  }

  public List<Cell> getRevealedCells() {
    return revealedCells;
  }

  public GameState getState() {
    return state;
  }
}

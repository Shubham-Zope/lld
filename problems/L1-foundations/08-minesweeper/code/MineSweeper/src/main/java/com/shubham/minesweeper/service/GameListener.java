package com.shubham.minesweeper.service;

import com.shubham.minesweeper.entities.Cell;
import com.shubham.minesweeper.enums.GameState;
import java.util.List;

public interface GameListener {
  void onCellsRevealed(List<Cell> cells);

  void onFlagToggled(Cell cell);

  void onGameOver(GameState state);
}

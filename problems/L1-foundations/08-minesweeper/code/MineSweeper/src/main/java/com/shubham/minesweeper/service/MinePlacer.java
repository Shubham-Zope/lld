package com.shubham.minesweeper.service;

import com.shubham.minesweeper.entities.Board;
import com.shubham.minesweeper.entities.Cell;
import java.util.Set;

public interface MinePlacer {
  void placeMines(Board board, int mineCount, Set<Cell> excludedCells);
}

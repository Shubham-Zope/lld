package com.shubham.minesweeper.service;

import com.shubham.minesweeper.entities.Board;
import com.shubham.minesweeper.entities.Cell;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class RandomMinePlacer implements MinePlacer {
  private final RandomSource randomSource;

  public RandomMinePlacer(RandomSource randomSource) {
    this.randomSource = randomSource;
  }

  @Override
  public void placeMines(Board board, int mineCount, Set<Cell> excludedCells) {
    List<Cell> candidates = new ArrayList<>(board.totalCells());
    for (int r = 0; r < board.getRows(); r++) {
      for (int c = 0; c < board.getCols(); c++) {
        Cell cell = board.getCell(r, c);
        if (!excludedCells.contains(cell)) {
          candidates.add(cell);
        }
      }
    }

    if (mineCount > candidates.size()) {
      throw new IllegalStateException("Not enough free cells to place " + mineCount + " mines");
    }

    // Partial Fisher-Yates: the first mineCount slots end up a uniform random pick.
    for (int i = 0; i < mineCount; i++) {
      int j = i + randomSource.nextInt(candidates.size() - i);
      Cell picked = candidates.get(j);
      candidates.set(j, candidates.get(i));
      candidates.set(i, picked);
      picked.setMine(true);
    }
  }
}

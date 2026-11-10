package com.shubham.minesweeper.service;

import com.shubham.minesweeper.entities.Board;
import com.shubham.minesweeper.entities.Cell;
import com.shubham.minesweeper.enums.CellState;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

/** Iterative BFS reveal. Safe on large boards because it never recurses. */
public class FloodFiller {

  /** Reveals start (a hidden non-mine cell) and floods outward from zero cells. */
  public List<Cell> reveal(Board board, Cell start) {
    List<Cell> revealed = new ArrayList<>();
    Deque<Cell> queue = new ArrayDeque<>();

    start.setState(CellState.REVEALED);
    revealed.add(start);

    if (start.getAdjacentMines() == 0) {
      queue.add(start);
    }

    while (!queue.isEmpty()) {
      Cell current = queue.poll();
      for (Cell neighbour : board.getNeighbours(current)) {
        // Skip cells already revealed, flagged by the player, or mines.
        if (neighbour.isHidden() || neighbour.isMine()) {
          continue;
        }
        neighbour.setState(CellState.REVEALED);
        revealed.add(neighbour);
        if (neighbour.getAdjacentMines() == 0) {
          queue.add(neighbour);
        }
      }
    }
    return revealed;
  }
}

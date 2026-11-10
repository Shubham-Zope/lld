package com.shubham.minesweeper.entities;

import java.util.ArrayList;
import java.util.List;

public class Board {
  private final int rows;
  private final int cols;
  private final Cell[][] cells;

  public Board(int rows, int cols) {
    this.rows = rows;
    this.cols = cols;
    this.cells = new Cell[rows][cols];
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        cells[r][c] = new Cell(r, c);
      }
    }
  }

  public boolean isInside(int row, int col) {
    return row >= 0 && row < rows && col >= 0 && col < cols;
  }

  public Cell getCell(int row, int col) {
    if (!isInside(row, col)) {
      throw new IllegalArgumentException("Cell (" + row + "," + col + ") is outside the board");
    }
    return cells[row][col];
  }

  public List<Cell> getNeighbours(Cell cell) {
    List<Cell> neighbours = new ArrayList<>(8);
    for (int dr = -1; dr <= 1; dr++) {
      for (int dc = -1; dc <= 1; dc++) {
        if (dr == 0 && dc == 0) {
          continue;
        }
        int r = cell.getRow() + dr;
        int c = cell.getCol() + dc;
        if (isInside(r, c)) {
          neighbours.add(cells[r][c]);
        }
      }
    }
    return neighbours;
  }

  public void computeAdjacentCounts() {
    for (int r = 0; r < rows; r++) {
      for (int c = 0; c < cols; c++) {
        Cell cell = cells[r][c];
        int count = 0;
        for (Cell neighbour : getNeighbours(cell)) {
          if (neighbour.isMine()) {
            count++;
          }
        }
        cell.setAdjacentMines(count);
      }
    }
  }

  public int getRows() {
    return rows;
  }

  public int getCols() {
    return cols;
  }

  public int totalCells() {
    return rows * cols;
  }
}

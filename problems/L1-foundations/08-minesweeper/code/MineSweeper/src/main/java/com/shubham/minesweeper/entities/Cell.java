package com.shubham.minesweeper.entities;

import com.shubham.minesweeper.enums.CellState;

public class Cell {
  private final int row;
  private final int col;
  private boolean mine;
  private int adjacentMines;
  private CellState state;

  public Cell(int row, int col) {
    this.row = row;
    this.col = col;
    this.mine = false;
    this.adjacentMines = 0;
    this.state = CellState.HIDDEN;
  }

  public int getRow() {
    return row;
  }

  public int getCol() {
    return col;
  }

  public boolean isMine() {
    return mine;
  }

  public void setMine(boolean mine) {
    this.mine = mine;
  }

  public int getAdjacentMines() {
    return adjacentMines;
  }

  public void setAdjacentMines(int adjacentMines) {
    this.adjacentMines = adjacentMines;
  }

  public CellState getState() {
    return state;
  }

  public void setState(CellState state) {
    this.state = state;
  }

  public boolean isHidden() {
    return state != CellState.HIDDEN;
  }

  public boolean isRevealed() {
    return state == CellState.REVEALED;
  }

  public boolean isFlagged() {
    return state == CellState.FLAGGED;
  }

  @Override
  public String toString() {
    return "(" + row + "," + col + ")";
  }
}

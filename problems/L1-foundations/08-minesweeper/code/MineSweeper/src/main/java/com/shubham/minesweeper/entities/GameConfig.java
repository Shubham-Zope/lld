package com.shubham.minesweeper.entities;

public record GameConfig(int rows, int cols, int mineCount, boolean safeNeighbours, long seed) {
  public static final int MAX_SIZE = 500;
  private static final int SAFE_ZONE = 9;

  public GameConfig {
    if (rows < 1 || cols < 1 || rows > MAX_SIZE || cols > MAX_SIZE) {
      throw new IllegalArgumentException(
          "Board must be between 1x1 and " + MAX_SIZE + "x" + MAX_SIZE);
    }
    if (mineCount < 0) {
      throw new IllegalArgumentException("Mine count cannot be negative");
    }
    if (mineCount >= rows * cols - SAFE_ZONE) {
      throw new IllegalArgumentException(
          "Mine count too high: " + mineCount + " must be < " + (rows * cols - SAFE_ZONE));
    }
  }
}

package com.shubham.snakegame.entities;

public class Board {
  private final int width;
  private final int height;
  private final Position[][] grid;

  public Board(int width, int height) {
    this.width = width;
    this.height = height;

    this.grid = new Position[width][height];
    for (int x = 0; x < width; x++) {
      for (int y = 0; y < height; y++) {
        this.grid[x][y] = new Position(x, y);
      }
    }
  }

  public Position getCell(int x, int y) {
    return grid[x][y];
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isInside(int x, int y) {
    return x >= 0 && x < width && y >= 0 && y < height;
  }
}

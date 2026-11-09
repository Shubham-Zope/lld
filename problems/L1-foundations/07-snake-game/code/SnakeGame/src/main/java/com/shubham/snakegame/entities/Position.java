package com.shubham.snakegame.entities;

public class Position {
  private final int x;
  private final int y;
  private boolean isSnakePresent;

  public Position(int x, int y) {
    this.x = x;
    this.y = y;
    this.isSnakePresent = false;
  }

  public int getX() {
    return x;
  }

  public int getY() {
    return y;
  }

  public boolean isSnakePresent() {
    return isSnakePresent;
  }

  public void setSnakePresent(boolean snakePresent) {
    isSnakePresent = snakePresent;
  }
}

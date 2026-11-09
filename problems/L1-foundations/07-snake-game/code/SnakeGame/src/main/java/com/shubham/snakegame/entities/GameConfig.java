package com.shubham.snakegame.entities;

public class GameConfig {
  private final int width;
  private final int height;
  private final boolean wrapAround;
  private final int startingSnakeLength;
  private final long seed;

  public GameConfig(int width, int height, boolean wrapAround, int startingSnakeLength, long seed) {
    if (width * height < startingSnakeLength + 1) {
      throw new IllegalArgumentException(
          "Board is too small for the starting snake plus one food cell");
    }

    this.width = width;
    this.height = height;
    this.wrapAround = wrapAround;
    this.startingSnakeLength = startingSnakeLength;
    this.seed = seed;
  }

  public int getWidth() {
    return width;
  }

  public int getHeight() {
    return height;
  }

  public boolean isWrapAround() {
    return wrapAround;
  }

  public int getStartingSnakeLength() {
    return startingSnakeLength;
  }

  public long getSeed() {
    return seed;
  }
}

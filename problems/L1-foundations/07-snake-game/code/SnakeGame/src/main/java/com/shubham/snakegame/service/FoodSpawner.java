package com.shubham.snakegame.service;

import com.shubham.snakegame.entities.Board;
import com.shubham.snakegame.entities.Position;

public class FoodSpawner {
  private final RandomSource randomSource;

  public FoodSpawner(RandomSource randomSource) {
    this.randomSource = randomSource;
  }

  public Position spawn(Board board) {
    Position candidate;

    do {
      int x = randomSource.nextInt(board.getWidth());
      int y = randomSource.nextInt(board.getHeight());
      candidate = board.getCell(x, y);
    } while (candidate.isSnakePresent());

    return candidate;
  }
}

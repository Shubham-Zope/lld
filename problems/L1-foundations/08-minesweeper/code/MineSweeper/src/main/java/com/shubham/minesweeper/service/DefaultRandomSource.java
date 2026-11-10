package com.shubham.minesweeper.service;

import java.util.Random;

public class DefaultRandomSource implements RandomSource {
  private final Random random;

  public DefaultRandomSource(long seed) {
    this.random = new Random(seed);
  }

  @Override
  public int nextInt(int bound) {
    return random.nextInt(bound);
  }
}

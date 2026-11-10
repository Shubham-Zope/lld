package com.shubham.minesweeper.service;

import com.shubham.minesweeper.entities.GameConfig;
import com.shubham.minesweeper.enums.Difficulty;

public final class GameConfigFactory {
  private GameConfigFactory() {}

  public static GameConfig fromDifficulty(Difficulty difficulty, long seed) {
    return new GameConfig(
        difficulty.getRows(), difficulty.getCols(), difficulty.getMines(), true, seed);
  }

  public static GameConfig custom(int rows, int cols, int mines, long seed) {
    return new GameConfig(rows, cols, mines, true, seed);
  }
}

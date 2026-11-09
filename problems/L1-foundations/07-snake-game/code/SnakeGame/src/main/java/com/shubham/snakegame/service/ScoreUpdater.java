package com.shubham.snakegame.service;

import com.shubham.snakegame.entities.Score;
import com.shubham.snakegame.enums.GameReason;

public class ScoreUpdater implements GameListener {
  private final Score score;

  public ScoreUpdater(Score score) {
    this.score = score;
  }

  @Override
  public void onFoodEaten() {
    score.increment();
  }

  @Override
  public void onFoodSpawned() {}

  @Override
  public void onGameOver(GameReason reason) {}
}

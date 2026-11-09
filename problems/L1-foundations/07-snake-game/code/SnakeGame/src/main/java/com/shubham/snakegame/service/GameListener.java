package com.shubham.snakegame.service;

import com.shubham.snakegame.enums.GameReason;

public interface GameListener {
  void onFootEaten();

  void onFoodSpawned();

  GameReason onGameOver(String reason);
}

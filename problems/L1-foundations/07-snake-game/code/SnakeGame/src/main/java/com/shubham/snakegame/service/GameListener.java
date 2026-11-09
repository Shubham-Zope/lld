package com.shubham.snakegame.service;

import com.shubham.snakegame.enums.GameReason;

public interface GameListener {
  void onFoodEaten();

  void onFoodSpawned();

  void onGameOver(GameReason reason);
}

package com.shubham.snakegame.entities;

public class Food {
  private Position food;

  Food(Position food) {
    this.food = food;
  }

  public Position getFood() {
    return this.food;
  }

  public void setFood(Position food) {
    this.food = food;
  }
}

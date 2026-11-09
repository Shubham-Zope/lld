package com.shubham.snakegame.entities;

public class Score {
  private int score;

  public Score() {
    this(0);
  }

  Score(int score) {
    this.score = score;
  }

  public int getScore() {
    return this.score;
  }

  public void setScore(int score) {
    this.score = score;
  }

  public void increment() {
    this.score++;
  }
}

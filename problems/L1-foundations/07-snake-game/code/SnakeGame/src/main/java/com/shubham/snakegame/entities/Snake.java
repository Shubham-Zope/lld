package com.shubham.snakegame.entities;

import java.util.ArrayDeque;
import java.util.Deque;

public class Snake {
  private final Deque<Position> snake;

  public Snake() {
    this.snake = new ArrayDeque<>();
  }

  public Deque<Position> getSnake() {
    return snake;
  }

  public void addHead(Position pos) {
    this.snake.addFirst(pos);
  }

  public void removeTail() {
    this.snake.removeLast();
  }

  public Position getHead() {
    return this.snake.getFirst();
  }

  public Position getTail() {
    return this.snake.getLast();
  }

  public int size() {
    return this.snake.size();
  }
}

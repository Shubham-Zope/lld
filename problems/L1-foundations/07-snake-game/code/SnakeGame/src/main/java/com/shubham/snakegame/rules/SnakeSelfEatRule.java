package com.shubham.snakegame.rules;

import com.shubham.snakegame.entities.Position;
import com.shubham.snakegame.entities.Snake;

public class SnakeSelfEatRule {
  public boolean isSelfCollision(Position next, Snake snake, boolean willEat) {
    if (!next.isSnakePresent()) {
      return false;
    }

    Position tail = snake.getTail();
    boolean isTail = next.getX() == tail.getX() && next.getY() == tail.getY();

    return willEat || !isTail;
  }
}

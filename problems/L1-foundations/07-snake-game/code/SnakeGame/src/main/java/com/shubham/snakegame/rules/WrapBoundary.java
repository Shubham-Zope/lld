package com.shubham.snakegame.rules;

import com.shubham.snakegame.entities.Board;
import com.shubham.snakegame.entities.Position;

public class WrapBoundary implements BoundaryRule {
  @Override
  public Position resolve(Position nextPosition, Board board) {
    int x = nextPosition.getX();
    int y = nextPosition.getY();

    if (x < 0) {
      x = board.getWidth() - 1;
    } else if (x >= board.getWidth()) {
      x = 0;
    }

    if (y < 0) {
      y = board.getHeight() - 1;
    } else if (y >= board.getHeight()) {
      y = 0;
    }

    return new Position(x, y);
  }
}

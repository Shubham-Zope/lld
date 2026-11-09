package com.shubham.snakegame.rules;

import com.shubham.snakegame.entities.Board;
import com.shubham.snakegame.entities.Position;

public class WallBoundary implements BoundaryRule {
  @Override
  public Position resolve(Position nextPosition, Board board) {
    if (!board.isInside(nextPosition.getX(), nextPosition.getY())) {
      return null;
    }

    return nextPosition;
  }
}

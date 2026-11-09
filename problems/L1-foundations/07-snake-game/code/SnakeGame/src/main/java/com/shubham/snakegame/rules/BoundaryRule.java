package com.shubham.snakegame.rules;

import com.shubham.snakegame.entities.Board;
import com.shubham.snakegame.entities.Position;

public interface BoundaryRule {
  Position resolve(Position nextPosition, Board board);
}

package com.shubham.snakegame.rules;

import com.shubham.snakegame.entities.Board;
import com.shubham.snakegame.entities.Snake;

public class AllCellsFilledRule {
  public boolean isBoardFull(Snake snake, Board board) {
    return snake.size() == board.getWidth() * board.getHeight();
  }
}

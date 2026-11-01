package com.tictactoe.strategy;

import com.tictactoe.models.Board;
import com.tictactoe.models.Player;
import com.tictactoe.models.Position;

public interface BotMoveStrategy {
    Position getNextMove(Board board, Player player);
}
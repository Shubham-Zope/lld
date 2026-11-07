package com.shubham.snakeladder.game;

import com.shubham.snakeladder.board.Jump;

import java.util.List;

public record TurnResult(
        String playerName,
        int roll,
        int from,
        int to,
        List<Jump> jumps,
        boolean won,
        boolean cancelled,
        boolean extraTurn
) {
    public TurnResult {
        jumps = List.copyOf(jumps);
    }
}
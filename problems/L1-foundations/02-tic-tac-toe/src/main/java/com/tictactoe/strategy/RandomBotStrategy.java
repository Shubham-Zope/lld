package com.tictactoe.strategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.tictactoe.models.Board;
import com.tictactoe.models.Player;
import com.tictactoe.models.Position;

public class RandomBotStrategy implements BotMoveStrategy {

    private final Random random = new Random();

    @Override
    public Position getNextMove(Board board, Player player) {

        List<Position> availablePositions = new ArrayList<>();

        for (int row = 0; row < board.getSize(); row++) {
            for (int col = 0; col < board.getSize(); col++) {

                if (board.isEmpty(row, col)) {
                    availablePositions.add(
                            new Position(row, col)
                    );
                }
            }
        }

        if (availablePositions.isEmpty()) {
            return null;
        }

        return availablePositions.get(
                random.nextInt(availablePositions.size())
        );
    }
}

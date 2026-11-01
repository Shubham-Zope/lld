package com.tictactoe.command;

import com.tictactoe.Game;
import com.tictactoe.models.Player;

public class MoveCommand {

    private final Game game;
    private final Player player;
    private final int row;
    private final int col;

    private int previousTurnIndex;
    private boolean executed;

    public MoveCommand(
            Game game,
            Player player,
            int row,
            int col
    ) {
        this.game = game;
        this.player = player;
        this.row = row;
        this.col = col;
    }

    public void execute() {

        previousTurnIndex = game.getCurrentTurnIndex();

        game.applyMove(player, row, col);

        executed = true;
    }

    public void undo() {

        if (!executed) {
            throw new IllegalStateException(
                    "Command was not executed"
            );
        }

        game.rollbackMove(
                player,
                row,
                col,
                previousTurnIndex
        );

        executed = false;
    }
}
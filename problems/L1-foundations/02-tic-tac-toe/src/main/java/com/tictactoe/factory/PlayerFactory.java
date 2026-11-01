package com.tictactoe.factory;

import com.tictactoe.enums.PlayerType;
import com.tictactoe.models.BotPlayer;
import com.tictactoe.models.HumanPlayer;
import com.tictactoe.models.Player;
import com.tictactoe.strategy.BotMoveStrategy;

public class PlayerFactory {
    public static Player createPlayer(
        String id,
        String name,
        char symbol,
        PlayerType type,
        BotMoveStrategy botStrategy
    ) {

        if (type == PlayerType.HUMAN) {
            return new HumanPlayer(id, name, symbol);
        }

        if (type == PlayerType.BOT) {
            if (botStrategy == null) {
                throw new IllegalArgumentException(
                        "Bot strategy is required"
                );
            }

            return new BotPlayer(
                id,
                name,
                symbol,
                botStrategy
            );
        }

        throw new IllegalArgumentException("Invalid player type");
    }
}

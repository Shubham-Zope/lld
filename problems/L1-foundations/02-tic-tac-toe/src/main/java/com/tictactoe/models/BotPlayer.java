package com.tictactoe.models;

import com.tictactoe.enums.PlayerType;
import com.tictactoe.strategy.BotMoveStrategy;

public class BotPlayer implements Player {

    private final String id;
    private final String name;
    private final char symbol;
    private final BotMoveStrategy botMoveStrategy;

    public BotPlayer(String id, String name, char symbol, BotMoveStrategy botMoveStrategy) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
        this.botMoveStrategy = botMoveStrategy;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public char getSymbol() {
        return symbol;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public PlayerType getType() {
        return PlayerType.BOT;
    }

    public BotMoveStrategy getBotMoveStrategy() {
        return botMoveStrategy;
    }
}

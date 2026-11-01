package com.tictactoe.models;

import com.tictactoe.enums.PlayerType;

public class HumanPlayer implements Player {
    private final String id;
    private final String name;
    private final char symbol;

    public HumanPlayer(String id, String name, char symbol) {
        this.id = id;
        this.name = name;
        this.symbol = symbol;
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
        return PlayerType.HUMAN;
    }
}
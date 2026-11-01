package com.tictactoe.models;

import com.tictactoe.enums.PlayerType;

public interface Player {

    String getId();

    String getName();

    char getSymbol();

    PlayerType getType();

}

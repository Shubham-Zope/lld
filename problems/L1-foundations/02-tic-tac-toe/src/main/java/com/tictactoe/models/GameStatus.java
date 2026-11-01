package com.tictactoe.models;

import com.tictactoe.enums.GameState;

public record GameStatus(GameState state, Player player) {

    /**
     * 
     * @return
     */
    public static GameStatus inProgress() {
        return new GameStatus(GameState.IN_PROGRESS, null);
    }

    /**
     * 
     * @return
     */
    public static GameStatus draw() {
        return new GameStatus(GameState.DRAW, null);
    }

    /**
     * 
     * @param player
     * @return
     */
    public static GameStatus won(Player player) {
        return new GameStatus(GameState.WON, player);
    }
}

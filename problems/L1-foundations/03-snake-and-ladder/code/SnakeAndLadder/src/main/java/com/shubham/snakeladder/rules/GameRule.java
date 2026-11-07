package com.shubham.snakeladder.rules;

import com.shubham.snakeladder.model.Player;

public interface GameRule {

    default boolean isTurnCancelled(Player player, int roll) {
        return false;
    }

    default boolean grantsExtraTurn(Player player, int roll) {
        return false;
    }

    default void afterTurn(Player player, int roll) {
    }

    default void afterCancelledTurn(Player player) {
    }
}
package com.shubham.snakeladder.rules;

import com.shubham.snakeladder.model.Player;

public class ThreeSixesCancelRule implements GameRule {

    @Override
    public boolean isTurnCancelled(Player player, int roll) {

        return roll == 6 &&
                player.getConsecutiveSixes() == 2;
    }

    @Override
    public void afterTurn(Player player, int roll) {

        if (roll == 6) {
            player.setConsecutiveSixes(
                    player.getConsecutiveSixes() + 1
            );
        } else {
            player.setConsecutiveSixes(0);
        }
    }

    @Override
    public void afterCancelledTurn(Player player) {
        player.setConsecutiveSixes(0);
    }
}
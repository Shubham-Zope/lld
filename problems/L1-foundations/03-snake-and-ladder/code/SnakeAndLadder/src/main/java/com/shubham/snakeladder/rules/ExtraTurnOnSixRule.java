package com.shubham.snakeladder.rules;

import com.shubham.snakeladder.model.Player;

public class ExtraTurnOnSixRule implements GameRule {

    @Override
    public boolean grantsExtraTurn(Player player, int roll) {
        return roll == 6;
    }
}
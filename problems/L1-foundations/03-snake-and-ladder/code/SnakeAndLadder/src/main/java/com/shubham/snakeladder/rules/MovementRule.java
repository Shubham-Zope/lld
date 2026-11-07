package com.shubham.snakeladder.rules;

public interface MovementRule extends GameRule {

    int calculatePosition(
            int currentPosition,
            int roll,
            int boardSize
    );
}
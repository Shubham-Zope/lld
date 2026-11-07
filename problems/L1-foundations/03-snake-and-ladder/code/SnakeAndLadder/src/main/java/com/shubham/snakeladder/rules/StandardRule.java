package com.shubham.snakeladder.rules;

public class StandardRule implements MovementRule {

    @Override
    public int calculatePosition(
            int currentPosition,
            int roll,
            int boardSize
    ) {

        int nextPosition = currentPosition + roll;

        // Overshoot means the player stays where they are.
        if (nextPosition > boardSize) {
            return currentPosition;
        }

        return nextPosition;
    }
}
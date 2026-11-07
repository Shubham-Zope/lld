package com.shubham.snakeladder.rules;

public class OvershootBounceBackRule implements MovementRule {

    @Override
    public int calculatePosition(
            int currentPosition,
            int roll,
            int boardSize
    ) {
        int nextPosition = currentPosition + roll;

        if (nextPosition <= boardSize) {
            return nextPosition;
        }

        return boardSize - (nextPosition - boardSize);
    }
}
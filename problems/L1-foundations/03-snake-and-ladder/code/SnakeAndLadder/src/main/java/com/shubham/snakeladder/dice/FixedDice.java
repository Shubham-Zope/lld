package com.shubham.snakeladder.dice;

import java.util.List;

public class FixedDice implements Dice {

    private final List<Integer> rolls;

    private int currentIndex = 0;

    public FixedDice(List<Integer> rolls) {
        this.rolls = List.copyOf(rolls);
    }

    @Override
    public int roll() {

        if (currentIndex >= rolls.size()) {
            throw new IllegalStateException(
                    "No more predefined dice rolls available"
            );
        }

        return rolls.get(currentIndex++);
    }
}
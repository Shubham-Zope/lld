package com.shubham.snakeladder.dice;

import java.util.Random;

public class RandomDice implements Dice {

    private final int diceCount;

    private final int faces;

    private final Random random;

    public RandomDice(int diceCount, int faces) {

        if (diceCount < 1 || faces < 1) {
            throw new IllegalArgumentException(
                    "Dice count and faces must be positive"
            );
        }

        this.diceCount = diceCount;
        this.faces = faces;
        this.random = new Random();
    }

    @Override
    public int roll() {

        int total = 0;

        for (int i = 0; i < diceCount; i++) {

            total += random.nextInt(faces) + 1;
        }

        return total;
    }
}
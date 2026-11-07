package com.shubham.snakeladder.observer;

import com.shubham.snakeladder.game.TurnResult;

public class ConsoleTurnObserver implements TurnObserver {

    @Override
    public void onTurn(TurnResult result) {

        System.out.println("\nPlayer: " + result.playerName());

        System.out.println("Dice Roll: " + result.roll());

        System.out.println(
                "Position: " + result.from() + " -> " + result.to()
        );

        if (result.cancelled()) {
            System.out.println("Turn cancelled!");
        }

        for (var jump : result.jumps()) {

            String type = jump.from() > jump.to()
                    ? "Snake"
                    : "Ladder";

            System.out.println(
                    type + ": " + jump.from() + " -> " + jump.to()
            );
        }

        if (result.extraTurn()) {
            System.out.println("Extra turn granted!");
        }

        if (result.won()) {
            System.out.println("Congratulations! Player won.");
        }
    }
}
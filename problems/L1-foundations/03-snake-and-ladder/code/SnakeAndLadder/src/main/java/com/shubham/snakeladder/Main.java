package com.shubham.snakeladder;

import com.shubham.snakeladder.board.Board;
import com.shubham.snakeladder.board.BoardBuilder;
import com.shubham.snakeladder.dice.Dice;
import com.shubham.snakeladder.dice.RandomDice;
import com.shubham.snakeladder.game.Game;
import com.shubham.snakeladder.model.Player;
import com.shubham.snakeladder.observer.ConsoleTurnObserver;
import com.shubham.snakeladder.rules.ExtraTurnOnSixRule;
import com.shubham.snakeladder.rules.RuleEngine;
import com.shubham.snakeladder.rules.StandardRule;
import com.shubham.snakeladder.rules.ThreeSixesCancelRule;

import java.util.List;

public class Main {

    public static void main(String[] args) {

        // 1. Create board.
        Board board = new BoardBuilder(100)
                .addSnake(99, 54)
                .addSnake(70, 55)
                .addSnake(52, 42)
                .addSnake(25, 5)
                .addLadder(2, 38)
                .addLadder(7, 14)
                .addLadder(8, 31)
                .addLadder(21, 42)
                .build();

        // 2. Configure dice.
        Game game = getGame(board);

        // 6. Register observer.
        game.addObserver(new ConsoleTurnObserver());

        // 7. Start game.
        List<Player> rankings = game.play();

        // 8. Display rankings.
        System.out.println("\n========== FINAL RANKINGS ==========");

        for (Player player : rankings) {

            System.out.println(
                    player.getRank() + ". " + player.getName()
            );
        }

        System.out.println("====================================");
    }

    private static Game getGame(Board board) {
        Dice dice = new RandomDice(1, 6);

        // 3. Configure game rules.
        RuleEngine ruleEngine = new RuleEngine(
                List.of(
                        new StandardRule(),
                        new ExtraTurnOnSixRule(),
                        new ThreeSixesCancelRule()
                )
        );

        // 4. Create players.
        List<Player> players = List.of(
                new Player("Alice"),
                new Player("Bob"),
                new Player("Charlie")
        );

        // 5. Create game.
        Game game = new Game(
                board,
                dice,
                ruleEngine,
                players,
                true
        );
        return game;
    }
}
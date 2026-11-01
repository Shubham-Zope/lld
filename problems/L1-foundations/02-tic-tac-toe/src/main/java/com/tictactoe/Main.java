package com.tictactoe;

import java.util.List;

import com.tictactoe.enums.PlayerType;
import com.tictactoe.factory.PlayerFactory;
import com.tictactoe.models.Player;
import com.tictactoe.strategy.FullLineWinningStrategy;
import com.tictactoe.strategy.RandomBotStrategy;

public class Main {

    public static void main(String[] args) {

        Player player1 = PlayerFactory.createPlayer(
                "1",
                "Shubham",
                'X',
                PlayerType.HUMAN,
                null
        );

        Player player2 = PlayerFactory.createPlayer(
                "2",
                "Bot",
                'O',
                PlayerType.BOT,
                new RandomBotStrategy()
        );

        Game game = new Game(
                3,
                List.of(player1, player2),
                new FullLineWinningStrategy()
        );

        game.printBoard();

        System.out.println("\nCurrent Player: "
                + game.getCurrentPlayer().getName());

        game.makeMove(player1, 0, 0);
        game.playBotTurn();

        game.makeMove(player1, 1, 1);
        game.playBotTurn();

        game.printBoard();

        System.out.println("\nUndoing last move...");

        game.undo();

        game.printBoard();

        System.out.println("\nCurrent Player: "
                + game.getCurrentPlayer().getName());

        System.out.println("\nGame Status: "
                + game.getGameStatus().state());
    }
}
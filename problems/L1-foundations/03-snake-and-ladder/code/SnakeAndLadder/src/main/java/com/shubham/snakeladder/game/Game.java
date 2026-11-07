package com.shubham.snakeladder.game;

import com.shubham.snakeladder.board.Board;
import com.shubham.snakeladder.board.BoardResolution;
import com.shubham.snakeladder.board.Jump;
import com.shubham.snakeladder.dice.Dice;
import com.shubham.snakeladder.model.Player;
import com.shubham.snakeladder.observer.TurnObserver;
import com.shubham.snakeladder.rules.RuleEngine;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Game {

    private final Board board;
    private final Dice dice;
    private final RuleEngine ruleEngine;
    private final List<Player> players;
    private final List<TurnObserver> observers;

    private final boolean continueAfterWinner;

    public Game(
            Board board,
            Dice dice,
            RuleEngine ruleEngine,
            List<Player> players,
            boolean continueAfterWinner
    ) {

        if (board == null || dice == null || ruleEngine == null) {
            throw new IllegalArgumentException(
                    "Board, dice and rules cannot be null"
            );
        }

        if (players == null || players.size() < 2 ||
                players.size() > 8) {
            throw new IllegalArgumentException(
                    "Game requires between 2 and 8 players"
            );
        }

        if (players.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException(
                    "Players cannot contain null"
            );
        }

        if (players.stream().distinct().count() != players.size()) {
            throw new IllegalArgumentException(
                    "Duplicate player objects are not allowed"
            );
        }

        this.board = board;
        this.dice = dice;
        this.ruleEngine = ruleEngine;
        this.players = new ArrayList<>(players);
        this.continueAfterWinner = continueAfterWinner;
        this.observers = new ArrayList<>();
    }

    public void addObserver(TurnObserver observer) {

        if (observer == null) {
            throw new IllegalArgumentException(
                    "Observer cannot be null"
            );
        }

        observers.add(observer);
    }

    public List<Player> play() {

        int currentPlayerIndex = 0;
        int finishedCount = 0;
        int nextRank = 1;

        while (finishedCount < players.size()) {

            Player player = players.get(currentPlayerIndex);

            // Skip players who have already finished.
            if (player.hasFinished()) {
                currentPlayerIndex =
                        (currentPlayerIndex + 1) % players.size();
                continue;
            }

            int roll = dice.roll();

            if (roll <= 0) {
                throw new IllegalStateException(
                        "Dice must return a positive number"
                );
            }

            int from = player.getPosition();

            // Check whether the current turn is cancelled.
            if (ruleEngine.isTurnCancelled(player, roll)) {

                ruleEngine.afterCancelledTurn(player);

                emit(new TurnResult(
                        player.getName(),
                        roll,
                        from,
                        from,
                        List.of(),
                        false,
                        true,
                        false
                ));

                currentPlayerIndex =
                        (currentPlayerIndex + 1) % players.size();

                continue;
            }

            // Calculate movement according to configured rules.
            int nextPosition = ruleEngine.calculatePosition(
                    from,
                    roll,
                    board.getSize()
            );

            if (nextPosition < 1 ||
                    nextPosition > board.getSize()) {

                throw new IllegalStateException(
                        "Movement rule returned invalid position: "
                                + nextPosition
                );
            }

            List<Jump> takenJumps = List.of();

            // Resolve snakes and ladders if the player has moved.
            if (nextPosition != from && nextPosition != board.getSize()) {

                BoardResolution resolution = board.resolvePosition(nextPosition);

                nextPosition = resolution.finalPosition();
                takenJumps = resolution.jumps();
            }

            player.setPosition(nextPosition);

            // Update state maintained by rules.
            ruleEngine.afterTurn(player, roll);

            boolean won = nextPosition == board.getSize();

            if (won) {
                player.finish(nextRank++);
                finishedCount++;
            }

            boolean extraTurn = !won && ruleEngine.grantsExtraTurn(player, roll);

            emit(new TurnResult(
                    player.getName(),
                    roll,
                    from,
                    nextPosition,
                    takenJumps,
                    won,
                    false,
                    extraTurn
            ));

            // Stop once the first winner is found if ranking
            // mode is disabled.
            if (won && !continueAfterWinner) {
                break;
            }

            // Keep the same player if an extra turn is granted.
            if (!extraTurn) {
                currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
            }
        }

        return players.stream()
                .filter(Player::hasFinished)
                .sorted((a, b) ->
                        Integer.compare(a.getRank(), b.getRank()))
                .toList();
    }

    private void emit(TurnResult result) {

        for (TurnObserver observer : observers) {
            observer.onTurn(result);
        }
    }
}
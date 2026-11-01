package com.tictactoe;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.tictactoe.command.MoveCommand;
import com.tictactoe.enums.GameState;
import com.tictactoe.exceptions.InvalidMoveException;
import com.tictactoe.models.Board;
import com.tictactoe.models.BotPlayer;
import com.tictactoe.models.GameStatus;
import com.tictactoe.models.Player;
import com.tictactoe.models.Position;
import com.tictactoe.service.TrackerService;
import com.tictactoe.strategy.WinningStrategy;

public class Game {
    private final Board board;
    private final List<Player> players;
    private final Map<String, Player> playersById;
    private final TrackerService trackerService;
    private final WinningStrategy winningStrategy;
    private final Stack<MoveCommand> moveHistory = new Stack<>();
    private int currentTurnIndex = 0;
    private GameStatus gameStatus = GameStatus.inProgress();

     public Game(
            int boardSize,
            List<Player> players,
            WinningStrategy winningStrategy
    ) {

        if (players == null
                || players.size() < 2
                || players.size() > boardSize - 1) {
            throw new IllegalArgumentException(
                    "Invalid number of players"
            );
        }

        this.board = new Board(boardSize);
        this.players = new ArrayList<>(players);
        this.winningStrategy = winningStrategy;

        this.playersById = new HashMap<>();

        Set<Character> symbols = new HashSet<>();

        for (Player player : players) {

            if (player == null
                    || player.getId() == null
                    || player.getId().isBlank()
                    || player.getName() == null
                    || player.getName().isBlank()) {
                throw new IllegalArgumentException(
                        "Invalid player details"
                );
            }

            if (playersById.containsKey(player.getId())) {
                throw new IllegalArgumentException(
                        "Duplicate player ID"
                );
            }

            if (!symbols.add(player.getSymbol())) {
                throw new IllegalArgumentException(
                        "Duplicate player symbol"
                );
            }

            playersById.put(player.getId(), player);
        }

        this.trackerService = new TrackerService(boardSize);

        for (Player player : players) {
            trackerService.registerPlayer(player.getId());
        }
    }

     public GameStatus makeMove(
            Player player,
            int row,
            int col
    ) {

        MoveCommand command = new MoveCommand(
            this,
            player,
            row,
            col
        );

        command.execute();

        moveHistory.push(command);

        return gameStatus;
    }

     public void applyMove(
            Player player,
            int row,
            int col
    ) {

        validateMove(player, row, col);

        board.place(row, col, player.getSymbol());

        trackerService.updateTrackerMap(
                player.getId(),
                row,
                col
        );

        boolean won = winningStrategy.isWinningMove(
                player,
                row,
                col,
                trackerService
        );

        if (won) {
            gameStatus = GameStatus.won(player);
        } else if (board.isFull()) {
            gameStatus = GameStatus.draw();
        } else {
            currentTurnIndex = (currentTurnIndex + 1) % players.size();
        }
    }

    private void validateMove(
            Player player,
            int row,
            int col
    ) {

        if (gameStatus.state() != GameState.IN_PROGRESS) {
            throw new InvalidMoveException(
                    "Game has already ended"
            );
        }

        if (player == null
                || playersById.get(player.getId()) != player) {
            throw new InvalidMoveException(
                    "Unknown player"
            );
        }

        Player currentPlayer = getCurrentPlayer();

        if (currentPlayer != player) {
            throw new InvalidMoveException(
                    "Not your turn"
            );
        }

        if (row < 0 || row >= board.getSize()
                || col < 0 || col >= board.getSize()) {
            throw new InvalidMoveException(
                    "Position is outside the board"
            );
        }

        if (!board.isEmpty(row, col)) {
            throw new InvalidMoveException(
                    "Cell is already occupied"
            );
        }
    }

    public void undo() {

        if (moveHistory.isEmpty()) {
            throw new IllegalStateException(
                    "No moves available to undo"
            );
        }

        MoveCommand command = moveHistory.pop();

        command.undo();
    }

    public void rollbackMove(
            Player player,
            int row,
            int col,
            int previousTurnIndex
    ) {

        board.remove(row, col);

        trackerService.undoTrackerMap(
                player.getId(),
                row,
                col
        );

        currentTurnIndex = previousTurnIndex;

        gameStatus = GameStatus.inProgress();
    }

    public GameStatus playBotTurn() {

        if (gameStatus.state() != GameState.IN_PROGRESS) {
            throw new IllegalStateException(
                    "Game has already ended"
            );
        }

        Player currentPlayer = getCurrentPlayer();

        if (!(currentPlayer instanceof BotPlayer bot)) {
            throw new IllegalStateException(
                    "Current player is not a bot"
            );
        }

        Position position = bot.getBotMoveStrategy().getNextMove(board, bot);

        if (position == null) {
            throw new IllegalStateException(
                    "Bot returned no legal move"
            );
        }

        return makeMove(
                bot,
                position.row(),
                position.col()
        );
    }

    public Player getCurrentPlayer() {
        return players.get(currentTurnIndex);
    }

    public Player getPlayerById(String playerId) {
        return playersById.get(playerId);
    }

    public List<Player> getPlayers() {
        return Collections.unmodifiableList(players);
    }

    public Board getBoard() {
        return board;
    }

    public GameStatus getGameStatus() {
        return gameStatus;
    }

    public int getCurrentTurnIndex() {
        return currentTurnIndex;
    }

    public void printBoard() {
        board.printBoard();
    }
}

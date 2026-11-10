package com.shubham.minesweeper;

import com.shubham.minesweeper.entities.Board;
import com.shubham.minesweeper.entities.Cell;
import com.shubham.minesweeper.entities.Game;
import com.shubham.minesweeper.entities.GameConfig;
import com.shubham.minesweeper.entities.RevealResult;
import com.shubham.minesweeper.enums.Difficulty;
import com.shubham.minesweeper.service.DefaultRandomSource;
import com.shubham.minesweeper.service.GameConfigFactory;
import com.shubham.minesweeper.service.RandomMinePlacer;
import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    GameConfig config =
        GameConfigFactory.fromDifficulty(Difficulty.BEGINNER, System.currentTimeMillis());
    Game game = new Game(config, new RandomMinePlacer(new DefaultRandomSource(config.seed())));

    System.out.println(
        "Commands: r <row> <col> reveal | f <row> <col> flag | c <row> <col> chord | q");
    render(game, false);

    Scanner scanner = new Scanner(System.in);
    while (!game.getState().isOver() && scanner.hasNextLine()) {
      String[] parts = scanner.nextLine().trim().toLowerCase().split("\\s+");
      if (parts.length == 1 && parts[0].equals("q")) {
        return;
      }
      if (parts.length != 3) {
        System.out.println("Expected: <r|f|c> <row> <col>");
        continue;
      }

      try {
        int row = Integer.parseInt(parts[1]);
        int col = Integer.parseInt(parts[2]);
        switch (parts[0]) {
          case "r" -> report(game.reveal(row, col));
          case "f" -> game.toggleFlag(row, col);
          case "c" -> report(game.chord(row, col));
          default -> System.out.println("Unknown command " + parts[0]);
        }
      } catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      render(game, game.getState().isOver());
    }

    System.out.println("GAME OVER: " + game.getState());
  }

  private static void report(RevealResult result) {
    System.out.println(
        "Revealed " + result.getRevealedCells().size() + " cell(s), state " + result.getState());
  }

  private static void render(Game game, boolean showMines) {
    Board board = game.getBoard();
    StringBuilder sb = new StringBuilder("   ");
    for (int c = 0; c < board.getCols(); c++) {
      sb.append(c % 10).append(' ');
    }
    sb.append('\n');
    for (int r = 0; r < board.getRows(); r++) {
      sb.append(String.format("%2d ", r));
      for (int c = 0; c < board.getCols(); c++) {
        sb.append(symbol(board.getCell(r, c), showMines)).append(' ');
      }
      sb.append('\n');
    }
    sb.append("Mines left: ").append(game.getRemainingMineCount());
    System.out.println(sb);
  }

  private static char symbol(Cell cell, boolean showMines) {
    if (cell.isFlagged()) {
      return 'F';
    }
    if (cell.isRevealed() || (showMines && cell.isMine())) {
      if (cell.isMine()) {
        return '*';
      }
      return cell.getAdjacentMines() == 0 ? ' ' : (char) ('0' + cell.getAdjacentMines());
    }
    return '#';
  }
}

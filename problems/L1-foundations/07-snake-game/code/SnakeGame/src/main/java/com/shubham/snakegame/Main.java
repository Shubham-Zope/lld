package com.shubham.snakegame;

import com.shubham.snakegame.entities.Board;
import com.shubham.snakegame.entities.Game;
import com.shubham.snakegame.entities.GameConfig;
import com.shubham.snakegame.entities.Position;
import com.shubham.snakegame.entities.TickResult;
import com.shubham.snakegame.enums.Direction;
import com.shubham.snakegame.enums.TickOutcome;
import com.shubham.snakegame.service.DefaultRandomSource;
import com.shubham.snakegame.service.RandomSource;
import com.shubham.snakegame.service.ScoreUpdater;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class Main {
  private static final long TICK_INTERVAL_MS = 400;

  public static void main(String[] args) throws InterruptedException {
    GameConfig config = new GameConfig(10, 10, false, 3, System.currentTimeMillis());
    RandomSource randomSource = new DefaultRandomSource(config.getSeed());
    Game game = new Game(config, randomSource);
    game.addListener(new ScoreUpdater(game.getScore()));

    AtomicBoolean running = new AtomicBoolean(true);
    AtomicReference<Direction> pendingInput = new AtomicReference<>();

    Thread inputThread = new Thread(() -> readInput(running, pendingInput));
    inputThread.setDaemon(true);
    inputThread.start();

    System.out.println(
        "Controls: w/a/s/d to steer, q to quit. Board "
            + config.getWidth()
            + "x"
            + config.getHeight());

    while (running.get()) {
      Direction input = pendingInput.getAndSet(null);
      if (input != null) {
        game.changeDirection(input);
      }

      TickResult result = game.tick();
      render(game);

      if (result != null && result.getOutcome() == TickOutcome.GAME_OVER) {
        System.out.println("GAME OVER: " + result.getReason());
        running.set(false);
        break;
      }

      Thread.sleep(TICK_INTERVAL_MS);
    }

    System.out.println("Final score: " + game.getScore().getScore());
  }

  private static void readInput(AtomicBoolean running, AtomicReference<Direction> pendingInput) {
    Scanner scanner = new Scanner(System.in);
    while (running.get() && scanner.hasNextLine()) {
      String line = scanner.nextLine().trim().toLowerCase();
      switch (line) {
        case "w":
          pendingInput.set(Direction.UP);
          break;
        case "s":
          pendingInput.set(Direction.DOWN);
          break;
        case "a":
          pendingInput.set(Direction.LEFT);
          break;
        case "d":
          pendingInput.set(Direction.RIGHT);
          break;
        case "q":
          running.set(false);
          return;
        default:
          break;
      }
    }
  }

  private static void render(Game game) {
    Board board = game.getBoard();
    Position foodPos = game.getFood().getFood();
    Position head = game.getSnake().getHead();

    StringBuilder sb = new StringBuilder();
    for (int y = 0; y < board.getHeight(); y++) {
      for (int x = 0; x < board.getWidth(); x++) {
        if (x == head.getX() && y == head.getY()) {
          sb.append('O');
        } else if (board.getCell(x, y).isSnakePresent()) {
          sb.append('o');
        } else if (x == foodPos.getX() && y == foodPos.getY()) {
          sb.append('*');
        } else {
          sb.append('.');
        }
      }
      sb.append('\n');
    }
    sb.append("Score: ").append(game.getScore().getScore());

    System.out.println(sb);
  }
}

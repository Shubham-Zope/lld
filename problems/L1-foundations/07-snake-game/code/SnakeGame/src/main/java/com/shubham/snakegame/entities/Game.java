package com.shubham.snakegame.entities;

import com.shubham.snakegame.enums.Direction;
import com.shubham.snakegame.enums.GameReason;
import com.shubham.snakegame.enums.GameState;
import com.shubham.snakegame.rules.AllCellsFilledRule;
import com.shubham.snakegame.rules.BoundaryRule;
import com.shubham.snakegame.rules.SnakeSelfEatRule;
import com.shubham.snakegame.rules.WallBoundary;
import com.shubham.snakegame.rules.WrapBoundary;
import com.shubham.snakegame.service.FoodSpawner;
import com.shubham.snakegame.service.GameListener;
import com.shubham.snakegame.service.RandomSource;
import java.util.ArrayList;
import java.util.List;

public class Game {
  private final Board board;
  private final Snake snake;
  private final Score score;
  private final List<GameListener> listeners;
  private final BoundaryRule boundaryRule;
  private final SnakeSelfEatRule selfEatRule;
  private final AllCellsFilledRule allCellsFilledRule;
  private final FoodSpawner foodSpawner;

  private final Food food;
  private Direction currentDirection;
  private Direction pendingDirection;
  private GameState state;

  public Game(GameConfig config, RandomSource randomSource) {
    this.board = new Board(config.getWidth(), config.getHeight());
    this.snake = new Snake();
    this.score = new Score();
    this.listeners = new ArrayList<>();
    this.boundaryRule = config.isWrapAround() ? new WrapBoundary() : new WallBoundary();
    this.selfEatRule = new SnakeSelfEatRule();
    this.allCellsFilledRule = new AllCellsFilledRule();
    this.foodSpawner = new FoodSpawner(randomSource);
    this.currentDirection = Direction.RIGHT;
    this.pendingDirection = Direction.RIGHT;
    this.state = GameState.RUNNING;

    int row = config.getHeight() / 2;
    for (int x = 0; x < config.getStartingSnakeLength(); x++) {
      Position segment = board.getCell(x, row);
      snake.addHead(segment);
      board.markSnake(segment.getX(), segment.getY());
    }

    this.food = new Food(foodSpawner.spawn(board));
  }

  public void addListener(GameListener listener) {
    listeners.add(listener);
  }

  public void changeDirection(Direction direction) {
    if (!state.canChangeDirection()) {
      return;
    }

    if (direction == currentDirection.opposite()) {
      return;
    }

    this.pendingDirection = direction;
  }

  public TickResult tick() {
    if (!state.canTick()) {
      return null;
    }

    this.currentDirection = pendingDirection;

    Position head = snake.getHead();
    Position candidate =
        new Position(
            head.getX() + currentDirection.getDx(), head.getY() + currentDirection.getDy());
    Position resolved = boundaryRule.resolve(candidate, board);

    if (resolved == null) {
      this.state = GameState.OVER;
      notifyGameOver(GameReason.WALL);
      return TickResult.gameOver(GameReason.WALL);
    }

    Position next = board.getCell(resolved.getX(), resolved.getY());
    boolean willEat = next.getX() == food.getFood().getX() && next.getY() == food.getFood().getY();

    if (selfEatRule.isSelfCollision(next, snake, willEat)) {
      this.state = GameState.OVER;
      notifyGameOver(GameReason.SELF);
      return TickResult.gameOver(GameReason.SELF);
    }

    if (!willEat) {
      Position tail = snake.getTail();
      board.unmarkSnake(tail.getX(), tail.getY());
      snake.removeTail();
    }

    snake.addHead(next);
    board.markSnake(next.getX(), next.getY());

    if (!willEat) {
      return TickResult.moved();
    }

    notifyFoodEaten();

    if (allCellsFilledRule.isBoardFull(snake, board)) {
      this.state = GameState.OVER;
      notifyGameOver(GameReason.BOARD_FULL_WIN);
      return TickResult.gameOver(GameReason.BOARD_FULL_WIN);
    }

    food.setFood(foodSpawner.spawn(board));
    notifyFoodSpawned();

    return TickResult.ate();
  }

  public Board getBoard() {
    return board;
  }

  public Snake getSnake() {
    return snake;
  }

  public Food getFood() {
    return food;
  }

  public Score getScore() {
    return score;
  }

  public GameState getState() {
    return state;
  }

  public Direction getCurrentDirection() {
    return currentDirection;
  }

  private void notifyFoodEaten() {
    for (GameListener listener : listeners) {
      listener.onFoodEaten();
    }
  }

  private void notifyFoodSpawned() {
    for (GameListener listener : listeners) {
      listener.onFoodSpawned();
    }
  }

  private void notifyGameOver(GameReason reason) {
    for (GameListener listener : listeners) {
      listener.onGameOver(reason);
    }
  }
}

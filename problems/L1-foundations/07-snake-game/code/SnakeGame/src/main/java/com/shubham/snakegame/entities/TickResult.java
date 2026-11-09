package com.shubham.snakegame.entities;

import com.shubham.snakegame.enums.GameReason;
import com.shubham.snakegame.enums.TickOutcome;

public class TickResult {
  private final TickOutcome outcome;
  private final GameReason reason;

  private TickResult(TickOutcome outcome, GameReason reason) {
    this.outcome = outcome;
    this.reason = reason;
  }

  public static TickResult moved() {
    return new TickResult(TickOutcome.MOVED, null);
  }

  public static TickResult ate() {
    return new TickResult(TickOutcome.ATE, null);
  }

  public static TickResult gameOver(GameReason reason) {
    return new TickResult(TickOutcome.GAME_OVER, reason);
  }

  public TickOutcome getOutcome() {
    return outcome;
  }

  public GameReason getReason() {
    return reason;
  }
}

package com.shubham.snakegame.enums;

public enum GameState {
  RUNNING {
    @Override
    public boolean canTick() {
      return true;
    }

    @Override
    public boolean canChangeDirection() {
      return true;
    }
  },
  PAUSED {
    @Override
    public boolean canTick() {
      return false;
    }

    @Override
    public boolean canChangeDirection() {
      return true;
    }
  },
  OVER {
    @Override
    public boolean canTick() {
      return false;
    }

    @Override
    public boolean canChangeDirection() {
      return false;
    }
  };

  public abstract boolean canTick();

  public abstract boolean canChangeDirection();
}

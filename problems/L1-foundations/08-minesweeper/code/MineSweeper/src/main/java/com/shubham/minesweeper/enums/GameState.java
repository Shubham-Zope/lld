package com.shubham.minesweeper.enums;

public enum GameState {
  READY {
    @Override
    public boolean canReveal() {
      return true;
    }

    @Override
    public boolean canFlag() {
      return true;
    }
  },
  PLAYING {
    @Override
    public boolean canReveal() {
      return true;
    }

    @Override
    public boolean canFlag() {
      return true;
    }
  },
  WON {
    @Override
    public boolean canReveal() {
      return false;
    }

    @Override
    public boolean canFlag() {
      return false;
    }
  },
  LOST {
    @Override
    public boolean canReveal() {
      return false;
    }

    @Override
    public boolean canFlag() {
      return false;
    }
  };

  public abstract boolean canReveal();

  public abstract boolean canFlag();

  public boolean isOver() {
    return this == WON || this == LOST;
  }
}

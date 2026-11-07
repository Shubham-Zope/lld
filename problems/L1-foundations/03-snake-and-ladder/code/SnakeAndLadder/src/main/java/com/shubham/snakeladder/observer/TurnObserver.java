package com.shubham.snakeladder.observer;

import com.shubham.snakeladder.game.TurnResult;

public interface TurnObserver {

    void onTurn(TurnResult result);
}
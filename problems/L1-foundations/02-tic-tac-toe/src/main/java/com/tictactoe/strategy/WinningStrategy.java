package com.tictactoe.strategy;

import com.tictactoe.models.Player;
import com.tictactoe.service.TrackerService;

public interface WinningStrategy {
    boolean isWinningMove(
        Player player,
        int row,
        int col,
        TrackerService trackerService
    );
}

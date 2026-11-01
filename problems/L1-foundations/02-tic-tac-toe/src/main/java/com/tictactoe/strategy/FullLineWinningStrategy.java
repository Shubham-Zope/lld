package com.tictactoe.strategy;

import com.tictactoe.models.Player;
import com.tictactoe.service.TrackerService;

public class FullLineWinningStrategy implements WinningStrategy {

    @Override
    public boolean isWinningMove(
            Player player,
            int row,
            int col,
            TrackerService trackerService
    ) {
        return trackerService.hasWon(
            player.getId(), 
            row, 
            col
        );
    }
}

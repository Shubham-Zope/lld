package com.tictactoe.service;

import java.util.HashMap;
import java.util.Map;

public class TrackerService {
    public static class PlayerTracker {
        int[] rows;
        int[] cols;

        int mainDiagonal;
        int antiDiagonal;

        public PlayerTracker(int size) {
            rows = new int[size];
            cols = new int[size];
            mainDiagonal = 0;
            antiDiagonal = 0;
        }
    }

    private final int size;
    private final Map<String, PlayerTracker> trackerMap = new HashMap<>();

    public TrackerService(int size) {
        this.size = size;
    }

    /**
     * Registers a player in the tracker map.
     * @param playerId
     */
    public void registerPlayer(String playerId) {
        trackerMap.put(playerId, new PlayerTracker(size));
    }

    /**
     * Updates the tracker for a given player based on their move.
     * @param playerId
     * @param row
     * @param col
     * @return
     */
    public boolean updateTrackerMap(String playerId, int row, int col) {
        PlayerTracker tracker = trackerMap.get(playerId);
        if (tracker == null) {
            throw new IllegalArgumentException("Player not registered: " + playerId);
        }

        tracker.rows[row]++;
        tracker.cols[col]++;

        if (row == col) {
            tracker.mainDiagonal++;
        }

        if (row + col == size - 1) {
            tracker.antiDiagonal++;
        }

        return hasWon(playerId, row, col);
    }

    /**
     * Checks if the player has won after their move.
     * @param playerId
     * @param row
     * @param col
     * @return
     */
    public boolean hasWon(
            String playerId,
            int row,
            int col
    ) {
        PlayerTracker tracker = trackerMap.get(playerId);

        return tracker.rows[row] == size
                || tracker.cols[col] == size
                || (row == col && tracker.mainDiagonal == size)
                || (row + col == size - 1
                    && tracker.antiDiagonal == size);
    }

    /**
     * Undoes the last move made by the player, updating the tracker accordingly.
     * @param playerId
     * @param row
     * @param col
     */
    public void undoTrackerMap(
            String playerId,
            int row,
            int col
    ) {
        PlayerTracker tracker = trackerMap.get(playerId);

        tracker.rows[row]--;
        tracker.cols[col]--;

        if (row == col) {
            tracker.mainDiagonal--;
        }

        if (row + col == size - 1) {
            tracker.antiDiagonal--;
        }
    }
}

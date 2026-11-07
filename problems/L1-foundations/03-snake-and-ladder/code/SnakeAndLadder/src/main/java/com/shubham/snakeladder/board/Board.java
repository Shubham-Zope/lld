package com.shubham.snakeladder.board;

import java.util.List;
import java.util.Map;
import java.util.ArrayList;

public class Board {
    private final int size;
    private final Map<Integer, Integer> jumps;

    Board(int size, Map<Integer, Integer> jumps) {
        this.size = size;
        this.jumps = jumps;
    }

    public int getSize() {
        return size;
    }

    public boolean hasJump(int position) {
        return jumps.containsKey(position);
    }

    public BoardResolution resolvePosition(int position) {
        int current = position;

        List<Jump> takenJumps = new ArrayList<>();

        while (jumps.containsKey(current)) {
            int destination = jumps.get(current);
            takenJumps.add(new Jump(current, destination));
            current = destination;
        }

        return new BoardResolution(current, List.copyOf(takenJumps));
    }
}
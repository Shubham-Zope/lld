package com.shubham.snakeladder.board;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class BoardBuilder {
    private final int size;
    private final Map<Integer, Integer> jumps = new HashMap<>();

    public BoardBuilder(int size) {
        if (size < 25 || size > 400) {
            throw new IllegalArgumentException(
                    "Board size must be between 25 and 400"
            );
        }

        this.size = size;
    }

    public BoardBuilder addSnake(int head, int tail) {
        validateJump(head, tail);

        if (head <= tail) {
            throw new IllegalArgumentException(
                    "Snake head must be greater than its tail"
            );
        }

        jumps.put(head, tail);
        return this;
    }

    public BoardBuilder addLadder(int start, int end) {

        validateJump(start, end);

        if (start >= end) {
            throw new IllegalArgumentException(
                    "Ladder start must be smaller than its end"
            );
        }

        jumps.put(start, end);

        return this;
    }

    private void validateJump(int from, int to) {

        if (from <= 1 || from >= size ||
                to <= 1 || to >= size) {

            throw new IllegalArgumentException(
                    "Jump cannot involve starting or ending cell"
            );
        }

        if (jumps.containsKey(from)) {
            throw new IllegalArgumentException(
                    "Cell already has a jump: " + from
            );
        }
    }

    public Board build() {
        validateNoCycles();

        return new Board(size, jumps);
    }

    private void validateNoCycles() {
        Set<Integer> visited = new HashSet<>();

        for (int start : jumps.keySet()) {
            Set<Integer> path = new HashSet<>();

            int current = start;

            while (jumps.containsKey(current)) {

                if (path.contains(current)) {
                    throw new IllegalArgumentException(
                            "Cycle detected at cell " + current
                    );
                }

                if (visited.contains(current)) {
                    break;
                }

                path.add(current);

                current = jumps.get(current);
            }

            visited.addAll(path);
        }
    }
}
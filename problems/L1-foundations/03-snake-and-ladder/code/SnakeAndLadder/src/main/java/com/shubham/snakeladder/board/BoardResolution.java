package com.shubham.snakeladder.board;

import java.util.List;

public record BoardResolution(
        int finalPosition,
        List<Jump> jumps
) {
}
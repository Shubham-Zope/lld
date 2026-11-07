package com.shubham.snakeladder.model;

public class Player {

    private final String name;

    private int position;

    private int consecutiveSixes;

    private int rank;

    public Player(String name) {

        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    "Player name cannot be empty"
            );
        }

        this.name = name;
        this.position = 1;
        this.consecutiveSixes = 0;
        this.rank = 0;
    }

    public String getName() {
        return name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getConsecutiveSixes() {
        return consecutiveSixes;
    }

    public void setConsecutiveSixes(int consecutiveSixes) {
        this.consecutiveSixes = consecutiveSixes;
    }

    public int getRank() {
        return rank;
    }

    public void finish(int rank) {
        this.rank = rank;
    }

    public boolean hasFinished() {
        return rank > 0;
    }
}
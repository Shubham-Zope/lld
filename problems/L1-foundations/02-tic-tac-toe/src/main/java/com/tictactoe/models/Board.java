package com.tictactoe.models;

public class Board {

    private final int size;
    private final char[][] grid;
    private int filledCells;

    public Board(int size) {
        if (size < 3 || size > 100) {
            throw new IllegalArgumentException(
                    "Board size must be between 3 and 100"
            );
        }


        this.size = size;
        this.grid = new char[size][size];
        this.filledCells = 0;

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '\0';
            }
        }
    }

    /**
     * 
     * @return
     */
    public int getSize() {
        return size;
    }

    /**
     * 
     * @param row
     * @param col
     * @return
     */
    public char getCell(int row, int col) {
        return grid[row][col];
    }

    /**
     * 
     * @param row
     * @param col
     * @return
     */
    public boolean isEmpty(int row, int col) {
        return grid[row][col] == '\0';
    }

    /**
     * 
     * @param row
     * @param col
     * @param symbol
     */
    public void place(int row, int col, char symbol) {
        grid[row][col] = symbol;
        filledCells++;
    }

    /**
     * 
     * @param row
     * @param col
     */
    public void remove(int row, int col) {
        grid[row][col] = '\0';
        filledCells--;
    }

    /**
     * 
     * @return
     */
    public boolean isFull() {
        return filledCells == size * size;
    }

    /**
     * 
     */
    public void printBoard() {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(
                        grid[i][j] == '\0' ? ". " : grid[i][j] + " "
                );
            }
            System.out.println();
        }
    }
}

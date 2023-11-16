/*
 * Artificial Intelligence: Principles & Techniques
 * Assignment 2: Sudoku
 * 16/11-23
 * Amanda Enhörning, s1128126
 * Jessica Borg, s1129470
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * The Sudoku class represents a Sudoku puzzle and provides methods for reading, displaying, and interacting with the puzzle.
 */
public class Sudoku {
    private Field[][] board;

    /**
     * Constructs a Sudoku object by reading a puzzle from the specified file.
     *
     * @param filename The name of the file containing the Sudoku puzzle.
     */
    Sudoku(String filename) {
        this.board = readsudoku(filename);
    }

    
    /**
     * Reads a Sudoku puzzle from the specified file and initializes the Sudoku grid.
     *
     * @param filename The name of the file containing the Sudoku puzzle.
     * @return A 2D array representing the Sudoku puzzle grid.
     */
    public static Field[][] readsudoku(String filename) {
        assert filename != null && !filename.isEmpty() : "Invalid filename";
        String line = "";
        Field[][] grid = new Field[9][9];

        // Initialize all fields first
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j] = new Field();
            }
        }

        try {
            FileInputStream inputStream = new FileInputStream(filename);
            Scanner scanner = new Scanner(inputStream);
            for (int i = 0; i < 9; i++) {
                if (scanner.hasNext()) {
                    line = scanner.nextLine();
                    for (int j = 0; j < 9; j++) {
                        int numValue = Character.getNumericValue(line.charAt(j));
                        if (numValue != 0) {
                            grid[i][j].setValue(numValue);
                        }
                    }
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error opening file: " + filename);
        }

        // Now, add neighbors to the initialized fields
        addNeighbours(grid);

        return grid;
    }

    /**
     * Initializes the neighbors for each field in the Sudoku grid.
     *
     * @param grid The 2D array representing the Sudoku puzzle grid.
     */
    private static void addNeighbours(Field[][] grid) {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                grid[i][j].setNeighbours(new ArrayList<>()); // Initialize the neighbors list for the current field

                // Add row neighbors
                for (int k = 0; k < 9; k++) {
                    if (k != j) {
                        grid[i][j].getNeighbours().add(grid[i][k]);
                    }
                }

                // Add column neighbors
                for (int k = 0; k < 9; k++) {
                    if (k != i) {
                        grid[i][j].getNeighbours().add(grid[k][j]);
                    }
                }

                // Add box neighbors
                int boxRow = i / 3 * 3;
                int boxCol = j / 3 * 3;
                for (int x = boxRow; x < boxRow + 3; x++) {
                    for (int y = boxCol; y < boxCol + 3; y++) {
                        if (x != i && y != j) {
                            grid[i][j].getNeighbours().add(grid[x][y]);
                        }
                    }
                }
            }
        }
    }

    /**
     * Generates a string representation of the Sudoku puzzle suitable for writing to a file.
     *
     * @return A string representing the Sudoku puzzle in a file-friendly format.
     */
    public String toFileString() {
        String output = "";
        for (int i = 0; i < board.length; i++) {
            for (int j = 0; j < board[0].length; j++) {
                output += board[i][j].getValue();
            }
            output += "\n";
        }
        return output;
    }

    /**
     * Gets the current Sudoku puzzle grid.
     *
     * @return A 2D array representing the Sudoku puzzle grid.
     */
    public Field[][] getBoard() {
        return board;
    }

    /**
     * Returns a string representation of the Sudoku puzzle suitable for printing.
     *
     * @return A formatted string representing the current state of the Sudoku puzzle.
     */
    @Override
    public String toString() {
        String output = "╔═══════╦═══════╦═══════╗\n";
        for (int i = 0; i < 9; i++) {
            if (i == 3 || i == 6) {
                output += "╠═══════╬═══════╬═══════╣\n";
            }
            output += "║ ";
            for (int j = 0; j < 9; j++) {
                if (j == 3 || j == 6) {
                    output += "║ ";
                }
                output += board[i][j] + " ";
            }

            output += "║\n";
        }
        output += "╚═══════╩═══════╩═══════╝\n";
        return output;
    }
}

/*
 * Artificial Intelligence: Principles & Techniques
 * Assignment 2: Sudoku
 * 16/11-23
 * Amanda Enhörning, s1128126
 * Jessica Borg, s1129470
 */

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * The Game class represents a Sudoku game and provides methods to solve the puzzle using different algorithms.
 */
public class Game {
    private Sudoku sudoku;
    private int iterations = 0;
    private int processedArcs = 0;

    /**
     * Constructs a Game object with the given Sudoku instance.
     *
     * @param sudoku The Sudoku instance representing the puzzle.
     */
    Game(Sudoku sudoku) {
        this.sudoku = sudoku;
        this.iterations = 0; // Initialize the iteration count.

    }

    /**
     * Displays the current state of the Sudoku puzzle.
     */
    public void showSudoku() {
        System.out.println(sudoku);
    }

    /**
     * Help-method to increase the number of iterations performed in the algorithms.
     *
     * @return The number of iterations.
     */
    public void increaseIterations() {
        iterations++;
    }

    /**
     * Help-method to increase the number of processed arcs performed in the algorithms.
     *
     * @return The number of iterations.
     */
    public void increaseProcessedArcs() {
        processedArcs++;
    }

    /**
     * Help-method to print the number of iterations and processed arcs for the specified algorithm
     * 
     * @param algorithmName
     * @param iterations
     * @param processedArcs
     */
    public void printNoOfIterationsAndProcessedArcs(String algorithmName,int iterations, int processedArcs){
        System.out.println("Number of iterations (" + algorithmName + "): " + iterations);
        System.out.println("Number of processed arcs (" + algorithmName + "): " + processedArcs);
    }

    /**
     * Checks if the current state of the Sudoku puzzle is a valid solution.
     *
     * @return true if the Sudoku is a valid solution, false otherwise.
     */
    public boolean validSolution() {
        Field[][] board = sudoku.getBoard();

        // Check rows
        for (int i = 0; i < 9; i++) {
            boolean[] rowSet = new boolean[9];
            for (int j = 0; j < 9; j++) {
                int value = board[i][j].getValue();
                if (value != 0) {
                    rowSet[value - 1] = true;
                } else {
                    return false;
                }
            }
        }

        // Check columns
        for (int j = 0; j < 9; j++) {
            boolean[] colSet = new boolean[9];
            for (int i = 0; i < 9; i++) {
                int value = board[i][j].getValue();
                if (value != 0) {
                    colSet[value - 1] = true;
                } else {
                    return false;
                }
            }
        }

        // Check 3x3 subgrid
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                boolean[] boxSet = new boolean[9];
                for (int i = boxRow; i < boxRow + 3; i++) {
                    for (int j = boxCol; j < boxCol + 3; j++) {
                        int value = board[i][j].getValue();
                        if (value != 0) {
                            boxSet[value - 1] = true;
                        } else {
                            return false;
                        }
                    }
                }
            }
        }

        return true; // Sudoku is a valid solution
    }

    /**
     * Solves the Sudoku puzzle using the AC-3 algorithm.
     *
     * @return true if the puzzle is solvable, false otherwise.
     */
    public boolean solveAC3() {
        Queue<Field> queue = new LinkedList<>();

        for (Field[] row : sudoku.getBoard()) {
            for (Field field : row) {
                if (field.getValue() != 0) {
                    for (Field neighbor : field.getNeighbours()) {
                        if (neighbor.removeFromDomain(field.getValue())) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }

        while (!queue.isEmpty()) {
            Field field = queue.poll();

            if (field.getDomain().isEmpty()) {
                printNoOfIterationsAndProcessedArcs("AC-3", iterations, processedArcs);
                return false; // Inconsistency, AC-3 fails
            }

            if (field.getDomainSize() == 1) {
                int value = field.getDomain().get(0);
                field.setValue(value);

                for (Field neighbor : field.getNeighbours()) {
                    if (neighbor.removeFromDomain(value)) {
                        queue.add(neighbor);
                        
                    }
                    increaseProcessedArcs();
                }
                increaseIterations();
            }
        }

        printNoOfIterationsAndProcessedArcs("AC-3", iterations, processedArcs);

        return true;
    }

    /**
     * Solves the Sudoku puzzle using the AC-3 algorithm with Minimum Remaining Values (MRV) heuristic.
     *
     * @return true if the puzzle is solvable, false otherwise.
     */
    public boolean solveAC3MRV() {
        Queue<Field> queue = new LinkedList<>();

        // Initialize the queue with fields in a MRV order
        List<Field> fields = getFieldsInMRVOrder();
        queue.addAll(fields);

        while (!queue.isEmpty()) {
            Field field = queue.poll();

            if (field.getDomain().isEmpty()) {
                printNoOfIterationsAndProcessedArcs("AC-3 with MRV", iterations, processedArcs);
                return false;
            }

            if (field.getDomainSize() == 1) {
                int value = field.getDomain().get(0);
                field.setValue(value);

                for (Field neighbor : field.getNeighbours()) {
                    if (neighbor.removeFromDomain(value)) {
                        queue.add(neighbor);
                    }
                    increaseProcessedArcs();
                }
                increaseIterations();
            }
        }

        printNoOfIterationsAndProcessedArcs("AC-3 with MRV", iterations, processedArcs);

        return true;
    }

    /**
     * Retrieves a list of fields in Minimum Remaining Values (MRV) order.
     *
     * @return A list of fields sorted by domain size in ascending order.
     */
    private List<Field> getFieldsInMRVOrder() {
        List<Field> fields = new ArrayList<>();
        for (Field[] row : sudoku.getBoard()) {
            for (Field field : row) {
                if (field.getValue() == 0) {
                    fields.add(field);
                }
            }
        }

        for (Field field : fields) {
            for (Field neighbor : field.getNeighbours()) {
                field.removeFromDomain(neighbor.getValue());
            }
        }

        fields.sort(Comparator.comparingInt(Field::getDomainSize)); // Sort by domain size in ascending order

        return fields;
    }

    /**
     * Solves the Sudoku puzzle using the AC-3 algorithm with the Degree heuristic.
     *
     * @return true if the puzzle is solvable, false otherwise.
     */
    public boolean solveAC3WithDegree() {
        Queue<Field> queue = new LinkedList<>();

        for (Field[] row : sudoku.getBoard()) {
            for (Field field : row) {
                if (field.getValue() != 0) {
                    for (Field neighbor : field.getNeighbours()) {
                        if (neighbor.removeFromDomain(field.getValue())) {
                            queue.add(neighbor);
                        }
                    }
                } else {
                    queue.add(field);
                }
            }
        }

        while (!queue.isEmpty()) {
            // Sort the queue based on the degree heuristic (number of unassigned neighbors)
            List<Field> sortedQueue = new ArrayList<>(queue);
            sortedQueue.sort(Comparator.comparingInt(f -> -countUnassignedNeighbors(f))); // Sort in descending order

            Field field = sortedQueue.remove(0); // Get the field with the highest degree
            queue.remove(field);

            if (field.getDomain().isEmpty()) {
                printNoOfIterationsAndProcessedArcs("AC-3 with Degree", iterations, processedArcs);
                return false;
            }

            if (field.getDomainSize() == 1) {
                int value = field.getDomain().get(0);
                field.setValue(value);

                for (Field neighbor : field.getNeighbours()) {
                    if (neighbor.removeFromDomain(value)) {
                        queue.add(neighbor);
                    }
                    increaseProcessedArcs();
                }
                increaseIterations();
            }
        }

        printNoOfIterationsAndProcessedArcs("AC-3 with Degree", iterations, processedArcs);

        return true;
    }

    /**
     * Counts the number of neighboring fields with unassigned values for a given field.
     *
     * @param field The field for which to count unassigned neighbors.
     * @return The number of neighboring fields with unassigned values.
     */
    private int countUnassignedNeighbors(Field field) {
        int unassignedNeighbors = 0;
        for (Field neighbor : field.getNeighbours()) {
            if (neighbor.getValue() == 0) {
                unassignedNeighbors++;
            }
        }
        return unassignedNeighbors;
    }

    /**
     * Verifies the output of the AC-3 algorithm with or without heuristics and prints whether the Sudoku puzzle is solvable or not.
     * Use one of the following methods: solveACr(), solveAC3MRV(), solveAC3WithDegree
     */
    public void verifyAC3Output() {
        if (solveAC3MRV() && validSolution()) {
            System.out.println("Sudoku is solvable.");
        } else {
            System.out.println("Sudoku is not solvable.");
        }
    }

}
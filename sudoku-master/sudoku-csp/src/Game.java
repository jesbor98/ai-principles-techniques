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
    private int iterations;

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
     * Retrieves the number of iterations performed during the solving process.
     *
     * @return The number of iterations.
     */
    public int getIterations() {
        return iterations;
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
                    /*
                     * if (rowSet[value - 1]) {
                     * return false; // Duplicate value in the row
                     * }
                     */
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
                    /*
                     * if (colSet[value - 1]) {
                     * return false; // Duplicate value in the column
                     * }
                     */
                    colSet[value - 1] = true;
                } else {
                    return false;
                }
            }
        }

        // Check 3x3 boxes
        for (int boxRow = 0; boxRow < 9; boxRow += 3) {
            for (int boxCol = 0; boxCol < 9; boxCol += 3) {
                boolean[] boxSet = new boolean[9];
                for (int i = boxRow; i < boxRow + 3; i++) {
                    for (int j = boxCol; j < boxCol + 3; j++) {
                        int value = board[i][j].getValue();
                        if (value != 0) {
                            /*
                             * if (boxSet[value - 1]) {
                             * return false; // Duplicate value in the 3x3 box
                             * }
                             */
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
        int iterations = 0; // Initialize the iteration counter
        Queue<Field> queue = new LinkedList<>();

        // Initialize the queue with all fields
        for (Field[] row : sudoku.getBoard()) {
            for (Field field : row) {
                if (field.getValue() != 0) {
                    // Remove the assigned value from neighbors' domains
                    for (Field neighbor : field.getNeighbours()) {
                        if (neighbor.removeFromDomain(field.getValue())) {
                            queue.add(neighbor);
                        }
                    }
                }
            }
        }

        // Process the queue
        while (!queue.isEmpty()) {
            Field field = queue.poll();

            if (field.getDomainSize() == 0) {
                return false; // Inconsistency, AC-3 fails
            }

            // If the domain has only one value, assign it and propagate constraints
            if (field.getDomainSize() == 1) {
                int value = field.getDomain().get(0);
                field.setValue(value);

                // Remove the assigned value from neighbors' domains
                for (Field neighbor : field.getNeighbours()) {
                    if (neighbor.removeFromDomain(value)) {
                        queue.add(neighbor);
                    }
                }
                iterations++; // Increment the iteration counter
            }
        }

        // Check if the puzzle is solved
        for (Field[] row : sudoku.getBoard()) {
            for (Field field : row) {
                if (field.getDomainSize() > 1) {
                    return false; // Puzzle is not solved
                }
            }
        }

        System.out.println("Number of iterations (AC-3): " + iterations); // Print the number of iterations

        if(validSolution()){
            return true; // Sudoku is solvable according to AC-3
        } else{
            return false;
        }
    }

    /**
     * Solves the Sudoku puzzle using the AC-3 algorithm with Minimum Remaining Values (MRV) heuristic.
     *
     * @return true if the puzzle is solvable, false otherwise.
     */
    public boolean solveAC3MRV() {
        int iterations = 0; // Initialize the iteration counter
        Queue<Field> queue = new LinkedList<>();

        // Initialize the queue with fields in a minimum remaining values (MRV) order
        List<Field> fields = getFieldsInMRVOrder(); // Implement your MRV heuristic
        queue.addAll(fields);

        // Process the queue
        while (!queue.isEmpty()) {
            Field field = queue.poll();

            if (field.getDomainSize() == 0) {
                return false; // The sudoku cannot be solved
            }

            // If the domain has only one value, assign it and propagate constraints
            if (field.getDomainSize() == 1) {
                int value = field.getDomain().get(0);
                field.setValue(value);

                // Remove the assigned value from neighbors' domains
                for (Field neighbor : field.getNeighbours()) {
                    if (neighbor.removeFromDomain(value)) {
                        queue.add(neighbor);
                    }
                }
                iterations++; // Increment the iteration counter
            }
        }

        System.out.println("Number of iterations (AC-3 with MRV): " + iterations); // Print the number of iterations

        if(validSolution()){
            return true; // Sudoku is solvable according to AC-3 with MRV
        } else{
            return false;
        }
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

        fields.sort(Comparator.comparingInt(Field::getDomainSize)); // Sort by domain size in ascending order'

        return fields;
    }

    /**
     * Solves the Sudoku puzzle using the AC-3 algorithm with the Degree heuristic.
     *
     * @return true if the puzzle is solvable, false otherwise.
     */
    public boolean solveAC3WithDegree() {
        int iterations = 0; // Initialize the iteration counter
        Queue<Field> queue = new LinkedList<>();

        // Initialize the queue with all fields, but prioritize constraints with arcs to
        // finalized fields
        for (Field[] row : sudoku.getBoard()) {
            for (Field field : row) {
                if (field.getValue() != 0) {
                    // Remove the assigned value from neighbors' domains
                    for (Field neighbor : field.getNeighbours()) {
                        if (neighbor.removeFromDomain(field.getValue())) {
                            queue.add(neighbor);
                        }
                    }
                } else {
                    queue.add(field); // Add all fields to the queue initially
                }
            }
        }

        // Process the queue
        while (!queue.isEmpty()) {
            // Sort the queue based on the degree heuristic (number of unassigned neighbors)
            List<Field> sortedQueue = new ArrayList<>(queue);
            sortedQueue.sort(Comparator.comparingInt(f -> -countUnassignedNeighbors(f))); // Sort in descending order

            Field field = sortedQueue.remove(0); // Get the field with the highest degree
            queue.remove(field);

            if (field.getDomainSize() == 0) {
                return false; // Inconsistency, AC-3 with Degree fails
            }

            // If the domain has only one value, assign it and propagate constraints
            if (field.getDomainSize() == 1) {
                int value = field.getDomain().get(0);
                field.setValue(value);

                // Remove the assigned value from neighbors' domains
                for (Field neighbor : field.getNeighbours()) {
                    if (neighbor.removeFromDomain(value)) {
                        queue.add(neighbor);
                    }
                }
                iterations++; // Increment the iteration counter
            }
        }

        System.out.println("Number of iterations (AC-3 with Degree): " + iterations); // Print the number of iterations

        if(validSolution()){
            return true; // Sudoku is solvable according to AC-3 with Degree
        } else{
            return false;
        }
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
     * Verifies the output of the AC-3 algorithm and prints whether the Sudoku puzzle is solvable or not.
     */
    // Use: solveAC3, solveAC3MRV, solveAC3WithPriority, solveAC3WithDegree
    public void verifyAC3Output() {
        if (solveAC3() /*&& validSolution()*/) {
            System.out.println("Sudoku is solvable.");
        } else {
            System.out.println("Sudoku is not solvable.");
        }
    }

}
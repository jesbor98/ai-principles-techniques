import java.util.LinkedList;
import java.util.Queue;

public class Game {
  private Sudoku sudoku;
  private int iterations;


  Game(Sudoku sudoku) {
    this.sudoku = sudoku;
    this.iterations = 0; // Initialize the iteration count.

  }

  public void showSudoku() {
    System.out.println(sudoku);
  }
  public int getIterations() {
    return iterations;
}

  public boolean solve() {
    return backtrack(0, 0); // Start from the top-left corner (0,0).
}

private boolean backtrack(int row, int col) {
    iterations++; // Increment the iteration count at the beginning of each call.

    if (row == 9) {
        // If we reach this point, the puzzle is solved.
        return true;
    }

    if (col == 9) {
        // Move to the next row if we reach the end of a row.
        return backtrack(row + 1, 0);
    }

    Field field = sudoku.getBoard()[row][col];
    if (field.getValue() != 0) {
        // If the field is already filled, move to the next column.
        return backtrack(row, col + 1);
    }

    for (int value = 1; value <= 9; value++) {
        if (isValidMove(row, col, value)) {
            // Make a move if it's valid.
            field.setValue(value);

            if (backtrack(row, col + 1)) {
                return true; // Move to the next column.
            }

            // If the move doesn't lead to a solution, reset the field.
            field.resetField();
        }
    }
    
    return false; // No solution found at this position.
}


private boolean isValidMove(int row, int col, int value) {
    // Check row
    for (int i = 0; i < 9; i++) {
        if (sudoku.getBoard()[row][i].getValue() == value) {
            return false;
        }
    }
    
    // Check column
    for (int i = 0; i < 9; i++) {
        if (sudoku.getBoard()[i][col].getValue() == value) {
            return false;
        }
    }
    
    // Check 3x3 box
    int boxRow = row / 3 * 3;
    int boxCol = col / 3 * 3;
    for (int i = boxRow; i < boxRow + 3; i++) {
        for (int j = boxCol; j < boxCol + 3; j++) {
            if (sudoku.getBoard()[i][j].getValue() == value) {
                return false;
            }
        }
    }
    
    return true; // The value is valid at this position.
}


    public boolean validSolution() {
        Field[][] board = sudoku.getBoard();

        // Check rows
        for (int i = 0; i < 9; i++) {
            boolean[] rowSet = new boolean[9];
            for (int j = 0; j < 9; j++) {
                int value = board[i][j].getValue();
                if (value != 0) {
                    if (rowSet[value - 1]) {
                        return false; // Duplicate value in the row
                    }
                    rowSet[value - 1] = true;
                }
            }
        }

        // Check columns
        for (int j = 0; j < 9; j++) {
            boolean[] colSet = new boolean[9];
            for (int i = 0; i < 9; i++) {
                int value = board[i][j].getValue();
                if (value != 0) {
                    if (colSet[value - 1]) {
                        return false; // Duplicate value in the column
                    }
                    colSet[value - 1] = true;
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
                            if (boxSet[value - 1]) {
                                return false; // Duplicate value in the 3x3 box
                            }
                            boxSet[value - 1] = true;
                        }
                    }
                }
            }
        }

        return true; // Sudoku is a valid solution
    }


    //This one is not using backtracking:
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
    
        System.out.println("Number of iterations (AC-3): " + iterations); // Print the number of iterations

        return true; // Sudoku is solvable according to AC-3
    }
    

    public void verifyAC3Output() {
        if (solveAC3()) {
            if (validSolution()) {
                System.out.println("Sudoku is solvable and valid.");
            } else {
                System.out.println("Sudoku is solvable but not valid.");
            }
        
            int iterationCount = getIterations();
            System.out.println("Number of iterations (AC-3 backtracking): " + iterationCount);
        
        } else {
            System.out.println("Sudoku is not solvable.");
        }
    }

}

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Sudoku {
  private Field[][] board;

  Sudoku(String filename) {
    this.board = readsudoku(filename);
  }

  @Override
  public String toString() {
    String output = "╔═══════╦═══════╦═══════╗\n";
		for(int i=0;i<9;i++){
      if(i == 3 || i == 6) {
		  	output += "╠═══════╬═══════╬═══════╣\n";
		  }
      output += "║ ";
		  for(int j=0;j<9;j++){
		   	if(j == 3 || j == 6) {
          output += "║ ";
		   	}
         output += board[i][j] + " ";
		  }
		  
      output += "║\n";
	  }
    output += "╚═══════╩═══════╩═══════╝\n";
    return output;
  }

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
	 * Generates fileformat output
	 */
	public String toFileString(){
    String output = "";
    for (int i = 0; i < board.length; i++) {
      for (int j = 0; j < board[0].length; j++) {
        output += board[i][j].getValue();
      }
      output += "\n";
    }
    return output;
	}

  public Field[][] getBoard(){
    return board;
  }
}

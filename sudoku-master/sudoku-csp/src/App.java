public class App {
    public static void main(String[] args) throws Exception {
        start("sudoku-csp/Sudoku3.txt");
        //String filePath = "C:/path/to/your/directory/Sudoku1.txt";
    }

    /**
     * Start AC-3 using the sudoku from the given filepath, and reports whether the sudoku could be solved or not, and how many steps the algorithm performed
     * 
     * @param filePath
     */
    public static void start(String filePath){
        Game game1 = new Game(new Sudoku(filePath));
        game1.showSudoku();
        game1.verifyAC3Output(); // Verify the output of the AC-3 algorithm


        /*if (game1.solve() && game1.validSolution()){
            System.out.println("Solved!");
        }
        else{
            System.out.println("Could not solve this sudoku :(");
        }*/
        game1.showSudoku();
    }
}

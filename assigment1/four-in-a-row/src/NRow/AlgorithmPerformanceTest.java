package NRow;
import NRow.Heuristics.Heuristic;
import NRow.Heuristics.CustomHeuristic;
import NRow.Players.MinMaxPlayer;
import NRow.Players.PlayerController;
import NRow.Players.AlphaBetaPlayer;

public class AlgorithmPerformanceTest {

    public static void main(String[] args) {
        // Experiment with different parameters
        int[] gameNValues = {3, 4};  // Values of N
        int[] boardWidths = {7, 9};  // Odd board widths
        int[] boardHeights = {7, 9}; // Odd board heights
        int[] searchDepths = {3, 4}; // Search depths

        // Initialize your heuristic here
        Heuristic heuristic = new CustomHeuristic(6);

        for (int gameN : gameNValues) {
            for (int boardWidth : boardWidths) {
                for (int boardHeight : boardHeights) {
                    for (int depth : searchDepths) {
                        // Create players for this game
                        PlayerController[] players = {
                            new MinMaxPlayer(1, gameN, depth, heuristic),
                            new AlphaBetaPlayer(2, gameN, depth, heuristic)
                        };

                        // Initialize the game with the current parameters
                        Game game = new Game(gameN, boardWidth, boardHeight, players);

                        // Measure and print the runtime for this combination
                        long minMaxRuntime = measureMinMaxRuntime(players[0], game.getBoard());
                        long alphaBetaRuntime = measureAlphaBetaRuntime(players[1], game.getBoard());

                        // Print the results
                        System.out.println("GameN: " + gameN +
                                ", Board Width: " + boardWidth +
                                ", Board Height: " + boardHeight +
                                ", Depth: " + depth);
                        System.out.println("MinMax Runtime: " + minMaxRuntime + " nanoseconds");
                        System.out.println("Alpha-beta Runtime: " + alphaBetaRuntime + " nanoseconds");
                        System.out.println();
                    }
                }
            }
        }
    }

    // Add helper methods to measure runtime for MinMax and Alpha-beta
    private static long measureMinMaxRuntime(PlayerController player, Board board) {
        long startTime = System.nanoTime();
        player.makeMove(board);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private static long measureAlphaBetaRuntime(PlayerController player, Board board) {
        long startTime = System.nanoTime();
        player.makeMove(board);
        long endTime = System.nanoTime();
        return endTime - startTime;
    }
}




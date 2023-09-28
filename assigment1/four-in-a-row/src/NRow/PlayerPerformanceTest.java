package NRow;
import NRow.Heuristics.Heuristic;
import NRow.Heuristics.SimpleHeuristic;
import NRow.Heuristics.CustomHeuristic;
import NRow.Players.MinMaxPlayer;
import NRow.Players.PlayerController;
import NRow.Players.AlphaBetaPlayer;

public class PlayerPerformanceTest {
    public static void main(String[] args) {
        // Define the game parameters
        int gameN = 10; // Adjust the game size as needed
        int[] depths = {1, 4, 50}; // Adjust the search depth as needed

        // Test Case 1: Vary Search Depth
        for (int depth : depths) {
            Board board = new Board(3, 5);
        
            Heuristic heuristic = new CustomHeuristic(gameN); // Replace with your heuristic
            MinMaxPlayer minMaxPlayer = new MinMaxPlayer(1, gameN, depth, heuristic);
            AlphaBetaPlayer alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, heuristic);
        
            System.out.println("Search Depth: " + depth);
        
            // Measure nodes expanded by MinMaxPlayer
            minMaxPlayer.makeMove(board); // This will print the nodes expanded by MinMaxPlayer
            // Reset nodes expanded count for AlphaBetaPlayer
            alphaBetaPlayer.resetNodesExpanded();
        
            // Measure nodes expanded by AlphaBetaPlayer
            alphaBetaPlayer.makeMove(board); // This will print the nodes expanded by AlphaBetaPlayer
        }
    }
}





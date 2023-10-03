package NRow.Tests;

import NRow.Board;
import NRow.Heuristics.*;
import NRow.Players.*;

public class NInARowTests {

    public static void main(String[] args) {
        System.out.println("----- Test Case 1 -----");
        // Define the game parameters
        int gameN = 4; // Adjust the game size as needed
        int[] depths = { 1, 4, 50 }; // Adjust the search depth as needed
        for (int depth : depths) {
            Board board = new Board(3, 5);

            Heuristic heuristic = new SimpleHeuristic(gameN); // Replace with your heuristic
            MinMaxPlayer minMaxPlayer = new MinMaxPlayer(1, gameN, depth, heuristic);
            AlphaBetaPlayer alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, heuristic);

            System.out.println("Search Depth: " + depth);

            // Measure nodes expanded by MinMaxPlayer
            minMaxPlayer.makeMove(board); // This will print the nodes expanded by MinMaxPlayer

            // Measure nodes expanded by AlphaBetaPlayer
            alphaBetaPlayer.makeMove(board); // This will print the nodes expanded by AlphaBetaPlayer
        }

        System.out.println("----- Test Case 2 -----");

    }

}

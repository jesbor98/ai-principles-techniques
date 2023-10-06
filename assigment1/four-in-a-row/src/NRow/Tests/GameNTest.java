/**
 * Artificial Intelligence: Principles & Techniques
 * Authors: Amanda Enhörning (s1128126) and Jessica Borg (s1129470)
 * Assignment 1: N-in-a-Row
 * 6 October 2023
 */

package NRow.Tests;

import NRow.Players.*;
import NRow.Game;
import NRow.Heuristics.*;

/**
 * The GameNTest class is responsible for testing different values of 'N' (the number of consecutive pieces to win) in a game of  N in a Row.
 * It creates game instances with varying 'N' values and players to analyze how different 'N' values affect gameplay.
 */
public class GameNTest {
    private static int depth = 3;
    private static int boardWidth = 7;
    private static int boardHeight = 5;

    /**
     * The main method of the GameNTest class.
     * It initializes different 'N' values for the game and runs games with these configurations.
     */
    public static void main(String[] args) {
        int[] gameNs = {2, 3, 4, 5};

        for (int gameN : gameNs) {
            PlayerController[] players = createPlayers(gameN);

            System.out.println("GameN " + gameN + ":");
            Game game = new Game(gameN, boardWidth, boardHeight, players);
            game.startGame();
            System.out.println();
        }
    }

    /**
     * Creates and initializes player controllers for the game with a specified 'N' value.
     *
     * @param gameN The 'N' value to set for the game.
     * @return An array of player controllers for the game with the specified 'N' value.
     */
    private static PlayerController[] createPlayers(int gameN) {
        CustomHeuristic customHeuristic1 = new CustomHeuristic(gameN);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(gameN);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, gameN, depth, customHeuristic1);
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, customHeuristic2);

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer };

        return players;
    }
}

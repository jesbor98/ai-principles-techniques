/**
 * Artificial Intelligence: Principles & Techniques
 * Authors: Amanda Enhörning (s1128126) and Jessica Borg (s1129470)
 * Assignment 1: N-in-a-Row
 * 6 October 2023
 */

package NRow.Tests;

import NRow.Players.*;
import NRow.Heuristics.CustomHeuristic;
import NRow.Game;

/**
 * The RuntimeComplexityTest class is responsible for testing the runtime complexity of the N in a Row game with varying depths and board sizes.
 * It measures how the execution time of the game changes as the search depth and board dimensions change.
 */
public class RuntimeComplexityTest {
    private static int gameN = 4;

    /**
     * The main method of the RuntimeComplexityTest class.
     * It runs the N-Row game with different depths, board widths, and board heights to analyze runtime complexity.
     */
    public static void main(String[] args) {
        int[] depths = {3, 5, 7};
        int[] boardWidths = {7, 9};
        int[] boardHeights = {5, 7};

        for (int depth : depths) {
            for (int width : boardWidths) {
                for (int height : boardHeights) {
                    PlayerController[] players = getPlayers(depth);

                    System.out.println("Depth " + depth + " with Board Size " + width + "x" + height + ":");
                    Game game = new Game(gameN, width, height, players);
                    game.startGame();
                    System.out.println();
                }
            }
        }
    }

    /**
     * Creates and initializes player controllers for the game with a specified depth.
     *
     * @param depth The depth of the search algorithm used by the players.
     * @return An array of player controllers with the specified search depth.
     */
    private static PlayerController[] getPlayers(int depth) {
        CustomHeuristic customHeuristic1 = new CustomHeuristic(gameN);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(gameN);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, gameN, depth, customHeuristic1);
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, customHeuristic2);

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer };

        return players;
    }
}

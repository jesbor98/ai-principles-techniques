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
 * The BoardSizeTest class is responsible for testing different board sizes in a game of N in a Row.
 * It creates various game boards with different dimensions and allows different players to compete.
 * The purpose is to assess the performance and behavior of the players on boards of varying sizes.
 */
public class BoardSizeTest {
    private static int gameN = 4;
    private static int depth = 3;

    /**
     * The main method of the BoardSizeTest class.
     * It initializes different board sizes and players, then runs games on each board size.
     */
    public static void main(String[] args) {
        int[] boardWidths = {5, 7, 9};
        int[] boardHeights = {5, 7, 9};

        PlayerController[] players = createPlayers();

        for (int width : boardWidths) {
            for(int height : boardHeights){
                System.out.println("Board Size " + width + "x" + height + ":");
                Game game = new Game(gameN, width, height, players);
                game.startGame();
                System.out.println();
            }
        }
    }

    /**
     * Creates and initializes player controllers for the game.
     *
     * @return An array of player controllers for the game.
     */
    private static PlayerController[] createPlayers() {
        CustomHeuristic customHeuristic1 = new CustomHeuristic(gameN);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(gameN);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, gameN, depth, customHeuristic1);
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, customHeuristic2);

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer };

        return players;
    }
}

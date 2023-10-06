/**
 * The DepthTest class is responsible for testing different depth levels for the MinMax and AlphaBeta players in a game of N in a Row.
 * It creates game instances with varying depths and players to analyze how different depths affect gameplay.
 */
package NRow.Tests;

import NRow.Players.*;
import NRow.Game;
import NRow.Heuristics.*;

public class DepthTest {
    private static int gameN = 4;
    private static int boardWidth = 7;
    private static int boardHeight = 5;

    /**
     * The main method of the DepthTest class.
     * It initializes different depth levels for the MinMax and AlphaBeta players and runs games with these configurations.
     */
    public static void main(String[] args) {
        int[] depths = {3, 4, 5, 6, 7};

        for (int depth : depths) {
            PlayerController[] players = createPlayers(depth);

            System.out.println("Depth " + depth + ":");
            Game game = new Game(gameN, boardWidth, boardHeight, players);
            game.startGame();
            System.out.println();
        }
    }

    /**
     * Creates and initializes player controllers for the game with a specified depth level.
     *
     * @param depth The depth level to set for the MinMax and AlphaBeta players.
     * @return An array of player controllers for the game with the specified depth level.
     */
    private static PlayerController[] createPlayers(int depth) {
        CustomHeuristic customHeuristic1 = new CustomHeuristic(gameN);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(gameN);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, gameN, depth, customHeuristic1);
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, customHeuristic2);

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer };

        return players;
    }
}

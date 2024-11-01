/**
 * Artificial Intelligence: Principles & Techniques
 * Authors: Amanda Enhörning (s1128126) and Jessica Borg (s1129470)
 * Assignment 1: N-in-a-Row
 * 6 October 2023
 */

package NRow;

import NRow.Heuristics.CustomHeuristic;
import NRow.Heuristics.SimpleHeuristic;
import NRow.Players.HumanPlayer;
import NRow.Players.MinMaxPlayer;
import NRow.Players.AlphaBetaPlayer;
import NRow.Players.PlayerController;

public class App {
    public static void main(String[] args) throws Exception {
        int gameN = 4;
        int boardWidth = 7;
        int boardHeight = 5;

        PlayerController[] players = getPlayers(gameN);

        Game game = new Game(gameN, boardWidth, boardHeight, players);
        game.startGame();
    }

    /**
     * Determine the players for the game
     * @param n which represents the number of game pieces in a row that is needed to win
     * @return an array of size 2 with two Playercontrollers
     */
    private static PlayerController[] getPlayers(int n) {
        SimpleHeuristic heuristic1 = new SimpleHeuristic(n);
        SimpleHeuristic heuristic2 = new SimpleHeuristic(n);

        CustomHeuristic customHeuristic1 = new CustomHeuristic(n);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(n);

        PlayerController human = new HumanPlayer(2, n, customHeuristic1 );
        PlayerController human2 = new HumanPlayer(2, n, customHeuristic1);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, n, 3, customHeuristic2); // Adjust the depth as needed
        PlayerController minMaxPlayer2 = new MinMaxPlayer(2, n, 4, customHeuristic2); // Adjust the depth as needed
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(1, n, 4, customHeuristic1 ); // Adjust the depth as needed
        PlayerController alphaBetaPlayer2 = new AlphaBetaPlayer(2, n, 3, customHeuristic1 ); // Adjust the depth as needed

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer2 };

        return players;
    }
}

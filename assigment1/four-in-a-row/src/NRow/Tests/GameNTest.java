package NRow.Tests;

import NRow.Players.*;
import NRow.Game;
import NRow.Heuristics.*;

public class GameNTest {
    private static int depth = 3;
    private static int boardWidth = 7;
    private static int boardHeight = 5;

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

    private static PlayerController[] createPlayers(int gameN) {
        CustomHeuristic customHeuristic1 = new CustomHeuristic(gameN);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(gameN);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, gameN, depth, customHeuristic1);
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, customHeuristic2);

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer };

        return players;
    }
}
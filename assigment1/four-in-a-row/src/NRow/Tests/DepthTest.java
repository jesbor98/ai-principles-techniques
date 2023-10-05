package NRow.Tests;

import NRow.Players.*;
import NRow.Board;
import NRow.Game;
import NRow.Heuristics.*;

public class DepthTest {
    private static int gameN = 4;
    private static int boardWidth = 7;
    private static int boardHeight = 5;

    public static void main(String[] args) {
        int[] depths = {1, 3, 5, 6};

        for (int depth : depths) {
            PlayerController[] players = createPlayers(depth);

            System.out.println("Depth " + depth + ":");
            Game game = new Game(gameN, boardWidth, boardHeight, players);
            game.startGame();
            System.out.println();
        }
    }

    private static PlayerController[] createPlayers(int depth) {
        CustomHeuristic customHeuristic1 = new CustomHeuristic(gameN);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(gameN);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, gameN, depth, customHeuristic1);
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, customHeuristic2);

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer };

        return players;
    }
}
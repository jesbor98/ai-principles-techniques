package NRow.Tests;

import NRow.Players.*;
import NRow.Game;
import NRow.Heuristics.*;

public class BoardSizeTest {
    private static int gameN = 4;
    private static int depth = 3;
    //private static int boardWidth = 7;
    //private static int boardHeight = 5;

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

    private static PlayerController[] createPlayers() {
        CustomHeuristic customHeuristic1 = new CustomHeuristic(gameN);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(gameN);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, gameN, depth, customHeuristic1);
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, customHeuristic2);

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer };

        return players;
    }
}
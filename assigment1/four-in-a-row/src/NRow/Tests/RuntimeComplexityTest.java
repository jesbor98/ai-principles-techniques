package NRow.Tests;

import NRow.Players.*;
import NRow.Heuristics.CustomHeuristic;
import NRow.Game;

public class RuntimeComplexityTest {
    private static int gameN = 4;

    public static void main(String[] args) {
        int[] depths = {3, 5, 7};
        int[] boardWidths = {7, 9};
        int[] boardHeights = {5, 7};

        for (int depth : depths) {
            for(int width : boardWidths){
                for(int height : boardHeights){
                    PlayerController[] players = getPlayers(depth);

                    System.out.println("Depth " + depth + " with Board Size " + width + "x" + height + ":");
                    Game game = new Game(gameN, width, height, players);
                    game.startGame();
                    System.out.println();
                }
            }
        }
    }

    private static PlayerController[] getPlayers(int depth) {
        CustomHeuristic customHeuristic1 = new CustomHeuristic(gameN);
        CustomHeuristic customHeuristic2 = new CustomHeuristic(gameN);

        PlayerController minMaxPlayer = new MinMaxPlayer(1, gameN, depth, customHeuristic1);
        PlayerController alphaBetaPlayer = new AlphaBetaPlayer(2, gameN, depth, customHeuristic2);

        PlayerController[] players = { minMaxPlayer, alphaBetaPlayer };

        return players;
    }
}

package NRow.Heuristics;

import NRow.Board;
import NRow.Game;

public class CustomHeuristic extends Heuristic {

    public CustomHeuristic(int gameN) {
        super(gameN);
    }

    @Override
    protected String name() {
        return "Custom";
    }

    /**
     * Determine utility of a board state
     */
    @Override
    protected int evaluate(int player, Board board) {
        int[][] boardState = board.getBoardState();
        int winning = Game.winning(boardState, this.gameN);
        if (winning == player) {
            return Integer.MAX_VALUE;
        } else if (winning != 0) {
            return Integer.MIN_VALUE;
        }

        int maxUtility = 0;

        // Define directions for checking
        int[][] directions = {
            {0, 1},  // Horizontal
            {1, 0},  // Vertical
            {1, 1},  // Diagonal /
            {-1, 1}  // Diagonal \
        };

        for (int i = 0; i < boardState.length; i++) {
            for (int j = 0; j < boardState[i].length; j++) {
                if (boardState[i][j] == player) {
                    for (int[] dir : directions) {
                        int rowDir = dir[0];
                        int colDir = dir[1];
                        int inRow = 1;  // Count the number of consecutive pieces in a row

                        // Check in both directions from the current position
                        for (int d = 1; d < gameN; d++) {
                            int newRow = i + d * rowDir;
                            int newCol = j + d * colDir;

                            if (newRow >= 0 && newRow < boardState.length && newCol >= 0 && newCol < boardState[0].length) {
                                if (boardState[newRow][newCol] == player) {
                                    inRow++;
                                } else {
                                    break;
                                }
                            } else {
                                break;
                            }
                        }

                        maxUtility = Math.max(maxUtility, inRow);
                    }
                } else if (boardState[i][j] == 0) {
                    int utility = 0;

                    for (int[] dir : directions) {
                        int rowDir = dir[0];
                        int colDir = dir[1];

                        // Check both directions from the current empty position
                        for (int side = -1; side <= 1; side += 2) {
                            int newRow = i + side * rowDir;
                            int newCol = j + side * colDir;

                            int playerInRow = 0;  // Count the number of consecutive player's pieces in a row
                            int opponentInRow = 0;  // Count the number of consecutive opponent's pieces in a row

                            // Check in one direction
                            while (newRow >= 0 && newRow < boardState.length && newCol >= 0 && newCol < boardState[0].length) {
                                if (boardState[newRow][newCol] == player) {
                                    playerInRow++;
                                    newRow += side * rowDir;
                                    newCol += side * colDir;
                                } else if (boardState[newRow][newCol] != player && boardState[newRow][newCol] != 0) {
                                    opponentInRow++;
                                    break;
                                } else {
                                    break;
                                }
                            }

                            newRow = i - side * rowDir;
                            newCol = j - side * colDir;

                            // Check in the other direction
                            while (newRow >= 0 && newRow < boardState.length && newCol >= 0 && newCol < boardState[0].length) {
                                if (boardState[newRow][newCol] == player) {
                                    playerInRow++;
                                    newRow -= side * rowDir;
                                    newCol -= side * colDir;
                                } else if (boardState[newRow][newCol] != player && boardState[newRow][newCol] != 0) {
                                    opponentInRow++;
                                    break;
                                } else {
                                    break;
                                }
                            }

                            // Check if blocking the opponent from winning or a winning move for the player
                            if (opponentInRow >= gameN - 1) {
                                utility = Integer.MIN_VALUE;
                                break;
                            } else if (playerInRow >= gameN - 1) {
                                utility = Integer.MAX_VALUE;
                                break;
                            }
                        }
                    }

                    // Suggest moves that maximize the player's chances of winning
                    utility = Math.max(utility, 1);

                    maxUtility = Math.max(maxUtility, utility);
                }
            }
        }

        return maxUtility;
    }
}

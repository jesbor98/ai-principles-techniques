package NRow.Heuristics;

import NRow.Board;
import NRow.Game;

/**
 * The CustomHeuristic class provides a custom heuristic function for evaluating game states.
 *
 * This heuristic considers factors such as consecutive pieces, blocking opponents, and encouraging
 * the completion of rows to assign a utility value to a game state.
 */
public class CustomHeuristic extends Heuristic {

    /**
     * Constructs a new CustomHeuristic with the specified game parameter.
     *
     * @param gameN The value of N for the game.
     */
    public CustomHeuristic(int gameN) {
        super(gameN);
    }

    /**
     * Gets the name of the heuristic.
     *
     * @return The name of the heuristic.
     */
    @Override
    protected String name() {
        return "Custom";
    }
    

     /**
     * Evaluates a game state and assigns a utility value.
     *
     * @param player The player for whom the utility is calculated.
     * @param board The game board state.
     * @param gameN The value of N for the game.
     * @return The utility value of the game state.
     */
    @Override
    protected int evaluate(int player, Board board, int gameN) {
        int[][] boardState = board.getBoardState();
        int winning = Game.winning(boardState, gameN);
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

                    // Suggest moves that maximize the player's chances of winning or blocking
                    utility += playerRowScore(player, i, j, boardState, gameN);
                    maxUtility = Math.max(maxUtility, utility);
                }
            }
        }

        return maxUtility;
    }

    /**
     * Assigns a score to the current player's row to encourage completing rows.
     *
     * @param player The player for whom the score is calculated.
     * @param row The row index on the game board.
     * @param col The column index on the game board.
     * @param boardState The current game board state.
     * @param gameN The value of N for the game.
     * @return The row score.
     */
    private int playerRowScore(int player, int row, int col, int[][] boardState, int gameN) {
        int score = 0;

        // Define directions for checking
        int[][] directions = {
            {0, 1},  // Horizontal
            {1, 0},  // Vertical
            {1, 1},  // Diagonal /
            {-1, 1}  // Diagonal \
        };

        for (int[] dir : directions) {
            int rowDir = dir[0];
            int colDir = dir[1];
            int count = 0;

            // Check in both directions
            for (int side = -1; side <= 1; side += 2) {
                int newRow = row + side * rowDir;
                int newCol = col + side * colDir;

                boolean opponentPresent = false;

                // Check for consecutive pieces
                while (newRow >= 0 && newRow < boardState.length && newCol >= 0 && newCol < boardState[0].length) {
                    if (boardState[newRow][newCol] == player) {
                        count++;
                        newRow += side * rowDir;
                        newCol += side * colDir;
                    } else if (boardState[newRow][newCol] != 0) {
                        opponentPresent = true;
                        break;
                    } else {
                        break;
                    }
                }

                if (!opponentPresent) {
                    score += count;
                }
            }
        }

        return score;
    }
}

package NRow.Players;

import NRow.TreeNode;
import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

/**
 * The AlphaBetaPlayer class represents a player that uses the Alpha-Beta Pruning algorithm
 * to make optimal moves in a game.
 *
 * This player extends the PlayerController class and implements the minimax algorithm
 * with alpha-beta pruning to find the best move in a game.
 */

public class AlphaBetaPlayer extends PlayerController {
    private int depth; // Search depth for the alpha-beta algorithm.

    /**
     * Constructs a new AlphaBetaPlayer with the specified parameters.
     *
     * @param playerId The identifier for this player.
     * @param gameN The value of N for the game.
     * @param depth The search depth for the alpha-beta algorithm.
     * @param heuristic The heuristic function used to evaluate game states.
     */
    public AlphaBetaPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
    }

    /**
     * Makes a move on the game board using the Alpha-Beta Pruning algorithm.
     *
     * @param board The current game board.
     * @return The column index representing the best move for the player.
     */
    @Override
    public int makeMove(Board board) {
        int[] availableMoves = getAvailableMoves(board);

        int bestMove = -1;
        int alpha = Integer.MIN_VALUE; // Initialize alpha
        int beta = Integer.MAX_VALUE; // Initialize beta
        
        for (int move : availableMoves) {
            Board newBoard = board.getNewBoard(move, playerId);
            TreeNode childNode = new TreeNode(newBoard, move);
            int value = minValue(childNode, depth - 1, alpha, beta, playerId);
            
            if (value > alpha) {
                alpha = value; // Update alpha
                bestMove = move;
            }
        }

        return bestMove;
    }

    /**
     * Finds the maximum value during the alpha-beta search.
     *
     * @param node The current tree node.
     * @param depth The remaining search depth.
     * @param alpha The current alpha value.
     * @param beta The current beta value.
     * @param playerId The player's identifier.
     * @return The maximum value found.
     */
    private int maxValue(TreeNode node, int depth, int alpha, int beta, int playerId) {
        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return heuristic.evaluateBoard(playerId, node.getBoard(), gameN);
        }

        for (TreeNode child : node.getChildren()) {
            int value = minValue(child, depth - 1, alpha, beta, playerId);
            alpha = Math.max(alpha, value); // Update alpha

            if (alpha >= beta) {
                return alpha; // Alpha-beta pruning
            }
        }

        return alpha;
    }
    /**
     * Finds the minimum value during the alpha-beta search.
     *
     * @param node The current tree node.
     * @param depth The remaining search depth.
     * @param alpha The current alpha value.
     * @param beta The current beta value.
     * @param playerId The player's identifier.
     * @return The minimum value found.
     */

    private int minValue(TreeNode node, int depth, int alpha, int beta, int playerId) {
        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return heuristic.evaluateBoard(playerId, node.getBoard(), gameN);
        }

        for (TreeNode child : node.getChildren()) {
            int value = maxValue(child, depth - 1, alpha, beta, playerId);
            beta = Math.min(beta, value); // Update beta

            if (alpha >= beta) {
                return beta; // Alpha-beta pruning
            }
        }

        return beta;
    }

    /**
     * Retrieves the available moves for the current game board.
     *
     * @param board The current game board.
     * @return An array of available move indices (column indices).
     */
    private int[] getAvailableMoves(Board board) {
        int[] availableMoves = new int[board.width];
        int moveCount = 0;

        for (int col = 0; col < board.width; col++) {
            if (board.isValid(col)) {
                availableMoves[moveCount] = col;
                moveCount++;
            }
        }

        int[] result = new int[moveCount];
        System.arraycopy(availableMoves, 0, result, 0, moveCount);
        return result;
    }
}



// package NRow.Players;

// import NRow.Board;
// import NRow.Heuristics.Heuristic;
// import NRow.Game;

// public class AlphaBetaPlayer extends PlayerController {
//     private int depth;

//     public AlphaBetaPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
//         super(playerId, gameN, heuristic);
//         this.depth = depth;
//     }

//     @Override
//     public int makeMove(Board board) {
//         int[] availableMoves = getAvailableMoves(board);

//         int bestMove = -1;
//         int alpha = Integer.MIN_VALUE; // Initialize alpha
//         int beta = Integer.MAX_VALUE; // Initialize beta
        
//         for (int move : availableMoves) {
//             Board newBoard = board.getNewBoard(move, playerId);
//             int value = minValue(newBoard, depth - 1, alpha, beta);
            
//             if (value > alpha) {
//                 alpha = value; // Update alpha
//                 bestMove = move;
//             }
//         }

//         return bestMove;
//     }

//     private int maxValue(Board board, int depth, int alpha, int beta) {
//         if (depth == 0 || Game.winning(board.getBoardState(), gameN) != 0) {
//             return heuristic.evaluateBoard(playerId, board);
//         }

//         int[] availableMoves = getAvailableMoves(board);

//         for (int move : availableMoves) {
//             Board newBoard = board.getNewBoard(move, playerId);
//             int value = minValue(newBoard, depth - 1, alpha, beta);
//             alpha = Math.max(alpha, value); // Update alpha

//             if (alpha >= beta) {
//                 return alpha; // Alpha-beta pruning
//             }
//         }

//         return alpha;
//     }

//     private int minValue(Board board, int depth, int alpha, int beta) {
//         if (depth == 0 || Game.winning(board.getBoardState(), gameN) != 0) {
//             return heuristic.evaluateBoard(playerId, board);
//         }

//         int[] availableMoves = getAvailableMoves(board);

//         for (int move : availableMoves) {
//             Board newBoard = board.getNewBoard(move, 3 - playerId); // Switch player
//             int value = maxValue(newBoard, depth - 1, alpha, beta);
//             beta = Math.min(beta, value); // Update beta

//             if (alpha >= beta) {
//                 return beta; // Alpha-beta pruning
//             }
//         }

//         return beta;
//     }

//     private int[] getAvailableMoves(Board board) {
//         int[] availableMoves = new int[board.width];
//         int moveCount = 0;

//         for (int col = 0; col < board.width; col++) {
//             if (board.isValid(col)) {
//                 availableMoves[moveCount] = col;
//                 moveCount++;
//             }
//         }

//         int[] result = new int[moveCount];
//         System.arraycopy(availableMoves, 0, result, 0, moveCount);
//         return result;
//     }
// }

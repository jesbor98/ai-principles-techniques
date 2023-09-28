package NRow.Players;

import NRow.TreeNode;

import java.util.Arrays;

import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

/**
 * The MinMaxPlayer class represents a player that uses the Minimax algorithm
 * to make optimal moves in a game.
 *
 * This player extends the PlayerController class and implements the Minimax algorithm
 * to find the best move in a game.
 */
public class MinMaxPlayer extends PlayerController {
    private int depth;


    /**
     * Constructs a new MinMaxPlayer with the specified parameters.
     *
     * @param playerId The identifier for this player.
     * @param gameN The value of N for the game.
     * @param depth The search depth for the Minimax algorithm.
     * @param heuristic The heuristic function used to evaluate game states.
     */
    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;

    }

    /**
     * Makes a move on the game board using the Minimax algorithm.
     *
     * @param board The current game board.
     * @return The column index representing the best move for the player.
     */

     @Override
public int makeMove(Board board) {
    int[] availableMoves = getAvailableMoves(board);

    // Print available moves for debugging
    System.out.println("Available Moves: " + Arrays.toString(availableMoves));

    int bestMove = -1;
    int bestValue = Integer.MIN_VALUE;

    TreeNode rootNode = new TreeNode(board, -1); // Create the root node with a dummy move

    for (int move : availableMoves) {
        Board newBoard = board.getNewBoard(move, playerId);
        TreeNode childNode = new TreeNode(newBoard, move);
        rootNode.addChild(childNode);
        int value = minValue(childNode, depth, playerId);

        if (value > bestValue) {
            bestValue = value;
            bestMove = move;
        }
    }

    System.out.println("Selected Move: " + bestMove); // Print the selected move for debugging
    System.out.println("Nodes Expanded for MinMax: " + super.getEvalCount()); // Print node count

    return bestMove;
}

     

    /**
     * Finds the maximum value during the Minimax search.
     *
     * @param node The current tree node.
     * @param depth The remaining search depth.
     * @param playerId The player's identifier.
     * @return The maximum value found.
     */
    private int maxValue(TreeNode node, int depth, int playerId) {
        System.out.println("Expanding node MAX: " + super.getEvalCount()); // Add this line for debugging

        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return heuristic.evaluateBoard(playerId, node.getBoard(), gameN);
        }

        int bestValue = Integer.MIN_VALUE;

        for (TreeNode child : node.getChildren()) {
            int value = minValue(child, depth - 1, playerId);
            bestValue = Math.max(bestValue, value);
        }

        return bestValue;
    }

    /**
     * Finds the minimum value during the Minimax search.
     *
     * @param node The current tree node.
     * @param depth The remaining search depth.
     * @param playerId The player's identifier.
     * @return The minimum value found.
     */
    private int minValue(TreeNode node, int depth, int playerId) {
        System.out.println("Expanding node MIN: " + super.getEvalCount()); // Add this line for debugging

        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return heuristic.evaluateBoard(playerId, node.getBoard(), gameN);
        }

        int bestValue = Integer.MAX_VALUE;

        for (TreeNode child : node.getChildren()) {
            int value = maxValue(child, depth - 1, playerId);
            bestValue = Math.min(bestValue, value);
        }

        return bestValue;
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

// public class MinMaxPlayer extends PlayerController {
//     private int depth;

//     public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
//         super(playerId, gameN, heuristic);
//         this.depth = depth;
//     }

//     @Override
//     public int makeMove(Board board) {
//         int[] availableMoves = getAvailableMoves(board);

//         int bestMove = -1;
//         int bestValue = Integer.MIN_VALUE;

//         for (int move : availableMoves) {
//             Board newBoard = board.getNewBoard(move, playerId);
//             int value = minValue(newBoard, depth - 1);
            
//             if (value > bestValue) {
//                 bestValue = value;
//                 bestMove = move;
//             }
//         }

//         return bestMove;
//     }

//     private int maxValue(Board board, int depth) {
//         if (depth == 0 || Game.winning(board.getBoardState(), gameN) != 0) {
//             return heuristic.evaluateBoard(playerId, board);
//         }

//         int[] availableMoves = getAvailableMoves(board);
//         int bestValue = Integer.MIN_VALUE;

//         for (int move : availableMoves) {
//             Board newBoard = board.getNewBoard(move, playerId);
//             int value = minValue(newBoard, depth - 1);
//             bestValue = Math.max(bestValue, value);
//         }

//         return bestValue;
//     }

//     private int minValue(Board board, int depth) {
//         if (depth == 0 || Game.winning(board.getBoardState(), gameN) != 0) {
//             return heuristic.evaluateBoard(playerId, board);
//         }

//         int[] availableMoves = getAvailableMoves(board);
//         int bestValue = Integer.MAX_VALUE;

//         for (int move : availableMoves) {
//             Board newBoard = board.getNewBoard(move, 3 - playerId); // Switch player
//             int value = maxValue(newBoard, depth - 1);
//             bestValue = Math.min(bestValue, value);
//         }

//         return bestValue;
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

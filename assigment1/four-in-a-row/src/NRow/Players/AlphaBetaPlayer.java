/**
 * Artificial Intelligence: Principles & Techniques
 * Authors: Amanda Enhörning (s1128126) and Jessica Borg (s1129470)
 * Assignment 1: N-in-a-Row
 * 6 October 2023
 */

package NRow.Players;

import NRow.TreeNode;
import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

public class AlphaBetaPlayer extends PlayerController {
    private int depth;
    private TreeNode rootNode;
    private int player2Id;

    /**
     * Constructs a new AlphaBetaPlayer with the specified parameters.
     * @param playerId The ID of the player.
     * @param gameN The value of N for the game.
     * @param depth The depth for the Alpha-Beta search.
     * @param heuristic The heuristic function for evaluating game states.
     */
    public AlphaBetaPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
        this.rootNode = new TreeNode(null, -1);
        this.player2Id = (playerId == 1) ? 2 : 1; // Set player2ID based on playerId
    }

    /**
     * Makes a move based on the current game state.
     * @param board The current game board.
     * @return The column index where the player intends to make a move.
     */
    @Override
    public int makeMove(Board board) {
        int[] availableMoves = getAvailableMoves(board);
        rootNode = new TreeNode(board, -1);
        buildTree(rootNode, depth, playerId);

        if (availableMoves.length == 0) {
            System.out.println("No available moves left.");
            return -1;
        }

        int bestMove = -1;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (TreeNode child : rootNode.getChildren()) {
            int value = minValue(child, depth, alpha, beta, playerId, player2Id);

            if (value > alpha) {
                alpha = value; // Update alpha
                bestMove = child.getMove();
            }
        }

        int validMove = findValidMove(board, bestMove);
        if (validMove != -1) {
            return validMove;
        } else {
            return -1;
        }
    }

    /**
     * Finds a valid move on the board, considering wrap-around.
     * @param board The current game board.
     * @param move The proposed move.
     * @return A valid move after considering wrap-around, or -1 if no valid move is found.
     */
    public int findValidMove(Board board, int move) {
        if (board.isValid(move)) {
            return move;
        } else {
            int nextMove = move + 1;
            if (nextMove >= board.width) {
                nextMove = 0; // Wrap around to the beginning
            }
            // Ensure nextMove is within valid bounds
            if (nextMove < 0) {
                nextMove = 0; // Handle case where nextMove becomes negative
            }
         
            return findValidMove(board, nextMove);
        }
    }

    /**
     * Computes the minimum value for the Minimax algorithm with Alpha-Beta pruning.
     * @param node The current node in the game tree.
     * @param depth The remaining depth for the search.
     * @param alpha The alpha value for pruning.
     * @param beta The beta value for pruning.
     * @param currentPlayer The ID of the current player.
     * @param opponent The ID of the opponent player.
     * @return The minimum value for the given node.
     */
    private int minValue(TreeNode node, int depth, int alpha, int beta, int currentPlayer, int opponent) {
        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return evaluatePosition(node.getBoard(), opponent);
        }

        for (TreeNode child : node.getChildren()) {
            int value = maxValue(child, depth - 1, alpha, beta, currentPlayer, opponent);
            beta = Math.min(beta, value);

            if (alpha >= beta) {
                return beta; // Alpha-beta pruning
            }
        }

        return beta;
    }

    /**
     * Computes the maximum value for the Minimax algorithm with Alpha-Beta pruning.
     * @param node The current node in the game tree.
     * @param depth The remaining depth for the search.
     * @param alpha The alpha value for pruning.
     * @param beta The beta value for pruning.
     * @param currentPlayer The ID of the current player.
     * @param opponent The ID of the opponent player.
     * @return The maximum value for the given node.
     */
    private int maxValue(TreeNode node, int depth, int alpha, int beta, int currentPlayer, int opponent) {
        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return evaluatePosition(node.getBoard(), currentPlayer);
        }

        for (TreeNode child : node.getChildren()) {
            int value = minValue(child, depth - 1, alpha, beta, currentPlayer, opponent);
            alpha = Math.max(alpha, value); // Update alpha

            if (alpha >= beta) {
                return alpha; // Alpha-beta pruning
            }
        }

        return alpha;
    }

    /**
     * Builds the game tree using recursion.
     * @param node The current node in the game tree.
     * @param depth The remaining depth for tree expansion.
     * @param currentPlayer The ID of the current player.
     */
    private void buildTree(TreeNode node, int depth, int currentPlayer) {
        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return;
        }

        int[] availableMoves = getAvailableMoves(node.getBoard());

        for (int move : availableMoves) {
            Board newBoard = node.getBoard().getNewBoard(move, currentPlayer);
            TreeNode childNode = new TreeNode(newBoard, move);
            node.addChild(childNode);
            buildTree(childNode, depth - 1, (currentPlayer == playerId) ? player2Id : playerId);
        }
    }

    /**
     * Evaluates the current game board position for a player.
     * @param board The current game board.
     * @param playerId The ID of the player for whom the position is evaluated.
     * @return The evaluation score of the board position for the player.
     */
    private int evaluatePosition(Board board, int playerId) {
        return heuristic.evaluateBoard(playerId, board, gameN);
    }

    /**
     * Retrieves an array of available moves on the current game board.
     * @param board The current game board.
     * @return An array containing column indices of available moves.
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



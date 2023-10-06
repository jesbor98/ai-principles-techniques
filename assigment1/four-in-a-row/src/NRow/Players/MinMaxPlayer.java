package NRow.Players;

import NRow.TreeNode;
import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

public class MinMaxPlayer extends PlayerController {
    private int depth;
    private TreeNode rootNode;
    private int player2Id; // Add a field for player2ID

    /**
     * Constructs a MinMaxPlayer with the specified parameters.
     *
     * @param playerId The ID of the player.
     * @param gameN The value of N for the game.
     * @param depth The depth of the search tree.
     * @param heuristic The heuristic used for evaluation.
     */
    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
        this.rootNode = new TreeNode(null, -1); // Create the root node with a dummy move and null board
        this.player2Id = (playerId == 1) ? 2 : 1; // Set player2ID based on playerId
    }

    /**
     * Makes a move by selecting the best move using the Minimax algorithm.
     *
     * @param board The current game board.
     * @return The selected move.
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
        int bestValue = Integer.MIN_VALUE;

        for (TreeNode child : rootNode.getChildren()) {
            int value = minValue(child, depth, playerId, player2Id);

            if (value > bestValue) {
                bestValue = value;
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
     * Finds a valid move starting from the specified move and wrapping around if needed.
     *
     * @param board The current game board.
     * @param move The initial move to start searching from.
     * @return A valid move.
     */
    public int findValidMove(Board board, int move) {
        if (board.isValid(move)) {
            return move; // Found a valid move
        } else {
            int nextMove = move + 1;
            if (nextMove >= board.width) {
                nextMove = 0; // Wrap around to the beginning
            }
            // Ensure that nextMove is within valid bounds
            if (nextMove < 0) {
                nextMove = 0; // Handle case where nextMove becomes negative
            }
            // Recursively try the next move
            return findValidMove(board, nextMove);
        }
    }

    /**
     * Builds the game tree up to a specified depth, exploring possible moves for the current player.
     *
     * @param node The current node of the game tree.
     * @param depth The maximum depth to explore.
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
     * Computes the minimum value for a node in the Minimax algorithm.
     *
     * @param node The current node in the game tree.
     * @param depth The remaining depth to explore.
     * @param currentPlayer The ID of the current player.
     * @param opponent The ID of the opponent player.
     * @return The minimum value.
     */
    private int minValue(TreeNode node, int depth, int currentPlayer, int opponent) {
        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return evaluatePosition(node.getBoard(), opponent);
        }

        int bestValue = Integer.MAX_VALUE;

        for (TreeNode child : node.getChildren()) {
            int value = maxValue(child, depth - 1, currentPlayer, opponent);
            bestValue = Math.min(bestValue, value);
        }

        return bestValue;
    }

    /**
     * Computes the maximum value for a node in the Minimax algorithm.
     *
     * @param node The current node in the game tree.
     * @param depth The remaining depth to explore.
     * @param currentPlayer The ID of the current player.
     * @param opponent The ID of the opponent player.
     * @return The maximum value.
     */
    private int maxValue(TreeNode node, int depth, int currentPlayer, int opponent) {
        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return evaluatePosition(node.getBoard(), currentPlayer);
        }

        int bestValue = Integer.MIN_VALUE;

        for (TreeNode child : node.getChildren()) {
            int value = minValue(child, depth - 1, currentPlayer, opponent);
            bestValue = Math.max(bestValue, value);
        }

        return bestValue;
    }

    /**
     * Evaluates the position of the game board for a given player using a heuristic function.
     *
     * @param board The game board to evaluate.
     * @param playerId The ID of the player for whom to evaluate the board.
     * @return The evaluation score of the board.
     */
    private int evaluatePosition(Board board, int playerId) {
        return heuristic.evaluateBoard(playerId, board, gameN);
    }

    /**
     * Retrieves an array of available moves on the current game board.
     *
     * @param board The current game board.
     * @return An array of available moves.
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
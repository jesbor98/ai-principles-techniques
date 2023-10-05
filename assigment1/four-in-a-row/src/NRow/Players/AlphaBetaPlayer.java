package NRow.Players;

import NRow.TreeNode;
import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

public class AlphaBetaPlayer extends PlayerController {
    private int depth;
    private TreeNode rootNode;
    private int player2Id;

    public AlphaBetaPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
        this.rootNode = new TreeNode(null, -1);
        this.player2Id = (playerId == 1) ? 2 : 1; // Set player2ID based on playerId
    }

    @Override
    public int makeMove(Board board) {
        int[] availableMoves = getAvailableMoves(board);
        rootNode = new TreeNode(board, -1);
        buildTree(rootNode, depth, player2Id);

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

    private int minValue(TreeNode node, int depth, int alpha, int beta, int currentPlayer, int opponent) {
        if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
            return evaluatePosition(node.getBoard(), opponent);
        }

        for (TreeNode child : node.getChildren()) {
            int value = maxValue(child, depth - 1, alpha, beta, currentPlayer, opponent);
            beta = Math.min(beta, value); // Update beta

            if (alpha >= beta) {
                return beta; // Alpha-beta pruning
            }
        }

        return beta;
    }

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

    private int evaluatePosition(Board board, int playerId) {
        return heuristic.evaluateBoard(playerId, board, gameN);
    }

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



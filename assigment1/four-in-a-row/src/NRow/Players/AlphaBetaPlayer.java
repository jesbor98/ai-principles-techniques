package NRow.Players;

import NRow.TreeNode;
import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

public class AlphaBetaPlayer extends PlayerController {
    private int depth;

    public AlphaBetaPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
    }

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

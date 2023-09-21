package NRow.Players;

import NRow.TreeNode;
import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

public class MinMaxPlayer extends PlayerController {
    private int depth;

    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
    }

    @Override
    public int makeMove(Board board) {
        int[] availableMoves = getAvailableMoves(board);

        int bestMove = -1;
        int bestValue = Integer.MIN_VALUE;

        for (int move : availableMoves) {
            Board newBoard = board.getNewBoard(move, playerId);
            TreeNode childNode = new TreeNode(newBoard, move);
            int value = minValue(childNode, depth - 1, playerId);

            if (value > bestValue) {
                bestValue = value;
                bestMove = move;
            }
        }

        return bestMove;
    }

    private int maxValue(TreeNode node, int depth, int playerId) {
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

    private int minValue(TreeNode node, int depth, int playerId) {
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

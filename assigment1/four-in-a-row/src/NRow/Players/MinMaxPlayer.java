package NRow.Players;

import NRow.TreeNode;

import java.util.Arrays;

import NRow.Board;
import NRow.Heuristics.Heuristic;
import NRow.Game;

import java.util.Arrays;

public class MinMaxPlayer extends PlayerController {
    private int depth;
    private TreeNode rootNode;
    private int player2ID; // Add a field for player2ID


    public MinMaxPlayer(int playerId, int gameN, int depth, Heuristic heuristic) {
        super(playerId, gameN, heuristic);
        this.depth = depth;
        this.rootNode = new TreeNode(null, -1); // Create the root node with a dummy move and null board
        this.player2ID = (playerId == 1) ? 2 : 1; // Set player2ID based on playerId
    }

    @Override
public int makeMove(Board board) {
    int[] availableMoves = getAvailableMoves(board);
    rootNode = new TreeNode(board, -1);
    buildTree(rootNode, depth, playerId);

    // Print available moves for debugging
    System.out.println("Available Moves: " + Arrays.toString(availableMoves));

    if (availableMoves.length == 0) {
        System.out.println("No available moves left.");
        return -1; // No valid moves left, return an error value or handle it accordingly
    }

    int bestMove = -1;
    int bestValue = Integer.MIN_VALUE;

    // Clear the existing child nodes (if any) from the previous move
  

    for (TreeNode child : rootNode.getChildren()) {
        int value = minValue(child, depth, playerId, player2ID);

        if (value > bestValue) {
            bestValue = value;
            bestMove = child.getMove();
        }
    }

    System.out.println("Selected Move: " + bestMove); // Print the selected move for debugging
    System.out.println("Nodes Expanded for MinMax: " + super.getEvalCount()); // Print node count

    int validMove = findValidMove(board, bestMove);
        if (validMove != -1) {
            return validMove;
        } else {
            // Handle the case when no valid move is found (e.g., board is full)
            // You may want to return a special value or handle it as appropriate for your game.
            return -1; // Or another suitable value or action
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

private void buildTree(TreeNode node, int depth, int currentPlayer) {
    if (depth == 0 || Game.winning(node.getBoard().getBoardState(), gameN) != 0) {
        return;
    }

    int[] availableMoves = getAvailableMoves(node.getBoard());

    for (int move : availableMoves) {
        Board newBoard = node.getBoard().getNewBoard(move, currentPlayer);
        TreeNode childNode = new TreeNode(newBoard, move);
        node.addChild(childNode);
        buildTree(childNode, depth - 1, (currentPlayer == playerId) ? player2ID : playerId);
    }
}

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

    private int evaluatePosition(Board board, int playerId) {
        // Implement your static evaluation function here
        // You can use the provided heuristic object to evaluate the board state
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

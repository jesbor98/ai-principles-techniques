/**
 * Artificial Intelligence: Principles & Techniques
 * Authors: Amanda Enhörning (s1128126) and Jessica Borg (s1129470)
 * Assignment 1: N-in-a-Row
 * 6 October 2023
 */

package NRow;

import java.util.ArrayList;
import java.util.List;

/**
 * The TreeNode class represents a node in a tree used for game state exploration.
 * Each node stores a game board state, a move representing the action taken to reach this state,
 * and a list of child nodes representing possible future states.
 */
public class TreeNode {
    private Board board;
    private int move;
    private List<TreeNode> children; 

    /**
     * Constructs a new TreeNode with the specified board state and move.
     * @param board represents the game board state associated with this node.
     * @param move represents the move representing the action taken to reach this state.
     */
    public TreeNode(Board board, int move) {
        this.board = board;
        this.move = move;
        this.children = new ArrayList<>();
    }

    /**
     * Gets the game board associated with this node.
     * @return the game board state.
     */
    public Board getBoard() {
        return board;
    }

    /**
     * Gets the move representing the action taken to reach this state.
     * @return the move associated with this node.
     */
    public int getMove() {
        return move;
    }

    /**
     * Gets the list of child nodes representing possible future states.
     *
     * @return A list of child nodes.
     */
    public List<TreeNode> getChildren() {
        return children;
    }

    /**
     * Adds a child node to this node's list of children.
     *
     * @param child The child node to add.
     */
    public void addChild(TreeNode child) {
        children.add(child);
    }
}

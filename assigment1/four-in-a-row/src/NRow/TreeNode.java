package NRow;
import java.util.*;
public class TreeNode {
    private Board gameState;
    private int currentPlayer; // Player to move in this state

    private List<TreeNode> children;

    public TreeNode(Board gameState, int currentPlayer) {
        this.gameState = gameState;
        this.currentPlayer = currentPlayer;
        this.children = new ArrayList<>();
    }

    // Getter and setter methods for gameState, currentPlayer, and value
    // Methods to add and access children nodes
}


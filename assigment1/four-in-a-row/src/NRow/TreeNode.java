package NRow;

import java.util.ArrayList;
import java.util.List;

public class TreeNode {
    private Board board;
    private int move;
    private List<TreeNode> children;

    public TreeNode(Board board, int move) {
        this.board = board;
        this.move = move;
        this.children = new ArrayList<>();
    }

    public Board getBoard() {
        return board;
    }

    public int getMove() {
        return move;
    }

    public List<TreeNode> getChildren() {
        return children;
    }

    public void addChild(TreeNode child) {
        children.add(child);
    }
}

package amazonsChessRecursive;

import java.util.ArrayList;
import java.util.List;

public class Tree {
	private Node root;
	private int depth;
	private ArrayList<Node> frontier = new ArrayList<Node>();

	public Tree(Node root) {
		this.root = root;
	}

	public void getDepth() {
		Node node = this.root;
		int depth = 0;
		while (node != null) {
			List<Node> children = node.getChildren();
			if (children.isEmpty())
				break;
			else {
				node = children.get(0);
				depth++;
			}

			this.depth = depth;
		}
	}

	public void expandFrontier() {

	}

	public void deleteFrontier() {

	}

	public Node maxValue(Node s, int alpha, int beta) {
		return null;
	}

	public Node minValue(Node s, int alpha, int beta) {
		return null;
	}
}

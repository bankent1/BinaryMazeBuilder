/**
 * Author: Travis Banken
 * Graph.java
 * 
 * 
 */

import java.util.ArrayList;

public class Graph {
	ArrayList<Node> allNodes = new ArrayList<Node>();
	private int size = 0;
	
	public void addNode(Node n) {
		n.setIndex(size);
		allNodes.add(n);
		size++;
	}
	
	public Node getNode(int i) {
		return allNodes.get(i);
	}
	
	public int size() {
		return size;
	}
}

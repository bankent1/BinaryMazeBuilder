/**
 * Author: Travis Banken
 * Graph.java
 * 
 * This is an undirected graph that is meant to be a representation of a nxn
 * grid. Edges represent adjacent tiles.
 */

import java.util.ArrayList;

public class Graph {
	ArrayList<Node> allNodes = new ArrayList<Node>();
	private int size = 0;
	
	// adds a node to graph and sets its index for future look-up
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

/**
 * Author: Travis Banken
 * Node.java
 * 
 * 
 */

import java.util.ArrayList;

public class Node {

	ArrayList<Node> neighbors = new ArrayList<Node>();
	private boolean marked = false;
	private int index = -1;
	private int[] loc = new int[2]; 

	public Node(int row, int col) {
		loc[0] = row;
		loc[1] = col;
	}
	
	public void addNeighbor(Node n) {
		neighbors.add(n);
	}
	
	public ArrayList<Node> getNeighbors() {
		return neighbors;
	}
	
	public int[] getLoc() {
		return loc;
	}
	
	public void setIndex(int i) {
		index = i;
	}
	
	public int getIndex() {
		return index;
	}
	
	public boolean checkMarked() {
		return marked;
	}
	
	public void mark() {
		marked = true;
	}
	
	public void unmark() {
		marked = false;
	}
}

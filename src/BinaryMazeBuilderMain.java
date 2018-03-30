/**
 * Author: Travis Banken
 * BinaryMazeBuilderMain.java
 * 
 * java BinaryMazeBuilder infile
 * 
 * Notes on Program: 
 * 
 * 
 */

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

public class BinaryMazeBuilderMain {
	
	public static void main(String[] args) {
		System.out.println("INIT");
		int x = 0;
//		try {
//			x = System.in.read();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		//System.out.println(x);
		
		// TODO: build blank maze
		int size = 10;
		Graph maze = buildBlankMaze(size);
		System.out.println("Area = " + maze.size());
		//System.out.println(maze.getNode(24).getNeighbors().size());
		
		// TODO: Fill maze
		Random rand = new Random();
		int r = rand.nextInt(size-2) + 1;
		System.out.println("Rand = " + r);
		Node start = maze.getNode(r);
		
		//char[][] mazeArr = initCharArr(size);
		char[][] mazeArr = buildPaths(maze, start, size);
		
		printMaze(mazeArr);
		
	}
	
	public static char[][] initCharArr(int size) {
		char[][] arr = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				arr[i][j] = '0';
			}
		}
		
		return arr;
	}
	
	public static void printMaze(char[][] mazeArr) {
		for (int i = 0; i < mazeArr.length; i++) {
			for (int j = 0; j < mazeArr[0].length; j++) {
				System.out.print(mazeArr[i][j]);
			}
			System.out.println();
		}
	}
	
	public static Graph buildBlankMaze(int size) {
		int[] colNeighbor = initArray(size);
		int rowNeighbor = -1; 
		Graph maze = new Graph();
		//char[][] maze = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				//maze[i][j] = '0';
				Node node = new Node(i, j);
				if (j == 0) {
					rowNeighbor = -1;
				}
				if (rowNeighbor != -1) {
					node.addNeighbor(maze.getNode(rowNeighbor));
					maze.getNode(rowNeighbor).addNeighbor(node);
				}
				if (colNeighbor[j] != -1) {
					node.addNeighbor(maze.getNode(colNeighbor[j]));
					maze.getNode(colNeighbor[j]).addNeighbor(node);
				}
				maze.addNode(node);
				rowNeighbor = node.getIndex();
				colNeighbor[j] = node.getIndex();		
			}
		}
		
		return maze;
	}
	
	public static int[] initArray(int size) {
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
			arr[i] = -1;
		}
		return arr;
	}
	
	public static char[][] buildPaths(Graph maze, Node curr, int size) {
		ArrayList<Node> stack = new ArrayList<Node>();
		//char[][] mazeArr = new char[size][size];
		char[][] mazeArr = initCharArr(size);
		Random rand = new Random();
		mazeArr[curr.getLoc()[0]][curr.getLoc()[1]] = '1';
		curr.mark();
		stack.add(curr);
		
		//while (curr.getLoc()[0] != size-1) {
		while (!stack.isEmpty() && curr.getLoc()[0] != size-1) {
			for (Node neigh : curr.getNeighbors()) {		
				ArrayList<Node> validNeigh = getValidNeigh(curr, size);
				if (!validNeigh.isEmpty()) {
					//ArrayList<Node> validChoices = validNeigh;
					// randNum 
					int r = rand.nextInt(validNeigh.size());
					Node randNode = validNeigh.get(r);
					randNode.mark();
					//curr = randNode;
					stack.add(randNode);
					// mazeArr[randNode.getLoc()[0]][randNode.getLoc()[1]] = '1';
					int[] loc = randNode.getLoc();
					mazeArr[loc[0]][loc[1]] = '1';
					//System.out.println("------");
	//				printMaze(mazeArr);
	//				System.out.println("------");
				}
			}
			curr = stack.get(stack.size()-1);
			stack.remove(stack.size()-1);
			//stack.remove(0);
		}
		
		return mazeArr;
	}
	
	public static ArrayList<Node> getValidNeigh(Node node, int size) {
		ArrayList<Node> validNodes = new ArrayList<Node>();
		for (Node n : node.getNeighbors()) {
			if (!n.checkMarked() && n.getLoc()[0] == (size-1) 
					&& n.getNeighbors().size() == 3 && validMove(n)) {
				validNodes.add(n);
			}
			else if (!n.checkMarked() && n.getNeighbors().size() == 4 && validMove(n)) {
				validNodes.add(n);
			}
		}
		return validNodes;
	}
	
	public static boolean validMove(Node n) {
		int marks = 0;
		for (Node neigh : n.getNeighbors()) {
			if (neigh.checkMarked()) {
				marks++;
			}
		}
		return marks <= 1;
	}
	
}

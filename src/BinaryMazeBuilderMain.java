/**
 * Author: Travis Banken
 * BinaryMazeBuilderMain.java
 * 
 * java BinaryMazeBuilder System.in
 * 
 * Notes on Program: This program will randomly generate an nxn maze where the
 * size n is defined by the user input. The maze being generated is a binary
 * representation of a maze with 0's being walls and 1's being paths. The maze
 * generated only has one solution.
 * 
 * 
 */

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Stack;
import java.util.Scanner;

public class BinaryMazeBuilderMain {
	
	private static boolean foundEnd = false;
	
	public static void main(String[] args){
		System.out.println("Let's generate a maze!");
		Scanner in = new Scanner(System.in);
		int size = 0;
		
//		System.out.print("Enter size of square maze: ");
//		size = in.nextInt();
//		in.close();
		
		// TODO: build blank maze
		size = 10;

		//size = 10;
		int area = size*size;
		System.out.print("Building maze...");
		Graph maze = buildBlankMaze(size);
		
		
		// picks a random start location for maze and builds the maze
		Random rand = new Random();
		int r = rand.nextInt(size-2) + 1;
		Node start = maze.getNode(r);
		
		char[][] mazeArr = initCharArr(size);
		int[] loc = start.getLoc();
		mazeArr[loc[0]][loc[1]] = '1';
		//mazeArr = recBuildPaths(maze, start, size, mazeArr, false);
		mazeArr = recBuildPaths2(maze, start, size, mazeArr);
		//mazeArr = recBackTrackBuild(maze, start, size, mazeArr);
		//char[][] mazeArr = buildPaths(maze, start, size);
		//char[][] mazeArr = bfsBuildPaths(maze, start, size);
		
		//printMaze(mazeArr);
		
		//char[][] mazeArr = bfsBuildPaths(maze, start, size);
		//char[][] mazeArr = recBacktrack(maze, start, size);
		//printMaze(mazeArr);
		//mazeArr = fixEnd(mazeArr, size);

		// prints out maze to outfile
		printMaze(mazeArr);
		
		System.out.println("...Done");
		System.out.println("Area = " + area);
		
	}
	
	/*
	 * fixEnd adds a random ending point for the maze. This is needed because no
	 * paths are generated on the last row. The ending point will be on a random 
	 * valid location on the last row. A valid location is a location that has a
	 * connecting path above it.
	 * 
	 * Returns 2d char array
	 */
	public static char[][] fixEnd(char[][] mazeArr, int size) {
		ArrayList<Integer> endpoints = new ArrayList<Integer>();
		// find all valid endpoints
		for (int i = 0; i < size; i++) {
			if (mazeArr[size-2][i] == '1') {
				//System.out.println("i = " + i);
				//ends.add(i);

				endpoints.add(i);
			}
		}
		
		// chooses a random valid endpoint and places a path there
		Random rand = new Random();
		int r = rand.nextInt(endpoints.size());
		for (int i = 0; i < size; i++) {
			if (i == endpoints.get(r)) {
				mazeArr[size-1][i] = '1';
			}
			
		}
		return mazeArr;
	}
	
	/*
	 * initCharArr creates an nxn size array with all values initialized to '0'
	 * 
	 * returns 2d char array
	 */
	public static char[][] initCharArr(int size) {
		char[][] arr = new char[size][size];
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				arr[i][j] = '0';
			}
		}
		
		return arr;
	}
	
	public static void printMaze(char[][] mazeArr){
		PrintWriter outfile = null;
		try {
			outfile = new PrintWriter("RandMaze.txt");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.exit(0);
		}
		// iterates through mazeArr and prints out all char values
		for (int i = 0; i < mazeArr.length; i++) {
			for (int j = 0; j < mazeArr[0].length; j++) {
				outfile.print(mazeArr[i][j]);
			}
			outfile.println();
		}
		outfile.close();
	}
	
	/*
	 * buildBlankMaze builds a graph representing an nxn grid with each adjacent
	 * elements being linked as neighbors.
	 * 
	 * Returns a Graph object
	 */
	public static Graph buildBlankMaze(int size) {
		int[] colNeighbor = initArray(size);
		int rowNeighbor = -1; 
		Graph maze = new Graph();
		
		for (int i = 0; i < size; i++) {
			for (int j = 0; j < size; j++) {
				Node node = new Node(i, j);
				if (i == size) { // skip last row
					continue;
				}
				if (j == 0) { // first column resets rowNeighbor
					rowNeighbor = -1;
				}
				if (rowNeighbor != -1) { // checks if element to left in grid
					node.addNeighbor(maze.getNode(rowNeighbor));
					maze.getNode(rowNeighbor).addNeighbor(node);
				}
				if (colNeighbor[j] != -1) { // checks if element above in grid
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
	
	/*
	 * initArray initializes a int array to all -1
	 * 
	 * returns a int array
	 */
	public static int[] initArray(int size) {
		int[] arr = new int[size];
		for (int i = 0; i < size; i++) {
			arr[i] = -1;
		}
		return arr;
	}
	
	/*
	 * bfsBuildPaths uses a modified breadth-first search algorithm to randomly
	 * generate the paths for the maze.
	 * 
	 * Returns a 2d char array representing the binary maze
	 */
	public static char[][] bfsBuildPaths(Graph maze, Node curr, int size) {
		ArrayList<Node> queue = new ArrayList<Node>();
		char[][] mazeArr = initCharArr(size);
		Random rand = new Random();
		
		mazeArr[curr.getLoc()[0]][curr.getLoc()[1]] = '1';
		curr.mark();
		queue.add(curr);
		
		while (!queue.isEmpty()) {
			curr = queue.get(0);
			ArrayList<Node> validNeigh = getValidNeigh(curr, size);
			ArrayList<Node> randVN = new ArrayList<Node>(); // randomized validNeigh order
			
			// randomly order neighbors
			for (int i = 0; i < validNeigh.size(); i++) { 
				int rn = rand.nextInt(validNeigh.size());
				queue.add(validNeigh.get(rn));
				randVN.add(validNeigh.get(rn));
			}
			
			// iterated through valid neighbors and place a path down
			for (Node n : randVN) {
				n.mark();
				int[] loc = n.getLoc();
				mazeArr[loc[0]][loc[1]] = '1';
				//printMaze(mazeArr);
				//System.out.println("------");
			}
			queue.remove(0);
		}
		return mazeArr;
	}

	public static char[][] recBackTrackBuild(Graph maze, Node curr, int size, char[][]mazeArr) {
		
		Stack<Node> nodesLeft = new Stack<Node>();
		curr.mark();
		int[] loc = curr.getLoc();
		mazeArr[loc[0]][loc[1]] = '1';
		nodesLeft.push(curr);
		
		while (!nodesLeft.isEmpty()) {
				ArrayList<Node> validNeigh = getValidNeigh(curr, size);
				if (!validNeigh.isEmpty()) {
					// choose random neighbor node
					Random rand = new Random();
					int r = rand.nextInt(validNeigh.size());
					Node randNode = validNeigh.get(r);
					nodesLeft.push(randNode);
					
					// make this node a path
					randNode.mark();
					loc = randNode.getLoc();
					mazeArr[loc[0]][loc[1]] = '1';
					curr = randNode;
					printMaze(mazeArr);
					System.out.println("------");
				}
				else {
					curr = nodesLeft.pop();
				}
		}
		
		return mazeArr;
		
	}
	
	public static char[][]recBuildPaths2(Graph maze, Node curr, int size, char[][]mazeArr) {
		ArrayList<Node> validNeigh = getValidNeigh(curr, size);
		int[] currLoc = curr.getLoc();
		//System.out.println("Num Valid Neighbors for loc[" + debugLoc[0] + "][" + debugLoc[1] + "]: " + validNeigh.size());
		if (validNeigh.size() == 0 || currLoc[0] == size-1) {
			if (currLoc[0] == size-1 && !foundEnd) {
				System.out.println("\nFirst end found at loc[" + currLoc[0] + "][" + currLoc[1] + "]");
				foundEnd = true;
				//mazeArr[currLoc[0]][currLoc[1]] = '1';
			}
			return mazeArr;
		}
		
		Collections.shuffle(validNeigh);
		for (int i = 0; i < validNeigh.size(); i++) {
			Node randNode = validNeigh.get(i);
			if (getValidNeigh(curr, size).contains(randNode)) {
				randNode.mark();
				int[] loc = randNode.getLoc();
				mazeArr[loc[0]][loc[1]] = '1';
				if (loc[0] == size-1 && foundEnd) {
					//System.out.println("Found end found at loc[" + loc[0] + "][" + loc[1] + "]");
					// undo path if one already on last row
					randNode.unmark();
					mazeArr[loc[0]][loc[1]] = '0';
				}
				
				//System.out.println("Printing path at [" + loc[0] + "][" + loc[1] + "]");
			}
			//printMaze(mazeArr);
			//System.out.println("------");
			mazeArr =  recBuildPaths2(maze, randNode, size, mazeArr);
		}
		return mazeArr;
	}
	
	public static ArrayList<Node> getUnvisitedNeigh(Node node) {
		ArrayList<Node> unvisitedNeigh = new ArrayList<Node>();
		for (Node n : node.getNeighbors()) {
			if (!n.checkMarked()) {
				unvisitedNeigh.add(n);
			}
		}
		return unvisitedNeigh;
	}
	
	/*
	 * getValidNeigh will search through all of node's neighbors and checks if the location is
	 * a valid location for a path.
	 * 
	 * Conditions checked:
	 * loc is already marked with a path
	 * loc is not on the border of the maze
	 * loc is a valid move (see validMove)
	 * 
	 * Returns a list of Nodes
	 */
	public static ArrayList<Node> getValidNeigh(Node node, int size) {
		ArrayList<Node> validNodes = new ArrayList<Node>();
		for (Node n : node.getNeighbors()) {
			// first check valid pos for last row and then for general row
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
	
	/*
	 * validMove is a helper function for getValidNeigh and checks if there the potential 
	 * location would be near more than 1 paths. This is meant to keep corridors in the maze only
	 * 1 'pixel' wide.
	 * 
	 * returns boolean value
	 */
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

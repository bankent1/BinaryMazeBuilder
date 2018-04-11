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
import java.util.Collections;
import java.util.Random;
import java.util.Stack;

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
		int size = 30;
		Graph maze = buildBlankMaze(size);
		System.out.println("Area = " + maze.size());
		//System.out.println(maze.getNode(24).getNeighbors().size());
		
		// TODO: Fill maze
		Random rand = new Random();
		int r = rand.nextInt(size-2) + 1;
		System.out.println("Rand = " + r);
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
		
		mazeArr = fixEnd(mazeArr, size);
		
		printMaze(mazeArr);
		
	}
	
	public static char[][] fixEnd(char[][] mazeArr, int size) {
		ArrayList<Integer> ends = new ArrayList<Integer>();
		for (int i = 0; i < size; i++) {
			if (mazeArr[size-2][i] == '1') {
				//System.out.println("i = " + i);
				ends.add(i);
			}
		}
		Random rand = new Random();
		int r = rand.nextInt(ends.size());
		
		for (int i = 0; i < size; i++) {
			if (i == ends.get(r)) {
				mazeArr[size-1][i] = '1';
			}
			
		}
		return mazeArr;
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
				if (i == size-1) {
					continue;
				}
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
		//for (Node neigh : curr.getNeighbors()) {
		while (!stack.isEmpty() && curr.getLoc()[0] != size-1) {
			//for (Node neigh : curr.getNeighbors()) {		
				ArrayList<Node> validNeigh = getValidNeigh(curr, size);
				if (!validNeigh.isEmpty()) {
					//ArrayList<Node> validChoices = validNeigh;
					// randNum 
					for (int i = 0; i < validNeigh.size(); i++) {
						int r = rand.nextInt(validNeigh.size());
						Node randNode = validNeigh.get(r);
						randNode.mark();
						//curr = randNode;
						stack.add(randNode);
						// mazeArr[randNode.getLoc()[0]][randNode.getLoc()[1]] = '1';
						int[] loc = randNode.getLoc();
						mazeArr[loc[0]][loc[1]] = '1';
					}
					//System.out.println("------");
					printMaze(mazeArr);
					System.out.println("------");
				}

			//}
			curr = stack.get(stack.size()-1);
			stack.remove(stack.size()-1);

			//stack.remove(0);
		}
		
		return mazeArr;
	}
	
	public static char[][] bfsBuildPaths(Graph maze, Node curr, int size) {
		ArrayList<Node> queue = new ArrayList<Node>();
		//char[][] mazeArr = new char[size][size];
		char[][] mazeArr = initCharArr(size);
		Random rand = new Random();
		mazeArr[curr.getLoc()[0]][curr.getLoc()[1]] = '1';
		curr.mark();
		queue.add(curr);
		
		while (!queue.isEmpty()) {
			curr = queue.get(0);
			//queue.addAll(curr.getNeighbors());
			//mazeArr[curr.getLoc()[0]][curr.getLoc()[1]] = '1';
			ArrayList<Node> validNeigh = getValidNeigh(curr, size);
			ArrayList<Node> subVN = new ArrayList<Node>();
			//subVN.addAll(curr.getNeighbors());
			for (int i = 0; i < validNeigh.size(); i++) {
				int rn = rand.nextInt(validNeigh.size());
				queue.add(validNeigh.get(rn));	
				subVN.add(validNeigh.get(rn));
			}
			
			for (Node n : subVN) {
				n.mark();
				int[] loc = n.getLoc();
				mazeArr[loc[0]][loc[1]] = '1';
//				printMaze(mazeArr);
//				System.out.println("------");
			}
			//curr.mark();
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
		int[] debugLoc = curr.getLoc();
		//System.out.println("Num Valid Neighbors for loc[" + debugLoc[0] + "][" + debugLoc[1] + "]: " + validNeigh.size());
		if (validNeigh.size() == 0) {
			return mazeArr;
		}
		
		Collections.shuffle(validNeigh);
		for (int i = 0; i < validNeigh.size(); i++) {
			//System.out.println("FOR i = " + i);
//			Random rand = new Random();
//			int r = rand.nextInt(validNeigh.size());
			//validNeigh.get(i).mark();
			Node randNode = validNeigh.get(i);
			if (getValidNeigh(curr, size).contains(randNode)) {
				randNode.mark();
				int[] loc = randNode.getLoc();
				mazeArr[loc[0]][loc[1]] = '1';
				//System.out.println("Printing path at [" + loc[0] + "][" + loc[1] + "]");
			}
			//printMaze(mazeArr);
			//System.out.println("------");
			mazeArr =  recBuildPaths2(maze, randNode, size, mazeArr);
		}
		return mazeArr;
	}
	
	public static char[][] recBuildPaths(Graph maze, Node curr, int size, char[][] mazeArr, boolean found) {
		mazeArr[curr.getLoc()[0]][curr.getLoc()[1]] = '1';

		if (curr.getLoc()[0] == size-2) {
			found = true;
			//return mazeArr;	

			return mazeArr;
		}
		
		ArrayList<Node> validNeigh = getValidNeigh(curr, size);
		for (int i = 0; i < validNeigh.size(); i++) {
			Random rand = new Random();
			System.out.println("Choices: " + validNeigh.size());
			int r = rand.nextInt(validNeigh.size());
			Node randNode = validNeigh.get(r);
			//randNode.mark();
			if (getValidNeigh(curr, size).contains(randNode)) {
				randNode.mark();
				int[] loc = randNode.getLoc();
				mazeArr[loc[0]][loc[1]] = '1';
				//return recBuildPaths(maze, randNode, size, mazeArr, found);
			}
			printMaze(mazeArr);
			System.out.println("------");
			//return recBuildPaths(maze, randNode, size, mazeArr, found);
//			if (found) {
//				return mazeArr;
//			}
		}
		
		return mazeArr;
	}
	
	
	public static ArrayList<Node> getValidNeigh(Node node, int size) {
		ArrayList<Node> validNodes = new ArrayList<Node>();
		for (Node n : node.getNeighbors()) {
			if (!n.checkMarked() && n.getLoc()[0] == (size-2) 
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

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

public class BinaryMazeBuilderMain {
	
	public static void main(String[] args) {
		System.out.println("INIT");
		int x = 0;
		try {
			x = System.in.read();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		System.out.println(x);
		
		
	}
}

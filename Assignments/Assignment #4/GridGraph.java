//CPSC 331 Assignment 4  
//Ali Akbari

package hw4.student;

import java.io.*;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Class to represent a Weighted Grid Graph. Methods are 
 * provided to build a graph from a file and to find 
 * shortest paths.
 * 
 */

public class GridGraph implements GridGraphInterface {
	
	//static variables need for the methods below
	public int [][] matrix;
	public int [][] adjMatrix;
	public int dimensions;
	public static int vertices;
	public static int INF = 999999;
	public static int negativeINF = -9999999;
	
	
	/**
	 * Default constructor 
	 */
	public GridGraph( ) {
		
	}
	
	public static void main(String args[]) throws FileNotFoundException {
		GridGraph grid = new GridGraph();
		try {
			grid.buildGraph("maze.txt");
			
			int v1 = 1;
			int v2 = 12;
			grid.findShortestPath(v1, v2, true);
			
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		}
	}
	
  
	/**
	 * Builds a grid graph from a specified file. It is assumed
	 * that the input file is formatted correctly.
	 * 
	 * @param filename
	 */
	public void buildGraph( String filename ) throws FileNotFoundException {
		//Making a file object for scanner
		File aFile = new File(filename);
		//Scanner object
		Scanner fileReader = new Scanner(aFile);
		//First row is equal to dimension of maze
		dimensions = Integer.parseInt(fileReader.nextLine());
		//Counter to see when the third value is being read from a line
		int counter = 1;
		//Temp 2-d list for printing the values from file
		int [][] matrix = new int[dimensions][dimensions-1];
		//Global matrix that is squared of the dimensions
		adjMatrix = new int [dimensions*dimensions][dimensions*dimensions];
		//While there is next line in the file 
		while (fileReader.hasNext()) {
			//Loops through the rows	
			for (int row = 0; row < dimensions; row++) {
				//Loops through the columns
				for (int column = 0; column < dimensions-1; column++) {
					//If there is no next value, break
					if (fileReader.hasNext() == false) {
						break;
					}
					//Temporary string from the column value 
					String tempEl = fileReader.next();
					//Temporary int value of that string
					int intTempEl = Integer.parseInt(tempEl);
					//Adding the temporary value to the matrix
					matrix[row][column] = intTempEl;
					//If there is edge which is known by the third value from each row in the file
					//Add to the adjacency matrix
					if (counter%3==0) {
						adjMatrix[matrix[row][column-2]-1][matrix[row][column-1]-1] = intTempEl;

					}
					//Counter increment
					counter ++;
			
				}
			}
			
		}
		//Close the file
		fileReader.close();

	}	
	

	/**
	 * Method to find the smallest vertex and store  
	 * @param distanceList
	 * @param visitedVertices
	 * @return
	 */
	public static int priorityQueueMinimum(int[] distanceList, boolean visitedVertices[]) {
		int priorityQueueMinimumVal = negativeINF;
		int whileCounter = 0;
		while(whileCounter< distanceList.length ) {
			if(!visitedVertices[whileCounter] && (priorityQueueMinimumVal == negativeINF || distanceList[whileCounter] < distanceList[priorityQueueMinimumVal])){
				priorityQueueMinimumVal = whileCounter;
			}
			whileCounter++;
		}
		return priorityQueueMinimumVal;
	}
	
	/**
	 * Finds the shortest path between a source vertex and a target vertex
	 * using Dijkstra's algorithm. 
	 * @param s Source vertex (one based index)
	 * @param t Target vertex (one based index) 
	 * @param weighted Whether edge weights should be used or not.
	 * @return A String encoding the shortest path. Vertices are
	 * separated by whitespace.  
	 */
	public String findShortestPath( int s, int t, boolean weighted ) {
		int adjMatrixLength = dimensions*dimensions;
		StringBuilder aStr = new StringBuilder();
		Stack<Integer> myStack = new Stack<Integer>();
		int predList[] = new int[adjMatrixLength];
		boolean visitedVertices[] = new boolean[adjMatrixLength];
		int distanceList[] = new int[adjMatrixLength];
		int updatedDistance;
		
		//Check if it is weighted  
		if (!weighted) {
		//Traverse through the matrix
			for (int row = 0; row < adjMatrixLength; row++) {
				for (int column = 0; column < adjMatrixLength; column++) {
					//Change the zeros to 1
					if (adjMatrix[row][column] != 0) {
						adjMatrix[row][column] = 1;
					}
				}
			}
			
		}
		//Changes all distances 
		int whileCounter = 1;
		while(whileCounter < adjMatrixLength) {
			distanceList[whileCounter] = INF;
			whileCounter ++;
		}
		//Offset for the starting of s
		distanceList[s-1] = 0;
		
		//Loop and update the vertices that were visited
		for (int i = 0; i < adjMatrixLength - 1; i++) {
			int priorityQueueMinimumVal = priorityQueueMinimum(distanceList, visitedVertices);
			visitedVertices[priorityQueueMinimumVal] = true;
			
			//Loop and update the shortest distance 
			whileCounter = 0;
			while(whileCounter < adjMatrixLength) {
				//If vertex is not visited and value and the vertex distance is not infinity
				//Update the distance
				if (adjMatrix[priorityQueueMinimumVal][whileCounter] != 0 && !visitedVertices[whileCounter] && distanceList[priorityQueueMinimumVal] != INF) {
					updatedDistance = distanceList[priorityQueueMinimumVal] + adjMatrix[priorityQueueMinimumVal][whileCounter];
					if (updatedDistance < distanceList[whileCounter]) {
						//Predecessor list updating with the lowest value of the vertex path
						predList[whileCounter] = priorityQueueMinimumVal + 1;
						distanceList[whileCounter] = updatedDistance;
					}
				}
				//Loop counter increment
				whileCounter ++;
			}
			
		}
		//Value of the target vertex
		int i = t;
		//Try and catch for exceptions, index and IO exceptions are thrown without try and catch
		try {
			//Loop the predecessor list and if it is not equal to the starting vertex push the value into stack
			while (predList[i-1] != s) {
				myStack.push(predList[i-1]);
				//Update i to the next value in the list
				i = predList[i-1];
			}
			
			//String builder object, first appends the starting vertex
			aStr.append(s);
			
			//Size of the stack
			int size = myStack.size();
			
			//Traverse through the stack and pop the values, and append those values with white space to the string builder
			for (int j = 0; j < size; j++) {
				aStr.append(" ");
				aStr.append(myStack.pop());
			}
			//Append the target vertex at the end 
			aStr.append(" ");
			aStr.append(t);
			//Return the string builder  
			return aStr.toString();
		} catch (Exception e) {
			// TODO: handle exception
			//If error or exception return empty string
			return "";
		}
		}
	
	}


	







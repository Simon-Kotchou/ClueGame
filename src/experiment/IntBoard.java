package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
*Author: Simon Kotchou 
*Date: 10/8/2019
*/

public class IntBoard {
	private Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();			//Map that includes every boardcell on grid and a set of adjacent cells
	private Set<BoardCell> visited;								//set of boardcells that have been visited by a function
	private Set<BoardCell> targets;								//Set of boardcells that are the targets when moving a player
	private BoardCell[][] grid;									//A grid representing the game board
	
	public IntBoard() {									//default constructor that sets up adjMatrix using calcAdjacencies()
		grid = fillGrid();
		calcAdjacencies();
	}
	
	public BoardCell[][] fillGrid() {						//function that creates a new grid and fills it with corresponding boardcells
		BoardCell[][] temp = new BoardCell[4][4];
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				temp[i][j] = new BoardCell(i,j);
			}
		}
		return temp;
	}
	
	public void calcAdjacencies() {							//Function that will fill adjMatrix with every boardcell and the boardcells adjacent
		for(int i = 0; i < 4; i++) {
			for(int j = 0; j < 4; j++) {
				Set<BoardCell> temp = new HashSet<BoardCell>();
				BoardCell aCell = grid[i][j];
				if(i - 1 >= 0) {
					temp.add(grid[i-1][j]);			//checks if each side is out of range of array and if not adds to set
				}
				if(i + 1 <= 3) {
					temp.add(grid[i+1][j]);
				}
				if(j - 1 >= 0) {
					temp.add(grid[i][j-1]);
				}
				if(j + 1 <= 3) {
					temp.add(grid[i][j+1]);
				}
				
				adjMatrix.put(aCell, temp);			//adds pair of cell and adj cells from temp set
			}
		}
	}
	
	public Set<BoardCell> getAdjList(BoardCell aCell){								//getter for adjMatrix
		return adjMatrix.get(aCell);
	}
	
	public void calcTargets(BoardCell startCell, int pathLength){		//Function that calculates the targets for a move and stores them in set of targets
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		
		visited.add(startCell);
		
		findTargets(startCell, pathLength);
	}
	
	public void findTargets(BoardCell currentCell, int length) {			//recursive function
		for(BoardCell adjCell : adjMatrix.get(currentCell)) {				//for every adjacent cell of the current cell
			if(!visited.contains(adjCell)) {					//if not visited
				visited.add(adjCell);						//is now visited
				if(length == 1) {
					targets.add(adjCell);					//base case
				}
				else {
					findTargets(adjCell, length -1);				//recursive call
				}
				visited.remove(adjCell);
			}
		}
	}
	
	public Set<BoardCell> getTargets(){									//getter for targets
		return targets;
	}
	
	public BoardCell getCell(int r, int c) {				//getter for each cell on grid
		return grid[r][c];
	}
	
	
}

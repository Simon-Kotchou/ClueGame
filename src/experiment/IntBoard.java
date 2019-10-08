package experiment;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class IntBoard {
	private Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();			//Map that includes every boardcell on grid and a set of adjacent cells
	private Set<BoardCell> visited;								//set of boardcells that have been visited by a function
	private Set<BoardCell> targets;								//Set of boardcells that are the targets when moving a player
	private BoardCell[][] grid;									//A grid representing the game board
	
	public IntBoard() {									//default constructor that sets up adjMatrix using calcAdjacencies()
		calcAdjacencies();
		grid = fillGrid();
	}
	
	public BoardCell[][] fillGrid() {
		BoardCell[][] grid = new BoardCell[3][3];
		for(int i = 0; i < grid.length; i++) {
			for(int j = 0; j < 3; j++) {
				grid[i][j] = new BoardCell(i,j);
			}
		}
		return grid;
	}
	
	public void calcAdjacencies() {							//Function that will fill adjMatrix with every boardcell and the boardcells adjacent
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 3; j++) {
				Set<BoardCell> temp = new HashSet<BoardCell>();
				BoardCell aCell = grid[i][j];
				if(i - 1 >= 0) {
					temp.add(grid[i-1][j]);
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
				
				adjMatrix.put(aCell, temp);
			}
		}
	}
	
	public Set<BoardCell> getAdjList(BoardCell aCell){								//getter for adjMatrix
		return adjMatrix.get(aCell);
	}
	
	public void calcTargets(BoardCell startCell, int pathLength){		//Function that calculates the targets for a move and stores them in set of targets
		return;
	}
	
	public Set<BoardCell> getTargets(){									//getter for targets
		return null;
	}
	
	public BoardCell getCell(int r, int c) {
		return grid[r][c];
	}
	
	
}

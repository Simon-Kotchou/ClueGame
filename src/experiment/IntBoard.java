package experiment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class IntBoard {
	private Map<BoardCell, Set<BoardCell>> adjMatrix;			//Map that includes every boardcell on grid and a set of adjacent cells
	private Set<BoardCell> visited;								//set of boardcells that have been visited by a function
	private Set<BoardCell> targets;								//Set of boardcells that are the targets when moving a player
	private BoardCell[][] grid;									//A grid representing the game board
	
	public IntBoard() {									//default constructor that sets up adjMatrix using calcAdjacencies()
		calcAdjacencies();
	}
	
	public void calcAdjacencies() {							//Function that will fill adjMatrix with every boardcell and the boardcells adjacent
		return;
	}
	
	public Set<BoardCell> getAdjList(BoardCell aCell){								//getter for adjMatrix
		return null;
	}
	
	public void calcTargets(BoardCell startCell, int pathLength){		//Function that calculates the targets for a move and stores them in set of targets
		return;
	}
	
	public Set<BoardCell> getTargets(){									//getter for targets
		return null;
	}
	
	public BoardCell getCell(int r, int c) {
		return null;
	}
	
	
}

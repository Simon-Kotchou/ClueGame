package clueGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
	public static final int MAX_BOARD_SIZE = 50;					//max board size constant
	private int numRows = 25;
	private int numColumns = 25;									//row and column variables
	
	private BoardCell[][] board= new BoardCell[numRows][numColumns];			//creating new board of numRow and numCol	
	Map<Character,String> legend = new HashMap<Character,String>();				//creating new hashmap for legend
	Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();		//creating new hashmap for adjmatrix
	Set<BoardCell> targets = new HashSet<BoardCell>();									//creating new hashset for targets
	
	String boardConfigFile;
	String roomConfigFile;							//strings holding the config file names
	
	private static Board theInstance = new Board();			//static Board object that calls private constructor
	
	private Board() {

	}
	
	public static Board getInstance() {			//returns the instance of the board
		return theInstance;
	}
	
	public void initialize() {							//populates board with boardcells
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				board[i][j] = new BoardCell(i,j);
			}
		}
		return;
	}
	
	public void loadRoomConfig() {					//loads the room configuration from a txt file
		return;
	}
	
	public void loadBoardConfig() {				//loads board configuration from csv file
		return;
	}
	
	public void calcAdjacencies() {					//calculates the adjacent boardcells from a given boardcell
		return;
	}
	
	public void calcTargets(BoardCell cell, int pathLength) {				//calculates the places the piece can move
		return;
	}
	
	public Set<BoardCell> getTargets(){									//getter for targets
		return targets;
	}
	
	public BoardCell getCellAt(int r, int c) {				//getter for each cell on grid
		return board[r][c];
	}
	
	public Set<BoardCell> getAdjList(BoardCell aCell){								//getter for adjMatrix
		return adjMatrix.get(aCell);
	}
	
	public int getNumRows() {										//getter for numRows
		return numRows;
	}
	
	public int getNumColumns() {											//getter for numCols
		return numColumns;
	}
	
	public void setConfigFiles(String boardConfig, String roomConfig) {			//setter for configFile names
		this.roomConfigFile = roomConfig;
		this.boardConfigFile = boardConfig;
	}
	
	public Map<Character,String> getLegend(){										//getter for legend
		return legend;
	}
}

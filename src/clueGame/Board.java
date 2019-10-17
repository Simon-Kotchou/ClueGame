package clueGame;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class Board {
	public static final int MAX_BOARD_SIZE = 50;
	private int numRows = 25;
	private int numColumns = 25;
	
	private BoardCell[][] board= new BoardCell[numRows][numColumns];
	Map<Character,String> legend = new HashMap<Character,String>();
	Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();
	Set<BoardCell> targets = new HashSet<BoardCell>();
	
	String boardConfigFile;
	String roomConfigFile;
	
	private static Board theInstance = new Board();
	
	private Board() {

	}
	
	public static Board getInstance() {
		return theInstance;
	}
	
	public void initialize() {
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				board[i][j] = new BoardCell(i,j);
			}
		}
		return;
	}
	
	public void loadRoomConfig() {
		return;
	}
	
	public void loadBoardConfig() {
		return;
	}
	
	public void calcAdjacencies() {
		return;
	}
	
	public void calcTargets(BoardCell cell, int pathLength) {
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
	
	public int getNumRows() {
		return numRows;
	}
	
	public int getNumColumns() {
		return numColumns;
	}
	
	public void setConfigFiles(String boardConfig, String roomConfig) {
		this.roomConfigFile = roomConfig;
		this.boardConfigFile = boardConfig;
	}
	
	public Map<Character,String> getLegend(){
		return legend;
	}
}

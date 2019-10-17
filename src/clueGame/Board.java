package clueGame;

import java.util.Map;
import java.util.Set;

public class Board {
	public static final int MAX_BOARD_SIZE = 50;
	private int numRows;
	private int numColumns;
	
	private BoardCell[][] board;
	Map<Character,String> legend;
	Map<BoardCell, Set<BoardCell>> adjMatrix;
	Set<BoardCell> targets;
	
	String boardConfigFile;
	String roomConfigFile;
	
	private static Board theInstance = new Board();
	
	private Board() {
		
	}
	
	public static Board getInstance() {
		return theInstance;
	}
	
	public void initialize() {
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

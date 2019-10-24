package clueGame;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class Board {
	public static final int MAX_BOARD_SIZE = 500;					//max board size constant
	private int numRows;
	private int numColumns;									//row and column variables
	
	private BoardCell[][] board;			//creating new board of numRow and numCol	
	private Map<Character,String> legend = new HashMap<Character,String>();				//creating new hashmap for legend
	private Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();		//creating new hashmap for adjmatrix
	private Set<BoardCell> targets = new HashSet<BoardCell>();									//creating new hashset for targets
	private Set<BoardCell> visited;	
	
	private String boardConfigFile;
	private String roomConfigFile;							//strings holding the config file names
	
	private static Board theInstance = new Board();			//static Board object that calls private constructor
	
	private Board() {

	}
	
	public static Board getInstance() {			//returns the instance of the board
		return theInstance;
	}
	
	public void initialize() {							//calls functions that are needed to initialize the board
		try {
			loadRoomConfig();				//try block that tests if loadRoomConfig and loadBoardConfig do not throw exceptions
			loadBoardConfig();
			calcAdjacencies();
		}
		catch(Exception e) {
			e.getMessage();
		}
		
		return;
	}
	
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException{					//loads the room configuration from a txt file
		File file = new File(roomConfigFile);
		try(Scanner read = new Scanner(file)) {						//try block to test if the file is found
			String tempStr;
			String[] lineFields;
			ArrayList<String> knownTileTypes = new ArrayList();			//arraylist of correct types of tiles
			knownTileTypes.add("Other");
			knownTileTypes.add("Card");
			while(read.hasNextLine()) {									//while the file has another line
				tempStr = read.nextLine();									//read in the line then split it using comma delimination
				lineFields = tempStr.split(", ");
				if(lineFields.length != 3 || !knownTileTypes.contains(lineFields[2])) {											//testing proper format
					throw new BadConfigFormatException("An entry in the room config file does not have the proper format.");
				}
				else {
					legend.put(lineFields[0].charAt(0), lineFields[1]);					//if format correct will put it into the legend 
				}
			}
		}
		catch(FileNotFoundException e){					//catches file not found exception
			e.printStackTrace();
		}
		return;
	}
	
	public void loadBoardConfig() throws BadConfigFormatException{				//loads board configuration from csv file
		File file = new File(boardConfigFile);
		try(Scanner read = new Scanner(file)){
			String tempLine;
			int colCount = 0;											//trys to see if file is found 
			int prevCount = 0;
			int counter = 0;
			ArrayList<String[]> rowFields = new ArrayList();					//arraylist of rows of characters
			
			while(read.hasNextLine()) {										//while the file has another line
				tempLine = read.nextLine();								//reads the line then splits with comma delimination
				String[] lineFields = tempLine.split(",");
				rowFields.add(lineFields);									//adds to list
				colCount = lineFields.length;									//updates new column count
				if(colCount != prevCount && counter != 0) {												//if column count changed from previous throws exception
					throw new BadConfigFormatException("Not same amount of columns in every row.");
				}
				else {
					prevCount = colCount;							//if not will update the previous with the current
					counter++;
				}
			}
			
			numRows = rowFields.size();								//sets dimensions of board
			numColumns = colCount;
			
			board = new BoardCell[numRows][numColumns];								//creates initial board with proper dimensions
			
			for(int i = 0; i < numRows; i++) {
				for(int j = 0; j < numColumns; j++) {										//for every cell on board
					if(!legend.containsKey(rowFields.get(i)[j].charAt(0))) {											//if the initial is found in the legend
						throw new BadConfigFormatException("Config file refers to a room that is not in room config file.");
					}
					else {
						board[i][j] = new BoardCell(i, j, rowFields.get(i)[j].charAt(0));			//will create a new boardcell with correct information passed to constructor
						if(rowFields.get(i)[j].length() > 1) {									//if initial is greater than 1 length
							if(rowFields.get(i)[j].charAt(1) == 'U') {								//will check which door direction it is and set it to the instance variable in the boardcell class
								board[i][j].setDoorDirection(DoorDirection.UP);
							}
							else if(rowFields.get(i)[j].charAt(1) == 'D') {
								board[i][j].setDoorDirection(DoorDirection.DOWN);
							}
							else if(rowFields.get(i)[j].charAt(1) == 'L') {
								board[i][j].setDoorDirection(DoorDirection.LEFT);
							}
							else if(rowFields.get(i)[j].charAt(1) == 'R') {
								board[i][j].setDoorDirection(DoorDirection.RIGHT);
							}
						}
						else {
							board[i][j].setDoorDirection(DoorDirection.NONE);
						}
					}
				}
			}
			read.close();								//closes scanner
		}
		catch(FileNotFoundException e) {			//catches file not found exception
			e.printStackTrace();
		}
		return;
	}
	
	public void calcAdjacencies() {					//calculates the adjacent boardcells from a given boardcell
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				Set<BoardCell> temp = new HashSet<BoardCell>();
				BoardCell aCell = board[i][j];
				if(i - 1 >= 0) {
					temp.add(board[i-1][j]);			//checks if each side is out of range of array and if not adds to set
				}
				if(i + 1 <= 3) {
					temp.add(board[i+1][j]);
				}
				if(j - 1 >= 0) {
					temp.add(board[i][j-1]);
				}
				if(j + 1 <= 3) {
					temp.add(board[i][j+1]);
				}
				
				adjMatrix.put(aCell, temp);			//adds pair of cell and adj cells from temp set
			}
		}
		return;
	}
	
	public void calcTargets(int r, int c, int pathLength) {				//calculates the places the piece can move
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		
		visited.add(this.getCellAt(r, c));
		
		findTargets(this.getCellAt(r, c), pathLength);
		return;
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
	
	public BoardCell getCellAt(int r, int c) {				//getter for each cell on grid
		return board[r][c];
	}
	
	public Set<BoardCell> getAdjList(int r, int c){								//getter for adjMatrix
		return adjMatrix.get(this.getCellAt(r, c));
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
	
	public void setNumRows(int numRows) {				//setter for numRows
		this.numRows = numRows;
	}
	
	public void setNumCols(int numCols) {				//setter for numCols
		this.numColumns = numCols;
	}
}

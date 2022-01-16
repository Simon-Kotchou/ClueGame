package clueGame;

import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import javafx.util.Pair;

public class Board extends JPanel{
	public static final int MAX_BOARD_SIZE = 60;					//max board size constant
	private int numRows;
	private int numColumns;									//row and column variables
	
	private Solution theSolution;					//the solution to the game
	
	private BoardCell[][] board;			//creating new board of numRow and numCol	
	private Map<Character,String> legend = new HashMap<Character,String>();				//creating new hashmap for legend
	
	private ArrayList<Player> players = new ArrayList<Player>();
	private Player currentPlayer;
	
	private ArrayList<Card> people = new ArrayList<Card>();
	private ArrayList<Card> weapons = new ArrayList<Card>();				//new arraylists to hold cards
	private ArrayList<Card> rooms = new ArrayList<Card>();
	private ArrayList<Card> deck = new ArrayList<Card>();
	private ArrayList<Card> mainDeck = new ArrayList<Card>();
	private ArrayList<Card> mainWeapons = new ArrayList<Card>();
	private ArrayList<Card> mainPeople = new ArrayList<Card>();
	private ArrayList<Card> mainRooms = new ArrayList<Card>();

	private Map<BoardCell, Set<BoardCell>> adjMatrix = new HashMap<BoardCell, Set<BoardCell>>();		//creating new hashmap for adjmatrix
	private Set<BoardCell> targets = new HashSet<BoardCell>();									//creating new hashset for targets
	private Set<BoardCell> visited;	
	
	private Random rand;														//random object for rolling dice
	private int currentRoll;										
	private ClickListener cL;													//clicklistener obj
	
	private String boardConfigFile;
	private String roomConfigFile;							//strings holding the config file names
	private String peopleConfigFile;
	
	private boolean computerAccusation = false;
	private Solution computerAccusationGuess;
	
	private static Board theInstance = new Board();			//static Board object that calls private constructor
	
	private Board() {

	}
	
	public static Board getInstance() {			//returns the instance of the board
		return theInstance;
	}
	
	public void initialize() {							//calls functions that are needed to initialize the board
		try {
			loadPeopleToDeck();
			loadRoomConfig();				//try block that tests if loadRoomConfig and loadBoardConfig do not throw exceptions
			loadBoardConfig();
			calcAdjacencies();
			loadPlayersConfig();
			loadWeaponsConfig();
			selectSolution();
			dealCards();
		}
		catch(Exception e) {
			e.getMessage();
		}
		
		return;
	}
	
	public void loadRoomConfig() throws BadConfigFormatException, FileNotFoundException{					//loads the room configuration from a txt file
		File file = new File(roomConfigFile);
		Scanner read = new Scanner(file);
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
				if(lineFields[2].equals(knownTileTypes.get(1))) {
					rooms.add(new Card(lineFields[1], CardType.ROOM));
				}
			}
		}
		return;
	}
	
	public void loadBoardConfig() throws BadConfigFormatException, FileNotFoundException{				//loads board configuration from csv file
		File file = new File(boardConfigFile);
		Scanner read = new Scanner(file);
		String tempLine;
		int colCount = 0;											//trys to see if file is found 
		int prevCount = 0;
		int counter = 0;
		String[] lineFields;
		ArrayList<String[]> rowFields = new ArrayList();					//arraylist of rows of characters
		
		while(read.hasNextLine()) {										//while the file has another line
			tempLine = read.nextLine();								//reads the line then splits with comma delimination
			lineFields = tempLine.split(",");
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
						switch(rowFields.get(i)[j].charAt(1)) {								//will check which door direction it is and set it to the instance variable in the boardcell class
						case 'U':
							board[i][j].setDoorDirection(DoorDirection.UP);
							break;
						case 'D':
							board[i][j].setDoorDirection(DoorDirection.DOWN);
							break;
						case 'L':
							board[i][j].setDoorDirection(DoorDirection.LEFT);
							break;
						case 'R':
							board[i][j].setDoorDirection(DoorDirection.RIGHT);
							break;
						case 'N':
							board[i][j].setNameLabel(legend.get(rowFields.get(i)[j].charAt(0)));			//if has n flag on second character, then will set name label to current room initial's corresponding value in map
						default:
							board[i][j].setDoorDirection(DoorDirection.NONE);
						}
					}
					else {
						board[i][j].setDoorDirection(DoorDirection.NONE);
					}
				}
			}
		}
		read.close();								//closes scanner
		return;
	}
	
	@Override
	public void paintComponent(Graphics g) {							//overrides paintComponent Jpanel method
		super.paintComponent(g);								//calls the super class with graphics obj
		Graphics2D g2 = (Graphics2D) g;							//casts graphics obj to graphics2D obj
		
		for(int i = 0; i < this.numRows; i++) {						//for every cell on board, will call the cells draw function
			for(int j = 0; j < this.numColumns; j++) {
				this.getCellAt(i, j).draw(g2);
			}
		}
		
		for(int i = 0; i < this.numRows; i++) {				//for every cell on the board, will call the cells drawRoomName function
			for(int j = 0; j < this.numColumns; j++) {
				this.getCellAt(i, j).drawRoomName(g2);
			}
		}
		
		for(Player p : players) {								//for loop that calls every player's draw function
			p.draw(g2);
		}
	}
	
	public void loadPlayersConfig() throws FileNotFoundException{
		File file = new File("data/CluePeople.txt");
		Scanner read = new Scanner(file);
		String tempLine;
		String[] lineFields;
		while(read.hasNextLine()) {										//while the file has another line
			tempLine = read.nextLine();								//reads the line then splits with comma delimination
			lineFields = tempLine.split(", ");
			if(lineFields[2].equals("human")) {
				players.add(new HumanPlayer(lineFields[0], Integer.parseInt(lineFields[3]), Integer.parseInt(lineFields[4]), lineFields[1]));		//adds player to player arraylist
			}
			else {
				players.add(new ComputerPlayer(lineFields[0], Integer.parseInt(lineFields[3]), Integer.parseInt(lineFields[4]), lineFields[1]));
			}
		}
		read.close();						//closes scanner
	}
	
	public void loadPeopleToDeck() {																							//populates the people arraylist with the preset characters of game 
		String[] peopleNames = {"Colonel Mustard", "Miss Scarlet", "Professor Plum", "Mrs. Peacock", "Mr. Green", "Mrs. White"};
		for(String s : peopleNames) {
			people.add(new Card(s, CardType.PERSON));			//for every character creates new card and adds to arraylist
		}
	}
	
	public void loadWeaponsConfig() throws FileNotFoundException{				//loads weapons from config file
		File file = new File("data/ClueWeapons.txt");
		Scanner read = new Scanner(file);
		String tempLine;
		while(read.hasNextLine()) {										//while the file has another line
			tempLine = read.nextLine();								//reads the line then splits with comma delimination
			weapons.add(new Card(tempLine, CardType.WEAPON));				//creates new weapons cards and adds them to arraylist of weapons
		}
	}
	
	private void selectSolution() {								//selects solution to game and removes from deck
		Collections.shuffle(people);							//shuffles all of the card collections
		Collections.shuffle(rooms);
		Collections.shuffle(weapons);
		
		theSolution = new Solution(people.get(0).getCardName(),rooms.get(0).getCardName(), weapons.get(0).getCardName());		//chooses the solution to the game
		
		mainDeck.addAll(people);												//adds the rest of the cards to the main deck
		mainDeck.addAll(rooms);
		mainDeck.addAll(weapons);
		mainWeapons.addAll(weapons);
		mainPeople.addAll(people);
		mainRooms.addAll(rooms);
		
		people.remove(0);
		rooms.remove(0);												//then removes the solution cards from respective decks
		weapons.remove(0);
		
		Collections.shuffle(mainDeck);							//shuffles all of the card collections
		Collections.shuffle(mainWeapons);
		Collections.shuffle(mainPeople);
		Collections.shuffle(mainRooms);
		
		deck.addAll(people);												//adds the rest of the cards to the main deck
		deck.addAll(rooms);
		deck.addAll(weapons);
	}
	
	public Card handleSuggestion(Solution suggestion, Player accuser) {						//board method to keep track of disprovals by players
		
		for(int i = players.indexOf(accuser)+1; i != players.indexOf(accuser); i++) {			//wraps around accuser index
			i = i % players.size();
			if(players.get(i).disproveSuggestion(suggestion) != null) {				//if the suggestion is not null, and it is not accueser, will return
				if(players.get(i) == accuser) {
					return null;
				}
				return players.get(i).disproveSuggestion(suggestion);
			}
		}
		return null;
	}
	
	private void dealCards() {							//method that deals the cards to each player
		Collections.shuffle(deck);								//shuffles the deck
		
		for(int i = 0, j = 0; i < deck.size(); j++, i++) {			//for every card in the deck
			if(j >= players.size()) {								//cycles through every player
				j = 0;
			}
			
			players.get(j).getPlayerHand().add(deck.get(i));			//adds a card from the deck to players hand
		}
	}
	
	public ArrayList<Card> getPeople() {						//getters for testing 
		return people;
	}
	
	public ArrayList<Card> getRooms() {
		return rooms;
	}
	
	public ArrayList<Card> getWeapons() {
		return weapons;
	}
	
	public ArrayList<Player> getPlayers() {
		return players;
	}

	public void calcAdjacencies() {					//calculates the adjacent boardcells from a given boardcell
		Set<BoardCell> temp;
		BoardCell aCell;
		ArrayList<BoardCell> myList = new ArrayList();
		for(int i = 0; i < numRows; i++) {
			for(int j = 0; j < numColumns; j++) {
				temp = new HashSet<BoardCell>();			//creating temp set
				aCell = board[i][j];							//temp boardcell
				if(!aCell.isRoom() ) {									//if the cell is a room cell then adjset is empty
					if(aCell.isDoorway()) {									//if a doorway
						switch(aCell.getDoorDirection()) {				//checks which doorway direction and adds only the cell toward its corresponding direction
						case LEFT:
							temp.add(board[i][j-1]);
							break;
						case RIGHT:
							temp.add(board[i][j+1]);
							break;
						case UP:
							temp.add(board[i-1][j]);
							break;
						case DOWN:
							temp.add(board[i+1][j]);
							break;
						}
					}
					else {										
						myList.add(putWalkwayAdjacencies(getCellAt(i+1,j), i, j));			//we know the cell is a walkway, so calls putWalkwayAdjacencies
						myList.add(putWalkwayAdjacencies(getCellAt(i-1,j), i, j));			//for every possible cell adjacent to the current cell
						myList.add(putWalkwayAdjacencies(getCellAt(i,j+1), i, j));			//and put the results in an arraylist
						myList.add(putWalkwayAdjacencies(getCellAt(i,j-1), i, j));
						for(BoardCell tempCell : myList) {									//foreach loop through the list with every valid adjacent cell
							if(tempCell != null) {											//if the adjacent cell is not valid will be null
								temp.add(tempCell);											//put every valid adjacent cell into the temperary set
							}
						}
						myList.clear();														//clears the arraylist after finished creating adjacency set
					}
				}
				
				adjMatrix.put(aCell, temp);			//adds pair of cell and adj cells from temp set
			}
		}
		return;
	}
	
	private BoardCell putWalkwayAdjacencies(BoardCell aCell, int initR, int initC) {		//private helper function that calcs valid adjacent cells for a walkway cell
		if(aCell != null) {																	//if the cell has invalid bounds will be null, so returns null
			if(!aCell.isRoom()) {															//if the cell is a room is an invalid adjacent cell so will return null
				if(aCell.isWalkway()) {														//if adjacent cell is a walkway then is valid
					return aCell;
				}
				else {																	//if it is a doorway
					switch(aCell.getRow() - initR) {									//calculates the direction of the adjacent cell with respect to initial cell
					case 1:
						if(aCell.getDoorDirection() == DoorDirection.UP) {			//if it is directed downwards from original cell, only valid adjacent doorway is a door pointed up
							return aCell;
						}
						return null;
					case -1:
						if(aCell.getDoorDirection() == DoorDirection.DOWN) {		//if it is directed upwards, only valid adjacent doorway is a door pointed down
							return aCell;
						}
						return null;
					default:														//if it is not directed vertically
						switch(aCell.getCol() - initC) {							//calculates the direction of the adjacent cell horizontally
						case 1:
							if(aCell.getDoorDirection() == DoorDirection.LEFT) {  //if it is directed to the right, only valid adjacent doorway is a door pointed to the left
								return aCell;
							}
							return null;
						case -1:
							if(aCell.getDoorDirection() == DoorDirection.RIGHT) {    //if it is directed to the left, only valid adjacent doorway is a door pointed to the right
								return aCell;
							}
							return null;
						}
					}
				}
			}
		}
		return null;														//returns null if a room or an out of bounds cell
	}
	
	public void calcTargets(int r, int c, int pathLength) {				//calculates the places the piece can move
		visited = new HashSet<BoardCell>();
		targets = new HashSet<BoardCell>();
		
		visited.add(this.getCellAt(r, c));
		
		findTargets(this.getCellAt(r, c), pathLength);
		return;
	}
	
	private void findTargets(BoardCell currentCell, int length) {			//recursive function
		for(BoardCell adjCell : adjMatrix.get(currentCell)) {				//for every adjacent cell of the current cell
			if(!visited.contains(adjCell)) {					//if not visited
				visited.add(adjCell);						//is now visited
				if(length == 1 || adjCell.isDoorway()) {			//will stop when length =1 or is a doorway
					targets.add(adjCell);					//base case
				}
				else {
					findTargets(adjCell, length -1);				//recursive call
				}
				visited.remove(adjCell);
			}
		}
	}
	
	public boolean checkAccusation(Solution accusation) {			//Method that returns true if accusation is equal to the solution to the game
		if(accusation.equals(theSolution)) {						//and false if it is not
			return true;
		}
		return false;
	}
	
	private void updatePlayerTurn() {											//method that will update whos turn it is in the game, roll die, update gui, and call to make a move
		if(currentPlayer == null) {											//if no current player, the game will start with human first
			currentPlayer = players.get(0);
		}
		else if(players.indexOf(currentPlayer) == 5) {							//if the current player is end of arraylist will loop back to front
			currentPlayer = players.get(0);
			currentPlayer.hasGuessed(false);
		}
		else {
			currentPlayer = players.get(players.indexOf(currentPlayer)+1);					//or will go to next player in list
		}
		
		ClueGame.getCtrlPanel().showTurn(currentPlayer.getPlayerName(), this.rollDie());			//will update the control panel and call to roll the die
		
		makeMove();																				//calls makeMove method below
	}
	
	private void makeMove() {																//method in charge of making the players move on board
		calcTargets(currentPlayer.getRow(), currentPlayer.getColumn(), currentRoll);			//will calculate the players targets
		
		if(currentPlayer.getPlayerType().equals("human")) {									//if humans turn
			for(BoardCell b : targets) {												//will show targets on board
				b.setTarget(true);
			}
			repaint();
			
			cL = new ClickListener();											//will create a new listener 
			
			this.addMouseListener(cL);											//adds mouse listener to board panel
			
		}
		else {
			ComputerPlayer tempPlayer = (ComputerPlayer) currentPlayer;		//if computer, will cast to temp computer player obj
			
			if(computerAccusation) {
				computerAccusation = false;
				if(this.checkAccusation(computerAccusationGuess)) {
					JOptionPane.showMessageDialog(ClueGame.getFrame(), currentPlayer.getPlayerName() + " has guessed correctly {" + computerAccusationGuess.person + ", " + computerAccusationGuess.room + ", " + computerAccusationGuess.weapon + "} and won the game.", "A player has guessed!", JOptionPane.INFORMATION_MESSAGE);
					ClueGame.getFrame().dispose();
				}
				else {
					JOptionPane.showMessageDialog(ClueGame.getFrame(), currentPlayer.getPlayerName() + " has guessed incorrectly {" + computerAccusationGuess.person + ", " + computerAccusationGuess.room + ", " + computerAccusationGuess.weapon + "}.", "A player has guessed!", JOptionPane.INFORMATION_MESSAGE);
				}
			}
			
			BoardCell tempLocation = tempPlayer.pickLocation(targets);			//call computerplayers pick location method
			currentPlayer.setRow(tempLocation.getRow());
			currentPlayer.setCol(tempLocation.getCol());					//will set the computer players position to picked location and repaint board
			repaint();
			
			if(this.getCellAt(tempLocation.getRow(), tempLocation.getCol()).isDoorway()) {				//if computer goes into room
				Solution tempGuess = tempPlayer.createSuggestion();
				ClueGame.getCtrlPanel().setGuess(tempGuess);										//will make a suggestion and board will handle it
				Card disprovedCard = this.handleSuggestion(tempGuess, tempPlayer);
				if(disprovedCard == null) {
					ClueGame.getCtrlPanel().setResult("No new clue");
					boolean tempBool = true;
					Player nextPlayer;
					if(players.indexOf(currentPlayer) == 5) {							//if the current player is end of arraylist will loop back to front
						nextPlayer = players.get(0);
					}
					else {
						nextPlayer = players.get(players.indexOf(currentPlayer)+1);					//or will go to next player in list
					}
					for(Card c : nextPlayer.getPlayerHand()) {
						if(c.getCardName().equals(tempGuess.room)) {
							tempBool = false;
						}
					}
					if(tempBool) {
						computerAccusation = true;
						computerAccusationGuess = tempGuess;
					}
				}
				else {
					if(currentPlayer.getClass() == HumanPlayer.class) {
						ClueGame.getCtrlPanel().setResult(disprovedCard.getCardName());
					}
					else {
						ClueGame.getCtrlPanel().setResult("Disproved by Player");
						currentPlayer.getSeenCards().add(disprovedCard);
					}
				}
			}
		}
			
	}
	
	public void handleHumanAccusation(String person, String room, String weapon) {					//class that will be called when accusation button is pressed at appropriate time
		if(this.checkAccusation(new Solution(person,room,weapon))) {
			JOptionPane.showMessageDialog(ClueGame.getFrame(), currentPlayer.getPlayerName() + " has guessed correctly {" + person + ", " + room + ", " + weapon + "} and won the game.", "You have guessed!", JOptionPane.INFORMATION_MESSAGE);
			ClueGame.getFrame().dispose();
		}
		else {
			JOptionPane.showMessageDialog(ClueGame.getFrame(), currentPlayer.getPlayerName() + " has guessed incorrectly {" + person + ", " + room + ", " + weapon + "}.", "You have guessed!", JOptionPane.INFORMATION_MESSAGE);
		}
		currentPlayer.hasGuessed(true);
	}
	
	private class ClickListener implements MouseListener{				//private class to implement mouseClick functionality
		
		private int x;
		private int y;												//istance variables to show coordinates of click

		@Override
		public void mouseClicked(MouseEvent e) {							//overriding inhereted method
			x = e.getX();
			y = e.getY();													//gets the coordinates of click
			
			for(BoardCell b : targets) {														//goes through every target boardcell
				if(x >= b.getColPixel() && x <= b.getColPixel() + BoardCell.SIDE_SIZE) {
					if(y >= b.getRowPixel() && y <= b.getRowPixel() + BoardCell.SIDE_SIZE) {		//if the click within its bounds
						currentPlayer.setRow(b.getRow());
						currentPlayer.setCol(b.getCol());
						currentPlayer.setMoved(true);												//will update players location and assert that the player has moved
						break;
					}
				}
			}
			if(currentPlayer.hasMoved() == false) {																								//if player did not move, error message
				JOptionPane.showMessageDialog(ClueGame.getFrame(), "Error: Invalid target selected", "Error", JOptionPane.ERROR_MESSAGE);
			}
			else {
				for(BoardCell b : targets) {														//if the player did move will remove the target markets on board gui and will remove the mouse listener from panel
					b.setTarget(false);
				}
				repaint();
				Board.getInstance().removeMouseListener(cL);
				
				if(Board.getInstance().getCellAt(currentPlayer.getRow(), currentPlayer.getColumn()).isDoorway()) {
					MakeGuessDialog guessDialog = new MakeGuessDialog(Board.getInstance().getCellAt(currentPlayer.getRow(), currentPlayer.getColumn()));
					guessDialog.setVisible(true);
				}
			}
		}

		@Override
		public void mousePressed(MouseEvent e) {
			
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			
			
		}

		@Override
		public void mouseEntered(MouseEvent e) {
			
			
		}

		@Override
		public void mouseExited(MouseEvent e) {
			
			
		}
		
	}
	
	public void handleNextButton() {											//method that is called when next button is pressed
		if(currentPlayer == null) {												//if it is the start of the game will call to update the player to human
			updatePlayerTurn();
		}
		else if(currentPlayer.getPlayerType().equals("human")) {			//if not start and it is humans turn
			if(currentPlayer.hasMoved()) {											//will make sure the human has moved, then update the player
				currentPlayer.setMoved(false);
				updatePlayerTurn();
			}
		}
		else {													//if not start and computer will just simply update the player
			updatePlayerTurn();
		}
	}
	
	public int rollDie() {
		rand = new Random();
		currentRoll = rand.nextInt(6)+1;
		return currentRoll;
	}
	
	public Set<BoardCell> getTargets(){									//getter for targets
		return targets;
	}
	
	public BoardCell getCellAt(int r, int c) {				//getter for each cell on grid
		if(r >= 0 && r < numRows && c >= 0 && c < numColumns) {
			return board[r][c];
		}
		return null;
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
		this.roomConfigFile = "data/" + roomConfig;
		this.boardConfigFile = "data/" + boardConfig;
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
	
	public ArrayList<Card> getDeck(){								//getter for testing 
		return deck;
	}
	
	public ArrayList<Card> getMainDeck(){								//getter for testing 
		return mainDeck;
	}
	
	public void setSolution(String p,String r,String w ) {				//setter for testing	
		theSolution = new Solution(p,r,w);
		
	}
	
	public ArrayList<Card> getMainWeapons() {
		return mainWeapons;
	}

	public ArrayList<Card> getMainPeople() {
		return mainPeople;
	}
	
	public void setPlayers(ArrayList<Player> pList) {
		players = pList;
	}
	
	public ArrayList<Card> getMainRooms() {
		return mainRooms;
	}
	
	public Player getCurrentPlayer() {
		return currentPlayer;
	}
	
	public void setAccusationTrue() {
		computerAccusation = true;
	}
	
	public void setAccusationGuess(Solution s) {
		computerAccusationGuess = s;
	}
}


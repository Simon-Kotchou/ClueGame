package clueGame;

public class BoardCell {
	private int row;
	private int col;							//instance variables for boardcell information
	private char initial;
	private DoorDirection doorDirection;
	
	public BoardCell(int r, int c) {				//parameterized constructor
		this.row = r;
		this.col = c;
	}
	
	public BoardCell(int r, int c, char init) {		//parameterized constructor that includes initial
		this.row = r;
		this.col = c;
		this.initial = init;
	}
	
	public boolean isWalkway() {								//checks if the boardcell is a walkway
		if(this.initial == 'W') {
			return true;
		}
		return false;
	}
	
	public boolean isRoom() {									//checks if the boardcell is a room cell
		if(this.initial != 'W') {
			return true;
		}
		return false;
	}
	
	public boolean isDoorway() {								//checks if the boardcell is a doorway
		if(this.doorDirection != DoorDirection.NONE) {
			return true;
		}
		return false;
	}
	
	public char getInitial() {									//getter for boardcell's initial
		return initial;
	}
	
	public DoorDirection getDoorDirection() {								//getter if the boardcell is a doorway it gets the direction
		return doorDirection;
	}
	
	public void setInitial(char initial) {							//setter for initial
		this.initial = initial;
	}
	
	public void setDoorDirection(DoorDirection direction) {				//setter for door direction
		this.doorDirection = direction;
	}
	
}

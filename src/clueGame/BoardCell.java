package clueGame;

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	
	public BoardCell(int r, int c) {				//parameterized constructor
		this.row = r;
		this.col = c;
	}
	
	public boolean isWalkway() {								//checks if the boardcell is a walkway
		return false;
	}
	
	public boolean isRoom() {									//checks if the boardcell is a room cell
		return false;
	}
	
	public boolean isDoorway() {								//checks if the boardcell is a doorway
		return false;
	}
	
	public char getInitial() {									//getter for boardcell's initial
		return initial;
	}
	
	public DoorDirection getDoorDirection() {								//getter if the boardcell is a doorway it gets the direction
		return null;
	}
	
}

package clueGame;

public class BoardCell {
	private int row;
	private int col;
	private char initial;
	
	public BoardCell(int r, int c) {
		this.row = r;
		this.col = c;
	}
	
	public boolean isWalkway() {
		return false;
	}
	
	public boolean isRoom() {
		return false;
	}
	
	public boolean isDoorway() {
		return false;
	}
	
	public char getInitial() {
		return initial;
	}
	
	public DoorDirection getDoorDirection() {
		return null;
	}
	
}

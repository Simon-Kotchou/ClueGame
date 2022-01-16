package clueGame;

import java.awt.Color;
import java.awt.Graphics2D;

import javax.swing.JLabel;

public class BoardCell {
	private int row;
	private int col;							//instance variables for boardcell information
	private char initial;
	private DoorDirection doorDirection;
	private String nameLabel;							//name label for cells that are to display the name of the room on GUI
	public static final int SIDE_SIZE = 40;			//constant for the size of each side of the cell's square to be drawn on GUI		
	private int rowPixel;								//x and y values for drawing on GUI
	private int colPixel;
	private static final int DOOR_WIDTH = 5;			//Constant for width of rectangle when drawing door on GUI
	private boolean isTarget = false;
	
	public BoardCell(int r, int c) {				//parameterized constructor
		this.row = r;
		this.col = c;
	}
	
	public BoardCell(int r, int c, char init) {		//parameterized constructor that includes initial
		this.row = r;
		this.col = c;
		this.initial = init;
		this.rowPixel =  row * SIDE_SIZE;			//sets pixel location to row/col count * size of side of square representing cell in GUI
		this.colPixel = col * SIDE_SIZE;
	}
	
	private Color chooseCellColor() {					//method that chooses the color that the cell will be on GUI
		if(this.isTarget()) {
			return Color.ORANGE;
		}
		else if(this.isDoorway() || this.isRoom()) {
			return Color.GRAY;
		}
		else {
			return Color.YELLOW;
		}
	}
	
	public void draw(Graphics2D g2) {								//draw method to be called by paintComponent in board class
		g2.setColor(this.chooseCellColor());							//sets the cells color to the one chosen by method above
		g2.fillRect(colPixel, rowPixel, SIDE_SIZE, SIDE_SIZE);			//draws a filled rect onto board
		if(this.isWalkway()) {											//if a walkway, will create a black boarder on GUI
			g2.setColor(Color.BLACK);
			g2.drawRect(colPixel, rowPixel, SIDE_SIZE, SIDE_SIZE);
		}
		if(this.isDoorway()) {												//if the cell is a doorway, 
			g2.setColor(Color.BLUE);
			switch(this.doorDirection) {										//based on door direction,
			case LEFT:
				g2.fillRect(colPixel, rowPixel, DOOR_WIDTH, SIDE_SIZE);				//calculates the position the rectangle should be drawn at within cell
				break;
			case RIGHT:
				g2.fillRect(colPixel+SIDE_SIZE-DOOR_WIDTH, rowPixel, DOOR_WIDTH, SIDE_SIZE);		
				break;
			case UP:
				g2.fillRect(colPixel, rowPixel, SIDE_SIZE, DOOR_WIDTH);
				break;
			case DOWN:
				g2.fillRect(colPixel, rowPixel+SIDE_SIZE-DOOR_WIDTH, SIDE_SIZE, DOOR_WIDTH);
				break;
			}
		}
	}
	
	public void drawRoomName(Graphics2D g2) {						//method to draw the name label of a room onto GUI
		if(nameLabel != null) {									//if the nameLabel of a cell is not null
			g2.setColor(Color.BLUE);
			g2.drawString(nameLabel, colPixel, rowPixel);		//then will print the name of the room held in the nameLabel variable
		}
	}
	
	public boolean isWalkway() {								//checks if the boardcell is a walkway
		if(this.initial == 'W') {
			return true;
		}
		return false;
	}
	
	public boolean isRoom() {									//checks if the boardcell is a room cell
		if(!this.isWalkway() && !this.isDoorway()) {
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
	
	public int getRow() {
		return row;
	}
	
	public int getCol() {
		return col;
	}
	
	public void setNameLabel(String label) {
		this.nameLabel = label;
	}
	
	public void setTarget(boolean b) {
		isTarget = b;
	}
	
	public boolean isTarget() {
		return isTarget;
	}
	
	public int getColPixel() {
		return colPixel;
	}
	
	public int getRowPixel() {
		return rowPixel;
	}
}

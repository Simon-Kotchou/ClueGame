package clueGame;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Random;

public class Player {
	protected String playerName;									//protected instance variables for player data
	protected String playerType;
	protected int row;
	protected int column;
	protected static final int DIAMETER_SIZE = 40;				//constant that holds diameter size of circle representing player on GUI
	protected int rowPixel;
	protected int colPixel;
	protected Color color;
	protected ArrayList<Card> cardHand = new ArrayList<Card>();
	protected ArrayList<Card> seenCards = new ArrayList<Card>();
	protected boolean hasMoved = false;
	protected boolean accusationMade = false;
	
	public Player() {
		this.playerName = null;
		this.playerType = null;										//default constructor
		this.row = 0;
		this.column = 0;
		this.color = null;
	}
	
	public Player(String name, String type, int row, int col, String color) {		//parameterized constructor to set data variables
		this.playerName = name;
		this.playerType = type;
		this.row = row;
		this.column = col;
		this.color = convertColor(color);
		this.colPixel = column * DIAMETER_SIZE;							//sets pixel location on board GUI 
		this.rowPixel = row * DIAMETER_SIZE;
	}
	
	public void draw(Graphics2D g2) {																	//draw method
		Ellipse2D circle = new Ellipse2D.Double(colPixel, rowPixel, DIAMETER_SIZE, DIAMETER_SIZE);		//creates an new ellipse representing player
		g2.setColor(color);
		g2.fill(circle);																				//sets its color and fills it 
		g2.draw(circle);																				//then draws on board
	}
	
	public Card disproveSuggestion(Solution suggestion) {									//method to disprove a suggestion
		ArrayList<Card> possibleDisprovals = new ArrayList<Card>();							//arraylist of disprovals
		Random rand = new Random();
		for(int i =0; i < this.cardHand.size(); i++) {																//for every card in hand, will chekc if equal to a part of solution
			if(cardHand.get(i).getCardName().equals(suggestion.person) || cardHand.get(i).getCardName().equals(suggestion.weapon) || cardHand.get(i).getCardName().equals(suggestion.room)) {
				possibleDisprovals.add(cardHand.get(i));
			}
		}
		if(!possibleDisprovals.isEmpty()) {											//if none are part of solution will null or if so will return a random disproval
			return possibleDisprovals.get(rand.nextInt(possibleDisprovals.size()));
		}
		return null;
	}
	
	private Color convertColor(String strColor) {					//method that converts string to java color
		Color color;
		try {
			Field field = Class.forName("java.awt.Color").getField(strColor.trim());		
			color = (Color)field.get(null);
		}
		catch(Exception e) {
			color = null;
		}
		return color;
	}

	public ArrayList<Card> getPlayerHand(){									//getters for testing
		return cardHand;
	}
	
	public void hasGuessed(boolean b) {
		accusationMade = b;
	}
	
	public String getPlayerName() {
		return playerName;
	}

	public int getRow() {
		return row;
	}

	public int getColumn() {
		return column;
	}

	public Color getColor() {
		return color;
	}
	
	public String getPlayerType() {
		return playerType;
	}
	
	public void setRow(int r) {													//setters for testing
		this.row = r;
		rowPixel = r * DIAMETER_SIZE;
	}
	
	public void setCol(int c) {
		this.column = c;
		colPixel = c * DIAMETER_SIZE;
	}
	
	public boolean hasMoved() {
		return hasMoved;
	}
	
	public void setMoved(boolean b) {
		hasMoved = b;
	}
	
	public ArrayList<Card> getSeenCards(){
		return seenCards;
	}
}

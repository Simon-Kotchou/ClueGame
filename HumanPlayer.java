package clueGame;

import java.util.ArrayList;

public class HumanPlayer extends Player{

	public HumanPlayer() {
		super();
	}
	
	public HumanPlayer(String name, int row, int col, String color) {			//constructor that calls super() for parent class constructor
		super(name, "human", row, col, color);
	}

	public void setHand(ArrayList<Card> cardsInHand) {				//used for testing
		this.cardHand = cardsInHand;
	}
	
}

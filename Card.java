package clueGame;

public class Card {
	private String cardName;									//private instance variables for card data
	private CardType cardType;
	
	public Card(String cN, CardType cT) {						//parameterized constructor
		this.cardName = cN;
		this.cardType = cT;
	}
	
	@Override
	public boolean equals(Object aObj) {					//equals method that tests cardName and cardType for equality for this and taken in obj
		if(aObj == this) {
			return true;
		}
		if(!(aObj instanceof Card)) {
			return false;
		}
		
		Card c = (Card)aObj;
		
		return this.cardName.equals(c.cardName) && this.cardType.equals(c.cardType);
	}
	
	public CardType getCardType() {							//getters for testing
		return cardType;
	}
	
	public String getCardName() {
		return cardName;
	}
}

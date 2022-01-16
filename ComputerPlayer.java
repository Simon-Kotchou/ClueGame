package clueGame;

import java.util.Set;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class ComputerPlayer extends Player{
	private BoardCell roomLastVisited;
	
	public ComputerPlayer() {
		super();
	}

	public ComputerPlayer(String name, int row, int col, String color) {			//constructor that calls super() for parent constructor
		super(name,"computer", row, col, color);
	}

	public BoardCell pickLocation(Set<BoardCell> targetList) {					//choosing a location from  a list of targets
		BoardCell targetCell = null;
		boolean tempBool = true;
		for(Iterator<BoardCell> itr = targetList.iterator(); itr.hasNext(); ) {		//iterates through set of targets
			BoardCell testCell = itr.next();
			if(testCell.isDoorway() && testCell != roomLastVisited) {			//if the next target is a doorway and is not already visited will choose
				targetCell = testCell;
				tempBool = false;
			}
		}
		if(tempBool) {												//or selse will randomly choose from the targetted list
			Random rand = new Random();
			int randIndex = rand.nextInt(targetList.size());
			int i = 0;
			for(Iterator<BoardCell> itr = targetList.iterator(); i <= randIndex; i++) {
				BoardCell testCell = itr.next();
				if(i == randIndex) {
					targetCell = testCell;
				}
			}
		}
		if(Board.getInstance().getCellAt(this.getRow(), this.getColumn()).isDoorway()) {				//then sets the room last visited to current pos if in doorway
			roomLastVisited = Board.getInstance().getCellAt(this.row, this.column);
		}
		return targetCell;
	}
	
	public Solution createSuggestion() {																					//method to create a suggestion
		String theRoom = Board.getInstance().getLegend().get(Board.getInstance().getCellAt(row, column).getInitial());
		String theWeapon;
		String thePerson;
		Random rand = new Random();
		ArrayList<Card> unseenPeople = new ArrayList<Card>();															//arraylists for unseen categories of cards
		ArrayList<Card> unseenWeapons = new ArrayList<Card>();
		
		for(int i = 0; i < Board.getInstance().getMainDeck().size(); i++) {
			if(!cardHand.contains(Board.getInstance().getMainDeck().get(i)) && !seenCards.contains(Board.getInstance().getMainDeck().get(i))) {			
				if(Board.getInstance().getMainWeapons().contains(Board.getInstance().getMainDeck().get(i))) {
					unseenWeapons.add(Board.getInstance().getMainDeck().get(i));
				}
				else if(Board.getInstance().getMainPeople().contains(Board.getInstance().getMainDeck().get(i))) {
					unseenPeople.add(Board.getInstance().getMainDeck().get(i));
				}
			}
		}
		theWeapon = unseenWeapons.get(rand.nextInt(unseenWeapons.size())).getCardName();
		thePerson = unseenPeople.get(rand.nextInt(unseenPeople.size())).getCardName();
		
		return new Solution(thePerson,theRoom,theWeapon);											//returns solutoon
	}
	
	public void setLastVisitedRoom(BoardCell room) {
		this.roomLastVisited = room;
	}
	
	public void addCards(ArrayList<Card> CardsToSeen) {
		this.seenCards.addAll(CardsToSeen);
	}
	
	public void setHand(ArrayList<Card> cardsInHand) {
		this.cardHand = cardsInHand;
	}
}

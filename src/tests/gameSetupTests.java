package tests;

import static org.junit.Assert.*;

import java.awt.Color;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;

public class gameSetupTests {

	private static Board board;								//private static variables to be set up in beforeClass method
	private static Card testPersonCard;
	private static Card testWeaponCard;
	private static Card testRoomCard;
	
	@BeforeClass
	public static void setUp() {
		
		board = Board.getInstance();
		
		board.setConfigFiles("GameBoard.csv", "ClueRooms.txt");						//sets config files
		testPersonCard = new Card("Colonel Mustard", CardType.PERSON);				//creates new cards for testing 
		testWeaponCard = new Card("Axe", CardType.WEAPON);
		testRoomCard = new Card("Billiard room", CardType.ROOM);
			
		board.initialize();									//initializes board variable
	}
	
	@Test
	public void loadPeopleFileTest() {														//this test is to test loading of players from config file
		assertEquals(board.getPlayers().get(0).getPlayerName(), "HumanPlayer");
		assertEquals(board.getPlayers().get(0).getColor(), Color.GREEN);
		assertEquals(board.getPlayers().get(0).getPlayerType(), "human");								//first tests the first player in arraylist to check if all data properly read from file
		assertEquals(board.getPlayers().get(0).getRow(), 0);
		assertEquals(board.getPlayers().get(0).getColumn(), 6);
		
		assertEquals(board.getPlayers().get(1).getPlayerName(), "Computer1");						//next the second player from file
		assertEquals(board.getPlayers().get(1).getColor(), Color.RED);
		assertEquals(board.getPlayers().get(1).getPlayerType(), "computer");
		assertEquals(board.getPlayers().get(1).getRow(), 21);
		assertEquals(board.getPlayers().get(1).getColumn(), 6);
		
		assertEquals(board.getPlayers().get(3).getPlayerName(), "Computer3");						//tests fourth player from file
		assertEquals(board.getPlayers().get(3).getColor(), Color.MAGENTA);
		assertEquals(board.getPlayers().get(3).getPlayerType(), "computer");
		assertEquals(board.getPlayers().get(3).getRow(), 5);
		assertEquals(board.getPlayers().get(3).getColumn(), 15);
		
		assertEquals(board.getPlayers().get(5).getPlayerName(), "Computer5");					//tests last player from file
		assertEquals(board.getPlayers().get(5).getColor(), Color.BLACK);
		assertEquals(board.getPlayers().get(5).getPlayerType(), "computer");
		assertEquals(board.getPlayers().get(5).getRow(), 13);
		assertEquals(board.getPlayers().get(5).getColumn(), 0);
	}
	
	@Test														//method to test of the deck is properly loaded
	public void loadDeckTest() {
		int numPeople = 0;
		int numWeapons = 0;	
		int numRooms = 0;
		
		assertEquals(board.getDeck().size(), board.getPeople().size() + board.getRooms().size() + board.getWeapons().size());			//tests of the deck is correct size, by checking if it is the same size as all of the different card decks
		
		for(int i = 0; i < board.getDeck().size(); i++) {				//loop that calculates the number of cards of each type in the deck
			switch(board.getDeck().get(i).getCardType()) {
			case PERSON:
				numPeople++;
				break;
			case WEAPON:
				numWeapons++;
				break;
			case ROOM:
				numRooms++;
				break;
			}
		}
		
		assertEquals(numPeople, board.getPeople().size());						//tests to see if calculated values above are equal to the size of each type of card's arraylist
		assertEquals(numWeapons, board.getWeapons().size());
		assertEquals(numRooms, board.getRooms().size());
		
		assertTrue(board.getMainDeck().contains(testPersonCard));					//tests if the deck contains an identical obj to that of the test variables created in beforeClass method
		assertTrue(board.getMainDeck().contains(testRoomCard));
		assertTrue(board.getMainDeck().contains(testWeaponCard));
	}

	@Test
	public void testDealingCards() {								//method to test of the dealing of the cards was carried out correctly
		int testDeckSize = 0;
		for(Player p : board.getPlayers()) {						//calculates how many cards are in all players hands
			testDeckSize += p.getPlayerHand().size();
		}
		assertEquals(testDeckSize, board.getDeck().size());			//checks if calculated value above is equal to the size of the deck
		
		for(int i = 0; i < board.getPlayers().size(); i++) {				//loop that tests if every player has roughly the same amount of cards, with a threshold of 2 card difference
			if(!(i+1 >= board.getPlayers().size())) {
				assertTrue((board.getPlayers().get(i).getPlayerHand().size() - board.getPlayers().get(i+1).getPlayerHand().size()) < 2 && 
						(board.getPlayers().get(i).getPlayerHand().size() - board.getPlayers().get(i+1).getPlayerHand().size()) > -2);
			}
		}
		
		for(Player p : board.getPlayers()) {						//for every player
			for(Card c : p.getPlayerHand()) {							//for every card from each player
				for(Player p2 : board.getPlayers()) {					//if every other player's hand does not contain that card, then test passes
					if(!(p.equals(p2))){
						assertFalse(p2.getPlayerHand().contains(c));
					}
				}
			}
		}
	}
}

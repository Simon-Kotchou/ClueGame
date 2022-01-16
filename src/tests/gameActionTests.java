package tests;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;
import clueGame.Card;
import clueGame.CardType;
import clueGame.ComputerPlayer;
import clueGame.HumanPlayer;
import clueGame.Player;
import clueGame.Solution;

public class gameActionTests {

	private static Board board;								//private static variables to be set up in beforeClass method
	
	@BeforeClass
	public static void setUp() {
		
		board = Board.getInstance();
		
		board.setConfigFiles("GameBoard.csv", "ClueRooms.txt");						//sets config files
			
		board.initialize();									//initializes board variable
	}
	
	@Test
	public void computerNoRoomTargetTest() {					//test to test random selection if no rooms in list
		ComputerPlayer player = new ComputerPlayer();
		board.calcTargets(8, 14, 2);									//point with no rooms in 2 squares
		boolean location_6_14 = false;
		boolean location_10_14 = false;
		boolean location_9_15 = false;
		boolean location_7_15 = false;								//all possible squares to hit
		boolean location_8_16 = false;
		
		for(int i = 0; i < 100; i++) {
			BoardCell targetLoc = player.pickLocation(board.getTargets());			//uses pickLocation to test if each target is hit 
			if(targetLoc == board.getCellAt(6, 14)) {
				location_6_14 = true;
			}
			else if(targetLoc == board.getCellAt(10, 14)) {
				location_10_14 = true;
			}
			else if(targetLoc == board.getCellAt(9, 15)) {
				location_9_15 = true;
			}
			else if(targetLoc == board.getCellAt(7, 15)) {				//if hit turn corresponding location to true
				location_7_15 = true;
			}
			else if(targetLoc == board.getCellAt(8, 16)) {
				location_8_16 = true;
			}
			else {
				fail("Computer Location Selection Error");
			}
		}
		
		assertTrue(location_6_14);										//check if all targets hit
		assertTrue(location_10_14);
		assertTrue(location_9_15);
		assertTrue(location_7_15);
		assertTrue(location_8_16);
	}
	
	@Test
	public void computerRoomTargetSelectTest() {					//if no room in list not just visited then must select the room
		ComputerPlayer player = new ComputerPlayer();
		board.calcTargets(6, 7, 3);										//square within 3 blocks from room not visited
		
		for(int i = 0; i < 20; i++) {
			BoardCell targetLoc = player.pickLocation(board.getTargets());				//checks if everytime it goes to room
		
			assertEquals(targetLoc, board.getCellAt(6, 5));
		}
	}
	
	@Test
	public void computerRoomVistedTargetSelectTest() {					//if room already visited in vacinity will randomly choose including the room
		ComputerPlayer player = new ComputerPlayer();
		board.calcTargets(6, 7, 2);										//chooses square that is close to door already visited
		player.setLastVisitedRoom(board.getCellAt(6, 5));
		
		boolean location_5_8 = false;
		boolean location_4_7 = false;
		boolean location_5_6 = false;				//all possible squares including door
		boolean location_7_6 = false;
		boolean location_8_7 = false;
		boolean location_6_5 = false;
		
		for(int i = 0; i < 100; i++) {
			BoardCell targetLoc = player.pickLocation(board.getTargets());		//picks a target many times 
			if(targetLoc == board.getCellAt(5, 8)) {
				location_5_8 = true;
			}
			else if(targetLoc == board.getCellAt(4, 7)) {
				location_4_7 = true;
			}
			else if(targetLoc == board.getCellAt(5, 6)) {
				location_5_6 = true;
			}
			else if(targetLoc == board.getCellAt(7, 6)) {
				location_7_6 = true;
			}
			else if(targetLoc == board.getCellAt(8, 7)) {
				location_8_7 = true;
			}
			else if(targetLoc == board.getCellAt(6, 5)) {
				location_6_5 = true;
			}
			else {
				fail("Computer Location Selection Error");
			}
		}
		
		assertTrue(location_5_8);								//checks all targets were hit at least once including the visited room
		assertTrue(location_4_7);
		assertTrue(location_5_6);
		assertTrue(location_7_6);
		assertTrue(location_8_7);
		assertTrue(location_6_5);
		
	}
	
	@Test
	public void computerAccusationTest() {														//testing if correct accusation will return a correct result
		board.setSolution("Colonel Mustard", "Billiard room", "Axe");
		
		assertTrue(board.checkAccusation(new Solution("Colonel Mustard", "Billiard room", "Axe")));			//checks correct
		assertFalse(board.checkAccusation(new Solution("Miss Scarlet", "Billiard room", "Axe")));				//checks false person
		assertFalse(board.checkAccusation(new Solution("Colonel Mustard", "Den", "Axe")));							//checks false room
		assertFalse(board.checkAccusation(new Solution("Colonel Mustard", "Billiard room", "Machete")));			//checks false weapon
	}
	
	@Test 
	public void suggestionRoomMatch() {										//testing if suggestion within a specific room will always suggest that current room
		ComputerPlayer player = new ComputerPlayer();
		player.setRow(6);
		player.setCol(5);												//setting to be in kitchen 
		
		assertEquals(player.createSuggestion().room, "Kitchen");									//asserting that the room suggestion is the same as current 
		
		player.setRow(20);
		player.setCol(3);											//setting to be in Billiard room
		
		assertEquals(player.createSuggestion().room, "Billiard room");							//testing again
	}
	
	@Test
	public void suggestionOneNotSeen() {									//testing a suggestion when only one card in both weapons and people are not seen
		ComputerPlayer player = new ComputerPlayer();
		ArrayList<Card> seenCard = new ArrayList();							//arraylist holding the cards to put in the players hand and seen cards list
		ArrayList<Card> handCard = new ArrayList();
		player.setRow(6);
		player.setCol(5);
		
		seenCard.add(new Card("Axe", CardType.WEAPON));
		seenCard.add(new Card("Machete", CardType.WEAPON));					//putting every weapon in but the shotgun
		seenCard.add(new Card("Revolver", CardType.WEAPON));
		handCard.add(new Card("Mallet", CardType.WEAPON));
		seenCard.add(new Card("Crowbar", CardType.WEAPON));
		seenCard.add(new Card("Colonel Mustard", CardType.PERSON));				//putting every person in but mrs white
		seenCard.add(new Card("Miss Scarlet", CardType.PERSON));
		handCard.add(new Card("Professor Plum", CardType.PERSON));
		seenCard.add(new Card("Mrs. Peacock", CardType.PERSON));
		handCard.add(new Card("Mr. Green", CardType.PERSON));
		
		player.addCards(seenCard);												//adding cards to players hands and seen lists
		player.setHand(handCard);
		
		assertEquals(player.createSuggestion().weapon, "Shotgun");				//asserts that shotgun and mrs white are the only suggested gcards
		assertEquals(player.createSuggestion().person, "Mrs. White");
	}
	
	@Test
	public void suggestionMultipleUnseen() {						//testing if there are multiple unseen cards that it will suggest a random one of those cards 
		ComputerPlayer player = new ComputerPlayer();
		ArrayList<Card> seenCard = new ArrayList();
		ArrayList<Card> handCard = new ArrayList();
		player.setRow(6);
		player.setCol(5);
		
		seenCard.add(new Card("Axe", CardType.WEAPON));						//putting every weapon but crowbar and shotgun in list
		seenCard.add(new Card("Machete", CardType.WEAPON));
		seenCard.add(new Card("Revolver", CardType.WEAPON));
		handCard.add(new Card("Mallet", CardType.WEAPON));
		seenCard.add(new Card("Colonel Mustard", CardType.PERSON));				//every person but mr green and mrs white in list
		seenCard.add(new Card("Miss Scarlet", CardType.PERSON));
		handCard.add(new Card("Professor Plum", CardType.PERSON));
		seenCard.add(new Card("Mrs. Peacock", CardType.PERSON));
		
		player.addCards(seenCard);
		player.setHand(handCard);
		
		boolean MrGreen = false;
		boolean MrsWhite = false;									//booleans to test if theyare chosen at least once
		boolean Crowbar = false;
		boolean Shotgun = false;
		
		for(int i = 0; i < 100; i++) {
			if(player.createSuggestion().weapon.equals( "Shotgun")) {				//testing if the suggestion equals either the options
				Shotgun = true;
			}
			else if(player.createSuggestion().weapon.equals("Crowbar")) {
				Crowbar = true;
			}
			if(player.createSuggestion().person.equals( "Mr. Green")) {
				MrGreen = true;
			}
			else if(player.createSuggestion().person.equals( "Mrs. White")) {
				MrsWhite = true;
			}
		}
		assertTrue(MrGreen);													//testing they were all chosen at least once confirming random choosing
		assertTrue(MrsWhite);
		assertTrue(Crowbar);
		assertTrue(Shotgun);
	}

	
	@Test
	public void disproveOneMatch() {							//if the player matched only one card will test if that card is returned
		ComputerPlayer player = new ComputerPlayer();
		ArrayList<Card> handCard = new ArrayList();
		
		handCard.add(new Card("Mallet", CardType.WEAPON));					//putting 3 different cards into players hand
		handCard.add(new Card("Colonel Mustard", CardType.PERSON));
		handCard.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard);
		
		assertEquals(player.disproveSuggestion(new Solution("Colonel Mustard", "Billiard room", "Axe")), new Card("Colonel Mustard", CardType.PERSON));		//tests if it returns the once card that is matching
	}
	
	@Test													//tests functionality when a player has multiple matches
	public void disproveMultipleMatch() {
		ComputerPlayer player = new ComputerPlayer();		
		ArrayList<Card> handCard = new ArrayList();
		
		handCard.add(new Card("Axe", CardType.WEAPON));							//adding cards into players deck
		handCard.add(new Card("Colonel Mustard", CardType.PERSON));
		handCard.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard);
		
		boolean Axe = false;												//testing randomness with booleans 
		boolean Mustard = false;
		
		for(int i = 0; i < 50; i++) {
			if(player.disproveSuggestion(new Solution("Colonel Mustard", "Billiard room", "Axe")).equals(new Card("Colonel Mustard", CardType.PERSON))) {		//tests that each will be chosen at least one time
				Mustard = true;
			}
			else if(player.disproveSuggestion(new Solution("Colonel Mustard", "Billiard room", "Axe")).equals(new Card("Axe", CardType.WEAPON))) {
				Axe = true;
			}
		}
		
		assertTrue(Axe);
		assertTrue(Mustard);
	}
	
	@Test											//test case where the player has no matches and is expecting null output
	public void disproveNoMatch() {
		ComputerPlayer player = new ComputerPlayer();
		ArrayList<Card> handCard = new ArrayList();
		
		handCard.add(new Card("Axe", CardType.WEAPON));									//adding cards to players deckk
		handCard.add(new Card("Colonel Mustard", CardType.PERSON));
		handCard.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard);
		
		assertEquals(player.disproveSuggestion(new Solution("Mrs. Scarlet", "Billiard room", "Mallet")), null);			//testing a solution with no matches to players deck will result in null
	}
	
	@Test													//how the board will handle a suggestion with no disprovals
	public void handleSuggestionNoDisprovals() {
		
		HumanPlayer player = new HumanPlayer();
		ComputerPlayer player1 = new ComputerPlayer();
		ComputerPlayer player2 = new ComputerPlayer();					//creating a couple new player objects
		
		ArrayList<Player> playersList = new ArrayList<Player>();				//adding them to arraylist
		ArrayList<Card> handCard1 = new ArrayList();
		ArrayList<Card> handCard2 = new ArrayList();						//creating arraylists for each of their hands
		ArrayList<Card> handCard3 = new ArrayList();
		playersList.add(player);
		playersList.add(player1);
		playersList.add(player2);
		
		handCard1.add(new Card("Axe", CardType.WEAPON));
		handCard1.add(new Card("Colonel Mustard", CardType.PERSON));						//creating different hands for each of the players
		handCard1.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard1);
		
		handCard2.add(new Card("Mallet", CardType.WEAPON));
		handCard2.add(new Card("Mr. Green", CardType.PERSON));
		handCard2.add(new Card("Billiard room", CardType.ROOM));
		
		player1.setHand(handCard2);
		
		handCard3.add(new Card("Shotgun", CardType.WEAPON));
		handCard3.add(new Card("Mrs. Scarlet", CardType.PERSON));
		handCard3.add(new Card("Kitchen", CardType.ROOM));
		
		player2.setHand(handCard3);
		
		board.getPlayers().clear();												//clearing the list of players already in the board class
		
		board.setPlayers(playersList);											//setting the players list to the 3 players here to be used in method
		
		assertEquals(board.handleSuggestion(new Solution("Mrs. White", "Study", "Revolver"), player1), null);		//testing with a solution that none of the players have cards for
	}
	
	@Test
	public void accusingPlayerNull() {								//testing when only accusing player has disprovals
		
		HumanPlayer player = new HumanPlayer();
		ComputerPlayer player1 = new ComputerPlayer();
		ComputerPlayer player2 = new ComputerPlayer();
		
		ArrayList<Player> playersList = new ArrayList<Player>();				//same functionality as above
		ArrayList<Card> handCard1 = new ArrayList();
		ArrayList<Card> handCard2 = new ArrayList();
		ArrayList<Card> handCard3 = new ArrayList();
		playersList.add(player);
		playersList.add(player1);
		playersList.add(player2);
		
		handCard1.add(new Card("Axe", CardType.WEAPON));
		handCard1.add(new Card("Colonel Mustard", CardType.PERSON));
		handCard1.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard1);
		
		handCard2.add(new Card("Mallet", CardType.WEAPON));
		handCard2.add(new Card("Mr. Green", CardType.PERSON));			//now solution and accuser have common room
		handCard2.add(new Card("Study", CardType.ROOM));
		
		player1.setHand(handCard2);
		
		handCard3.add(new Card("Shotgun", CardType.WEAPON));
		handCard3.add(new Card("Mrs. Scarlet", CardType.PERSON));
		handCard3.add(new Card("Kitchen", CardType.ROOM));
		
		player2.setHand(handCard3);
		
		board.getPlayers().clear();
		
		board.setPlayers(playersList);
		
		board.setPlayers(playersList);
		
		assertEquals(board.handleSuggestion(new Solution("Mrs. White", "Study", "Revolver"), player1), null);			//testing will return null even though accuser has disprovals
	}
	
	@Test
	public void humanDisproveAnswer() {								//now testing that the human can disprove the suggestion
		
		HumanPlayer player = new HumanPlayer();
		ComputerPlayer player1 = new ComputerPlayer();
		ComputerPlayer player2 = new ComputerPlayer();									//same functionality as above
		
		ArrayList<Player> playersList = new ArrayList<Player>();
		ArrayList<Card> handCard1 = new ArrayList();
		ArrayList<Card> handCard2 = new ArrayList();
		ArrayList<Card> handCard3 = new ArrayList();
		playersList.add(player);
		playersList.add(player1);
		playersList.add(player2);
		
		handCard1.add(new Card("Revolver", CardType.WEAPON));				//giving human and solution a common weapon
		handCard1.add(new Card("Colonel Mustard", CardType.PERSON));
		handCard1.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard1);
		
		handCard2.add(new Card("Mallet", CardType.WEAPON));
		handCard2.add(new Card("Mr. Green", CardType.PERSON));
		handCard2.add(new Card("Billiard room", CardType.ROOM));
		
		player1.setHand(handCard2);
		
		handCard3.add(new Card("Shotgun", CardType.WEAPON));
		handCard3.add(new Card("Mrs. Scarlet", CardType.PERSON));
		handCard3.add(new Card("Kitchen", CardType.ROOM));
		
		player2.setHand(handCard3);
		
		board.getPlayers().clear();
		
		board.setPlayers(playersList);
		
		assertEquals(board.handleSuggestion(new Solution("Mrs. White", "Study", "Revolver"), player1), new Card("Revolver",CardType.WEAPON));			//testing that the common weapon card will be returned
	}
	
	@Test
	public void humanAccuserSuggestion() {							//testing that the human will not return anything when the accuser with disprovals
		
		HumanPlayer player = new HumanPlayer();
		ComputerPlayer player1 = new ComputerPlayer();
		ComputerPlayer player2 = new ComputerPlayer();
		
		ArrayList<Player> playersList = new ArrayList<Player>();
		ArrayList<Card> handCard1 = new ArrayList();
		ArrayList<Card> handCard2 = new ArrayList();
		ArrayList<Card> handCard3 = new ArrayList();
		playersList.add(player);
		playersList.add(player1);
		playersList.add(player2);
		
		handCard1.add(new Card("Revolver", CardType.WEAPON));					//common weapon
		handCard1.add(new Card("Colonel Mustard", CardType.PERSON));
		handCard1.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard1);
		
		handCard2.add(new Card("Mallet", CardType.WEAPON));
		handCard2.add(new Card("Mr. Green", CardType.PERSON));
		handCard2.add(new Card("Billiard room", CardType.ROOM));
		
		player1.setHand(handCard2);
		
		handCard3.add(new Card("Shotgun", CardType.WEAPON));
		handCard3.add(new Card("Mrs. Scarlet", CardType.PERSON));
		handCard3.add(new Card("Kitchen", CardType.ROOM));
		
		player2.setHand(handCard3);
		
		board.getPlayers().clear();
		
		board.setPlayers(playersList);
		
		assertEquals(board.handleSuggestion(new Solution("Mrs. White", "Study", "Revolver"), player), null);			//testing null output from human accuser with disrpovals	
	}
	
	@Test
	public void multipleCanDisprove() {									//testing that when multiple people can disprove, only the closest in the list will return output
		
		HumanPlayer player = new HumanPlayer();
		ComputerPlayer player1 = new ComputerPlayer();
		ComputerPlayer player2 = new ComputerPlayer();
		
		ArrayList<Player> playersList = new ArrayList<Player>();
		ArrayList<Card> handCard1 = new ArrayList();
		ArrayList<Card> handCard2 = new ArrayList();
		ArrayList<Card> handCard3 = new ArrayList();
		playersList.add(player);
		playersList.add(player1);
		playersList.add(player2);
		
		handCard1.add(new Card("Revolver", CardType.WEAPON));
		handCard1.add(new Card("Colonel Mustard", CardType.PERSON));
		handCard1.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard1);
		
		handCard2.add(new Card("Mallet", CardType.WEAPON));
		handCard2.add(new Card("Mrs. White", CardType.PERSON));				//common person
		handCard2.add(new Card("Billiard room", CardType.ROOM));
		
		player1.setHand(handCard2);
		
		handCard3.add(new Card("Shotgun", CardType.WEAPON));
		handCard3.add(new Card("Mrs. Scarlet", CardType.PERSON));
		handCard3.add(new Card("Study", CardType.ROOM));					//common room
		
		player2.setHand(handCard3);
		
		board.getPlayers().clear();
		
		board.setPlayers(playersList);
		
		assertEquals(board.handleSuggestion(new Solution("Mrs. White", "Study", "Revolver"), player), new Card("Mrs. White", CardType.PERSON));	 //since player is accuser, player1's disproval card will be outputted and not player2's
	}
	
	@Test
	public void playerAndHumanCanDisproveTest() {							//testing the same functionality with a human player
		
		HumanPlayer player = new HumanPlayer();
		ComputerPlayer player1 = new ComputerPlayer();
		ComputerPlayer player2 = new ComputerPlayer();
		
		ArrayList<Player> playersList = new ArrayList<Player>();
		ArrayList<Card> handCard1 = new ArrayList();
		ArrayList<Card> handCard2 = new ArrayList();
		ArrayList<Card> handCard3 = new ArrayList();
		playersList.add(player);
		playersList.add(player1);
		playersList.add(player2);
		
		handCard1.add(new Card("Revolver", CardType.WEAPON));					//common weapon
		handCard1.add(new Card("Colonel Mustard", CardType.PERSON));
		handCard1.add(new Card("Den", CardType.ROOM));
		
		player.setHand(handCard1);
		
		handCard2.add(new Card("Mallet", CardType.WEAPON));
		handCard2.add(new Card("Mrs. White", CardType.PERSON));
		handCard2.add(new Card("Billiard room", CardType.ROOM));
		
		player1.setHand(handCard2);
		
		handCard3.add(new Card("Shotgun", CardType.WEAPON));
		handCard3.add(new Card("Mrs. Scarlet", CardType.PERSON));
		handCard3.add(new Card("Study", CardType.ROOM));					//common room
		
		player2.setHand(handCard3);
		
		board.getPlayers().clear();
		
		board.setPlayers(playersList);
		
		assertEquals(board.handleSuggestion(new Solution("Mrs. White", "Study", "Revolver"), player1), new Card("Study", CardType.ROOM));			//testing even though both have disprovals, the next in line will be the only card returned 
	}
}

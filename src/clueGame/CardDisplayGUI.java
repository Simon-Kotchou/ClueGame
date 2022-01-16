package clueGame;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class CardDisplayGUI extends JPanel{						//class extends JPanel
	private ArrayList<Card> cardList;							//instance variable to hold players hand		
	
	public CardDisplayGUI(Board board) {
		JPanel mainCardPanel = new JPanel();											//creating main panel
		mainCardPanel.setLayout(new GridLayout(3,1));										//setting layout to fit need of gui
		mainCardPanel.setBorder(new TitledBorder(new EtchedBorder(), "My Cards"));			//setting border to show title 
		mainCardPanel.setPreferredSize(new Dimension(200,870));								//setting size of main panel
		
		JPanel peoplePanel = createPeoplePanel();								//calling methods below to create each panel for gui
		JPanel roomPanel = createRoomPanel();
		JPanel weaponPanel = createWeaponPanel();
		
		cardList = board.getPlayers().get(0).getPlayerHand();					//initializing the players hand
	
		for(Card c : cardList) {										//for every card in the hand
			switch(c.getCardType()) {
			case PERSON:												//if person type will add a new textfield to person panel
				peoplePanel.add(createNewTextField(c.getCardName()));
				break;
			case ROOM:														//if room type will add textfield to room panel
				roomPanel.add(createNewTextField(c.getCardName()));
				break;
			case WEAPON:													//if weapon type will add textfield to weapon panel
				weaponPanel.add(createNewTextField(c.getCardName()));
				break;
			default:
				break;
			}
		}
		
		mainCardPanel.add(peoplePanel);									//adds all 3 panels to main panel
		mainCardPanel.add(roomPanel);
		mainCardPanel.add(weaponPanel);
		
		add(mainCardPanel);											//adds that panel to the main gui
		
	}
	
	private JPanel createPeoplePanel() {												//private method that creates and returns person panel
		JPanel peoplePanel = new JPanel();
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		peoplePanel.setLayout(new GridLayout(3,1));
		
		return peoplePanel;
		
		
	}
	
	private JPanel createRoomPanel() {											//private method that creates and returns room panel
		JPanel roomPanel = new JPanel();
		roomPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));
		roomPanel.setLayout(new GridLayout(3,1));
		
		return roomPanel;
	}
	
	private JPanel createWeaponPanel() {											//private method that creates and returns weapon panel
		JPanel weaponPanel = new JPanel();
		weaponPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));
		weaponPanel.setLayout(new GridLayout(3,1));
		
		return weaponPanel;
	}
	
	private JTextField createNewTextField(String text) {					//private method that creates and returns a JTextField representing a card in players hand
		JTextField displayField = new JTextField(20);
		displayField.setEditable(false);
		displayField.setText(text);
		displayField.setBackground(Color.WHITE);
		
		return displayField;
	}
	
	
}

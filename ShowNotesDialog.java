package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionListener;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class ShowNotesDialog extends JDialog{			//extends JDialog
	
	public ShowNotesDialog() {						//default constructor to create custom dialog 
		setTitle("Detective Notes");				//sets title
		setSize(700,700);							//sets size
		setLayout(new GridLayout(3,2));				//sets layout
		
		JComboBox peopleComboBox = new JComboBox();		//initializes three combo boxes to be added onto panels and then onto the custom dialog
		JComboBox roomsComboBox = new JComboBox();
		JComboBox weaponsComboBox = new JComboBox();
		
		JPanel peoplePanel = new JPanel();											//panel for people check boxes
		peoplePanel.setBorder(new TitledBorder(new EtchedBorder(), "People"));
		peoplePanel.setLayout(new GridLayout(3,2));
		for(Card c : Board.getInstance().getMainPeople()) {							//for every person in game will create a checkbox
			JCheckBox aCheckBox = new JCheckBox();
			aCheckBox.setText(c.getCardName());
			peoplePanel.add(aCheckBox);										//adds checkbox to panel
		}
		
		JPanel roomsPanel = new JPanel();
		roomsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Rooms"));			//does same as above for every room in game
		roomsPanel.setLayout(new GridLayout(5,2));
		for(Card c : Board.getInstance().getMainRooms()) {
			JCheckBox aCheckBox = new JCheckBox();
			aCheckBox.setText(c.getCardName());
			roomsPanel.add(aCheckBox);
		}
		
		JPanel weaponsPanel = new JPanel();
		weaponsPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapons"));			//does same as above for every weapon in game
		weaponsPanel.setLayout(new GridLayout(3,2));
		for(Card c : Board.getInstance().getMainWeapons()) {
			JCheckBox aCheckBox = new JCheckBox();
			aCheckBox.setText(c.getCardName());
			weaponsPanel.add(aCheckBox);
		}
		
		JPanel personGuessPanel = new JPanel();												//creates a new panel to house the guess of user
		personGuessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Person Guess"));
		for(Card c : Board.getInstance().getMainPeople()) {
			peopleComboBox.addItem(c.getCardName());											//for every person in game will add to a combobox initialized ablove
		}
		personGuessPanel.add(peopleComboBox);												//will add that combo box to the panel
		
		JPanel roomGuessPanel = new JPanel();
		roomGuessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Room Guess"));				//same as above for every room in game
		for(Card c : Board.getInstance().getMainRooms()) {
			roomsComboBox.addItem(c.getCardName());
		}
		roomGuessPanel.add(roomsComboBox);
		
		JPanel weaponGuessPanel = new JPanel();
		weaponGuessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Weapon Guess"));			//same as above for every weapon in game
		for(Card c : Board.getInstance().getMainWeapons()) {
			weaponsComboBox.addItem(c.getCardName());
		}
		weaponGuessPanel.add(weaponsComboBox);
		
		add(peoplePanel);														//adds all panels created above to the custom dialog
		add(personGuessPanel);
		add(roomsPanel);
		add(roomGuessPanel);
		add(weaponsPanel);
		add(weaponGuessPanel);
	}
}

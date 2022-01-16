package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class MakeAccusationDialog extends JDialog{
	private boolean submitted; 
	public JPanel mainRightPanel;									//this class is almost a mirror of guessdialog but with different button implementations
	public JComboBox peopleComboBox;
	public JComboBox weaponsComboBox;
	public JComboBox roomsComboBox;
	
	
	public MakeAccusationDialog() {						//default constructor to create custom dialog 
		setTitle("Make an Accusation");				//sets title
		setSize(500,500);							//sets size
		setLayout(new GridLayout(1,2));				//sets layout
		setModal(true);                             //makes dialog modal
		
		peopleComboBox = new JComboBox();		//initializes three combo boxes to be added onto panels and then onto the custom dialog
		for(Card c : Board.getInstance().getMainPeople()) {
			peopleComboBox.addItem(c.getCardName());											//for every person in game will add to a combobox initialized ablove
		}
		
		weaponsComboBox = new JComboBox();
		for(Card c : Board.getInstance().getMainWeapons()) {
			weaponsComboBox.addItem(c.getCardName());											//for every person in game will add to a combobox initialized ablove
		}
		
		roomsComboBox = new JComboBox();
		for(Card c : Board.getInstance().getMainRooms()) {
			roomsComboBox.addItem(c.getCardName());											//for every person in game will add to a combobox initialized ablove
		}
		
		JTextField roomDisplay = new JTextField(20);
		roomDisplay.setText("Your room");
		roomDisplay.setEditable(false);
		
		JTextField personDisplay = new JTextField(20);
		personDisplay.setText("Person");
		personDisplay.setEditable(false);
		
		JTextField weaponDisplay = new JTextField(20);
		weaponDisplay.setText("Weapon");
		weaponDisplay.setEditable(false);
		
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener(){			//adds a action listener to button that will call method in board class when pressed
			public void actionPerformed(ActionEvent e) {
				Board.getInstance().handleHumanAccusation(peopleComboBox.getSelectedItem().toString(),roomsComboBox.getSelectedItem().toString(),weaponsComboBox.getSelectedItem().toString());
				dispose();														//calls to handle accusation in board class
			}
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){			//adds a action listener to button that will call method in board class when pressed
			public void actionPerformed(ActionEvent e) {
				dispose();											//will dispose the dialog if cancelled
			}
		});
		
		
		JPanel mainLeftPanel = new JPanel();											//panel for people check boxes
		mainLeftPanel.setLayout(new GridLayout(4,1));
		
		
		mainRightPanel = new JPanel();
		mainRightPanel.setLayout(new GridLayout(4,1));
		
		mainLeftPanel.add(roomDisplay);
		mainLeftPanel.add(personDisplay);
		mainLeftPanel.add(weaponDisplay);
		mainLeftPanel.add(submit);
		add(mainLeftPanel);
		
		mainRightPanel.add(peopleComboBox);
		mainRightPanel.add(roomsComboBox);
		mainRightPanel.add(weaponsComboBox);
		mainRightPanel.add(cancel);
		add(mainRightPanel);
	}
	
	public boolean getSubmitted() {
		return submitted;
	}
}

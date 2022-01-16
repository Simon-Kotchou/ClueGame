package clueGame;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

public class MakeGuessDialog extends JDialog{
	private boolean submitted; 
	public JPanel mainRightPanel;
	public JComboBox peopleComboBox;
	public JTextField roomPanel;
	public JComboBox weaponsComboBox;
	
	
	public MakeGuessDialog(BoardCell roomCell) {						//default constructor to create custom dialog 
		setTitle("Make a Guess");				//sets title
		setSize(500,500);							//sets size
		setLayout(new GridLayout(1,2));				//sets layout
		setModal(true);                             //makes dialog modal
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		
		peopleComboBox = new JComboBox();		//initializes three combo boxes to be added onto panels and then onto the custom dialog
		for(Card c : Board.getInstance().getMainPeople()) {
			peopleComboBox.addItem(c.getCardName());											//for every person in game will add to a combobox initialized ablove
		}
		
		weaponsComboBox = new JComboBox();
		for(Card c : Board.getInstance().getMainWeapons()) {
			weaponsComboBox.addItem(c.getCardName());											//for every person in game will add to a combobox initialized ablove
		}
		
		JTextField roomDisplay = new JTextField(20);
		roomDisplay.setText("Your room");
		roomDisplay.setEditable(false);
																						//textfields to display markers for user
		JTextField personDisplay = new JTextField(20);
		personDisplay.setText("Person");
		personDisplay.setEditable(false);
		
		JTextField weaponDisplay = new JTextField(20);
		weaponDisplay.setText("Weapon");
		weaponDisplay.setEditable(false);
		
		JButton submit = new JButton("Submit");
		submit.addActionListener(new ActionListener(){			//adds a action listener to button that will call method in board class when pressed
			public void actionPerformed(ActionEvent e) {
				Solution tempGuess = new Solution(peopleComboBox.getSelectedItem().toString(),roomPanel.getText(),weaponsComboBox.getSelectedItem().toString());
				Player tempPlayer = Board.getInstance().getCurrentPlayer();
				setVisible(false);
				
				ClueGame.getCtrlPanel().setGuess(tempGuess);
				Card disprovedCard = Board.getInstance().handleSuggestion(tempGuess, tempPlayer);							//will set guess to gui and handle it in board class
				if(disprovedCard == null) {
					ClueGame.getCtrlPanel().setResult("No new clue");
					boolean tempBool = true;
					Player nextPlayer;
					if(Board.getInstance().getPlayers().indexOf(Board.getInstance().getCurrentPlayer()) == 5) {							//if the current player is end of arraylist will loop back to front
						nextPlayer = Board.getInstance().getPlayers().get(0);
					}
					else {
						nextPlayer = Board.getInstance().getPlayers().get(Board.getInstance().getPlayers().indexOf(Board.getInstance().getCurrentPlayer())+1);					//or will go to next player in list
					}
					for(Card c : nextPlayer.getPlayerHand()) {
						if(c.getCardName().equals(tempGuess.room)) {																			//seeing if the computer should accuse next round
							tempBool = false;
						}
					}
					if(tempBool) {
						Board.getInstance().setAccusationTrue();
						Board.getInstance().setAccusationGuess(tempGuess);
					}
				}
				else {
					ClueGame.getCtrlPanel().setResult(disprovedCard.getCardName());
					tempPlayer.getSeenCards().add(disprovedCard);
				}
				dispose();
			}
		});
		
		JButton cancel = new JButton("Cancel");
		cancel.addActionListener(new ActionListener(){			//adds a action listener to button that will call method in board class when pressed
			public void actionPerformed(ActionEvent e) {
				repaint();
			}
		});
		
		roomPanel = new JTextField(Board.getInstance().getLegend().get(roomCell.getInitial()));
		roomPanel.setEditable(false);
		
		
		JPanel mainLeftPanel = new JPanel();											//panel for people check boxes
		mainLeftPanel.setLayout(new GridLayout(4,1));
		
		
		mainRightPanel = new JPanel();
		mainRightPanel.setLayout(new GridLayout(4,1));									//adds right panel with combo boxes
		
		mainLeftPanel.add(roomDisplay);
		mainLeftPanel.add(personDisplay);
		mainLeftPanel.add(weaponDisplay);										//adds components to panels
		mainLeftPanel.add(submit);
		add(mainLeftPanel);
		
		mainRightPanel.add(roomPanel);
		mainRightPanel.add(peopleComboBox);
		mainRightPanel.add(weaponsComboBox);
		mainRightPanel.add(cancel);											//then adds main panels to the dialog obj
		add(mainRightPanel);
	}
	
	public boolean getSubmitted() {
		return submitted;
	}
}

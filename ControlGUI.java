package clueGame;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;
import java.awt.Dimension;
import java.awt.GridBagLayout;

public class ControlGUI extends JPanel{
	private JTextField guessField;
	private JTextField dieField;
	private JTextField resultField;
	private JTextField turnField;
	private JButton nextPlayer;
	
	public ControlGUI() {
		setLayout(new GridLayout(2,0));				//sets up main panel with 2 rows
		JPanel panel = createTopPanel();		//adds top panel
		add(panel);
		panel = createBottomPanel();		//adds bottom panel
		add(panel);
	}

	private JPanel createTopPanel() {
		JPanel panel = new JPanel(new GridLayout(1,3));				//sets main top panel with 1 row 3 cols
		JPanel turnPanel = new JPanel(new GridLayout(2,0));			//sets leftmost panel
		JLabel turnLabel = new JLabel("Whose turn?");				//label fro leftmost panel
		
	
		nextPlayer = new JButton("Next player");					//creates new button
		nextPlayer.addActionListener(new ActionListener(){			//adds a action listener to button that will call method in board class when pressed
			public void actionPerformed(ActionEvent e) {
				Board.getInstance().handleNextButton();
			}
		});
		
		JButton makeAccusation = new JButton("Make an accusation");							//button to make an accusation, action listener added
		makeAccusation.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(Board.getInstance().getCurrentPlayer() == null) {																									//4 error cases that will throw a joptionpane error if true
					JOptionPane.showMessageDialog(ClueGame.getFrame(), "Error: Must Start Game With Next Player Button", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(!Board.getInstance().getCurrentPlayer().playerType.equals("human")) {
					JOptionPane.showMessageDialog(ClueGame.getFrame(), "Error: Not Your Turn", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(Board.getInstance().getCurrentPlayer().accusationMade == true) {
					JOptionPane.showMessageDialog(ClueGame.getFrame(), "Error: Accusation Already Made This Turn", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else if(Board.getInstance().getCurrentPlayer().hasMoved == true) {
					JOptionPane.showMessageDialog(ClueGame.getFrame(), "Error: Must Accuse Before Moving", "Error", JOptionPane.ERROR_MESSAGE);
				}
				else {
					MakeAccusationDialog dialog = new MakeAccusationDialog();																		//otherwise will make a dialog object and make it visible
					dialog.setVisible(true);
				}
			}
		});
		
		
		turnField = new JTextField(20);							//creates field form leftmost panel
		turnField.setEditable(false);
		
		JPanel panel1 = new JPanel();							//2 subpanels for leftmost panel
		JPanel panel2 = new JPanel();
		panel1.setLayout(new GridBagLayout());					//centering text
		panel1.add(turnLabel);
		panel2.add(turnField);								//adds subpanels to leftmost panel
		turnPanel.add(panel1);
		turnPanel.add(panel2);						
		panel.add(turnPanel);
		panel.add(nextPlayer);						//adds 3 panels to main top panel
		panel.add(makeAccusation);
		return panel;
	}
	
	private JPanel createBottomPanel() {
		JPanel panel = new JPanel();
		JPanel diePanel = new JPanel(new GridLayout(0,2));				//creates main panel and 3 subpanels
		JPanel guessPanel = new JPanel(new GridLayout(2,0));
		JPanel resultPanel = new JPanel(new GridLayout(0,2));
		
		JLabel dieLabel = new JLabel("Roll");						//3 labels for each subpanel
		JLabel guessLabel = new JLabel("Guess");
		JLabel resultLabel = new JLabel("Response");
		
		dieField = new JTextField(20);
		guessField = new JTextField(20);						//3 fields for the 3 subpanels
		resultField = new JTextField(20);
		dieField.setEditable(false);
		
		diePanel.setBorder(new TitledBorder(new EtchedBorder(), "Die"));					//adds labeled borders to subpanels
		guessPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess"));
		resultPanel.setBorder(new TitledBorder(new EtchedBorder(), "Guess Result"));
		diePanel.add(dieLabel);
		guessPanel.add(guessLabel);										//adding fields and labels to subpanels
		resultPanel.add(resultLabel);
		diePanel.add(dieField);
		guessPanel.add(guessField);
		resultPanel.add(resultField);
		
		panel.add(diePanel);						//adds 3 subpanels to main panel
		panel.add(guessPanel);
		panel.add(resultPanel);
		
		return panel;
	}
	
	public void setGuess(Solution guess) {
		guessField.setText(guess.person + ", " + guess.room + ", " + guess.weapon);
	}
	
	public void setResult(String result) {							//setters for textfields
		resultField.setText(result);
	}
	
	public void showTurn(String character, int roll) {
		dieField.setText(Integer.toString(roll));
		turnField.setText(character);
	}
	
}

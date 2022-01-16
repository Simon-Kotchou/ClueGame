package clueGame;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

public class ClueGame extends JFrame {
	
	private static ControlGUI ctrlPanel;					//static variables 
	private static JFrame frame;
	
	private static JMenu createMenuWithItems() {
		JMenu mainMenu = new JMenu("File");					//adds a new menu to the menu bar 				
		
		JMenuItem exitItem = new JMenuItem("Exit");				//creates new menu item to be added to menu
		class MenuExitListener implements ActionListener{		//class that implements action listener to preform an action 
			@Override
			public void actionPerformed(ActionEvent e) {				//will exit the program when clicked
				System.exit(0);
			}
		}
		exitItem.addActionListener(new MenuExitListener());				//adds this actionlistener to the menu item
		
		mainMenu.add(exitItem);										//adds menu item to the menu on the menubar
		
		JMenuItem notesItem = new JMenuItem("Show Notes");					//creates a new menu item
		class MenuNotesListener implements ActionListener{				//new actionlistener calss
			ShowNotesDialog notesDialog;							//creates custom dialog obj
			@Override
			public void actionPerformed(ActionEvent e) {			//when an action is preformed
				if(notesDialog == null) {							//will check if a custom dialog is alreadu created
					notesDialog = new ShowNotesDialog();			//if not will create a new obj and make it visible
					notesDialog.setVisible(true);
				}
				else {
					notesDialog.setVisible(true);					//if one already exists, will make it visible
				}
			}
			
		}
		notesItem.addActionListener(new MenuNotesListener());			//adds action listener to menu item
		
		mainMenu.add(notesItem);										//adds the menu item to the menu
		
		return mainMenu;
	}
	
	public static JFrame getFrame() {				//getters to be used to update gui from other classes
		return frame;
	}
	
	public static ControlGUI getCtrlPanel() {
		return ctrlPanel;
	}

	public static void main(String[] args) {
		frame = new JFrame();								//main jframe
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			//closes on exit
		frame.setTitle("Clue Game");									//sets title of frame	
		frame.setSize(1250,1170);		
		
		JMenuBar fileMenu = new JMenuBar();				//creates new menu bar to be added to frame
		frame.setJMenuBar(fileMenu);							//sets master bar to frame 
		fileMenu.add(createMenuWithItems());												//and adds the menu to the menubar
		
		Board board = Board.getInstance();
		
		board.setConfigFiles("GameBoard.csv", "ClueRooms.txt");			//initializes the board
		board.initialize();
		
		frame.add(board, BorderLayout.CENTER);							//adds the board via paintComponent method to the JFrame 
		ctrlPanel = new ControlGUI();							//instantiates new obj
		frame.add(ctrlPanel, BorderLayout.SOUTH);						//adds to frame
		
		ctrlPanel.setGuess(new Solution("This","Default","Guess"));
		ctrlPanel.setResult("I guessed it!");
		ctrlPanel.showTurn("Some Player", 0);
		
		CardDisplayGUI cardPanel = new CardDisplayGUI(board);							//creates new instance of carddisplay for the gui
		frame.add(cardPanel, BorderLayout.EAST);										//adds it to the east part of the frame
		
		frame.setVisible(true);						//makes frame visible
																																			//using joptionpane to create a new dialog message that will print at the start of the game to tell player their name and how to start
		JOptionPane.showMessageDialog(frame, "You are " + Board.getInstance().getPlayers().get(0).getPlayerName() + ", press Next Player to begin play", "Welcome to Clue", JOptionPane.INFORMATION_MESSAGE);
	}

}

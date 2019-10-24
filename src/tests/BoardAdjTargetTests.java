package tests;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.BoardCell;

public class BoardAdjTargetTests {

	private static Board board;
	
	@BeforeClass
	public static void setUp() {
		
		board = Board.getInstance();														//getting single instance
		board.setConfigFiles("data/CTest_ClueLayout.csv", "data/CTest_ClueLegend.txt");		//setting config file names
		board.initialize();																	//initializing board, legend, and adjMatrix
	}
	
	@Test 
	public void walkwayAdjTest() {
		
		Set<BoardCell> testList = board.getAdjList(14, 6);				//Testing cell with all adjs as walkways
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(14, 5)));
		assertTrue(testList.contains(board.getCellAt(14, 7)));
		assertTrue(testList.contains(board.getCellAt(15, 6)));
		assertTrue(testList.contains(board.getCellAt(13, 6)));
		
		testList = board.getAdjList(16, 3);				//Testing cell with room below it
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(16, 2)));
		assertTrue(testList.contains(board.getCellAt(16, 4)));
		assertTrue(testList.contains(board.getCellAt(15, 3)));
		
		testList = board.getAdjList(13, 0);				//Testing cell on left edge of board also is next to room cell
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(14, 0)));
		
		testList = board.getAdjList(7, 5);				//Testing cell with door at wrong direction above it
		assertEquals(3, testList.size());
		assertTrue(testList.contains(board.getCellAt(8, 5)));
		assertTrue(testList.contains(board.getCellAt(7, 6)));
		assertTrue(testList.contains(board.getCellAt(7, 4)));
		
		testList = board.getAdjList(0, 6);				//Testing cell at top edge of board
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCellAt(0, 7)));
		assertTrue(testList.contains(board.getCellAt(1, 6)));
		
		testList = board.getAdjList(21, 12);				//Testing cell at bottom edge of board
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(20, 12)));
		
		testList = board.getAdjList(9, 20);				//Testing cell at right edge of board also is next to room cell
		assertEquals(2, testList.size());
		assertTrue(testList.contains(board.getCellAt(8, 20)));
		assertTrue(testList.contains(board.getCellAt(9, 19)));
		
	}
	
	@Test
	public void walkwayBesideDoorTests() {
		Set<BoardCell> testList = board.getAdjList(19, 6);				//Testing cell next to both a left and right direction door
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(19, 5)));
		assertTrue(testList.contains(board.getCellAt(19, 7)));
		assertTrue(testList.contains(board.getCellAt(18, 6)));
		assertTrue(testList.contains(board.getCellAt(20, 6)));
		
		testList = board.getAdjList(14, 2);				//Testing cell next to downward direction door
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(14, 3)));
		assertTrue(testList.contains(board.getCellAt(14, 1)));
		assertTrue(testList.contains(board.getCellAt(15, 2)));
		assertTrue(testList.contains(board.getCellAt(13, 2)));
		
		testList = board.getAdjList(15, 9);				//Testing cell next to upward direction door
		assertEquals(4, testList.size());
		assertTrue(testList.contains(board.getCellAt(14, 9)));
		assertTrue(testList.contains(board.getCellAt(16, 9)));
		assertTrue(testList.contains(board.getCellAt(15, 8)));
		assertTrue(testList.contains(board.getCellAt(15, 10)));
	}
	
	@Test
	public void cellInRoomAdjTests() {
		Set<BoardCell> testList = board.getAdjList(18, 7);				//Testing cells within rooms in different rooms
		assertEquals(0, testList.size());						//in library next to door
		
		testList = board.getAdjList(1, 2);				//in kitchen in middle
		assertEquals(0, testList.size());
		
		testList = board.getAdjList(17, 16);					//In den on left and right edge
		assertEquals(0, testList.size());
		
		testList = board.getAdjList(6, 17);					//in master bedroom on bottom edge
		assertEquals(0, testList.size());
		
	}
	
	@Test
	public void cellOnRoomExitAdjTests() {
		Set<BoardCell> testList = board.getAdjList(17, 0);		//testing cell on door with up direction		
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(16, 0)));
		
		testList = board.getAdjList(1, 10);						//testing cell on door with right direction
		assertEquals(1, testList.size());
		assertTrue(testList.contains(board.getCellAt(1, 11)));
		
		testList = board.getAdjList(20, 13);				
		assertEquals(1, testList.size());							//testing cell on door with left direction
		assertTrue(testList.contains(board.getCellAt(20, 12)));
		
		testList = board.getAdjList(19, 19);				
		assertEquals(1, testList.size());								//testing cell on door with downward direction
		assertTrue(testList.contains(board.getCellAt(20, 19)));
	}
	
	@Test
	public void targetVariousLengthTests() {
		board.calcTargets(4, 7, 2);									//testing cell with path length of 2
		Set<BoardCell> testList= board.getTargets();
		assertEquals(6, testList.size());
		
		assertTrue(testList.contains(board.getCellAt(6, 7)));
		assertTrue(testList.contains(board.getCellAt(2, 7)));
		assertTrue(testList.contains(board.getCellAt(4, 9)));
		assertTrue(testList.contains(board.getCellAt(5, 8)));
		assertTrue(testList.contains(board.getCellAt(3, 6)));
		assertTrue(testList.contains(board.getCellAt(6, 7)));
		
		board.calcTargets(13, 12, 3);									//testing cell with path length of 3
		testList= board.getTargets();
		assertEquals(10, testList.size());
		
		assertTrue(testList.contains(board.getCellAt(12, 10)));
		assertTrue(testList.contains(board.getCellAt(13, 11)));
		assertTrue(testList.contains(board.getCellAt(13, 9)));
		assertTrue(testList.contains(board.getCellAt(14, 10)));
		assertTrue(testList.contains(board.getCellAt(15,11)));
		assertTrue(testList.contains(board.getCellAt(16, 12)));
		assertTrue(testList.contains(board.getCellAt(15, 13)));
		assertTrue(testList.contains(board.getCellAt(14, 14)));
		assertTrue(testList.contains(board.getCellAt(12, 14)));
		assertTrue(testList.contains(board.getCellAt(12, 12)));
		
		board.calcTargets(8, 0, 5);									//testing cell with path length of 5
		testList= board.getTargets();
		assertEquals(5, testList.size());
		
		assertTrue(testList.contains(board.getCellAt(7, 2)));
		assertTrue(testList.contains(board.getCellAt(7, 4)));
		assertTrue(testList.contains(board.getCellAt(8, 5)));
		assertTrue(testList.contains(board.getCellAt(8, 3)));
		assertTrue(testList.contains(board.getCellAt(9, 4)));
		
		board.calcTargets(8, 14, 1);									//testing cell with path length of 1
		testList= board.getTargets();
		assertEquals(3, testList.size());
		
		assertTrue(testList.contains(board.getCellAt(9, 14)));
		assertTrue(testList.contains(board.getCellAt(7, 14)));
		assertTrue(testList.contains(board.getCellAt(8, 15)));
		
	}
	
	@Test
	public void roomEnterTargetsTests() {
		board.calcTargets(0, 11, 2);									//testing cell with path length of 2
		Set<BoardCell> testList= board.getTargets();					
		
		assertTrue(testList.contains(board.getCellAt(0, 10)));				//testing if the cell can move into both doors in its target range
		assertTrue(testList.contains(board.getCellAt(1, 10)));
		
		board.calcTargets(18, 18, 4);									//testing cell with path length of 4
		testList= board.getTargets();
		assertEquals(6, testList.size());
		
		assertTrue(testList.contains(board.getCellAt(21, 17)));			//testing if the cell can move into both doors in its target range
		assertTrue(testList.contains(board.getCellAt(19, 19)));
		
	}
	
	@Test
	public void roomExitTargetsTests() {
		board.calcTargets(6, 20, 2);									//testing cell with path length of 2
		Set<BoardCell> testList= board.getTargets();
		assertEquals(2, testList.size());
		
		assertTrue(testList.contains(board.getCellAt(8, 20)));			//testing if the cell in doorway will calc correct exit targets
		assertTrue(testList.contains(board.getCellAt(7, 19)));
		
		board.calcTargets(0, 14, 1);									//testing cell with path length of 1
		testList= board.getTargets();
		assertEquals(1, testList.size());
		
		assertTrue(testList.contains(board.getCellAt(0, 13)));			//testing if the cell in doorway will calc correct exit targets
	}
}

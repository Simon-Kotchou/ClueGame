package tests;

import static org.junit.Assert.*;

import java.util.Map;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;
import clueGame.DoorDirection;
import clueGame.BoardCell;

public class BoardConfigTest {
	
	public static Board board;
	public static final int LEGEND_SIZE = 11;						//constants that are used to assert tests later
	public static final int NUM_ROWS = 22;
	public static final int NUM_COLUMNS = 21;
	public static final int NUM_DOORWAYS = 16;

	@BeforeClass
	public static void initObj() {									//beforeclass method that sets up the board and loads the config files
		board = Board.getInstance();
		board.setConfigFiles("data/GameBoard.csv", "data/ClueRooms.txt");		
		board.initialize();
	}
	
	@Test
	public void legendSizeTest() {								//test that tests if the legend size is correct
		assertEquals(board.getLegend().size(), LEGEND_SIZE);
	}
	
	@Test
	public void legendEntriesTest() {
		assertFalse(board.getLegend().isEmpty());									//tests if legend is empty
		assertEquals(board.getLegend().get('X'), "Closet");
		assertTrue(board.getLegend().containsKey('D'));									//then tests if the legend contains the correct information from config files
		assertEquals(board.getLegend().get('R'), "Billiard room");
		assertTrue(board.getLegend().containsKey('C'));
		assertEquals(board.getLegend().get('W'), "Walkway");
	}
	
	@Test 
	public void boardDimensionTest(){											//tests if the rows and column dimensions of the board are correct
		assertEquals(board.getNumRows(), NUM_ROWS);
		assertEquals(board.getNumColumns(), NUM_COLUMNS);
		assertTrue(board.getNumRows()*board.getNumColumns() <= board.MAX_BOARD_SIZE);			//tests if the board size is less than or equal to the max board size
	}

	@Test
	public void doorwayTests() {
		assertEquals(board.getCellAt(6, 5).getDoorDirection(), DoorDirection.RIGHT);			//tests boardcells that are known to be doorways' directions 
		assertEquals(board.getCellAt(21, 15).getDoorDirection(), DoorDirection.LEFT);
		assertEquals(board.getCellAt(19, 19).getDoorDirection(), DoorDirection.DOWN);
		assertEquals(board.getCellAt(9, 2).getDoorDirection(), DoorDirection.UP);
		
		assertTrue(board.getCellAt(1, 10).isDoorway());			//tests different boardcells if they are a doorway or not 
		assertTrue(board.getCellAt(20, 13).isDoorway());
		assertFalse(board.getCellAt(14, 6).isDoorway());
		assertFalse(board.getCellAt(8, 10).isDoorway());
		assertFalse(board.getCellAt(12, 20).isDoorway());
		assertTrue(board.getCellAt(17, 0).isDoorway());
	}
	
	@Test
	public void numDoorwaysTest() {
		int doorwayCount = 0;
		for(int i = 0; i < board.getNumRows(); i++) {					//for that calculates all of the doorways on the board
			for(int j = 0; j < board.getNumColumns(); j++) {
				if(board.getCellAt(i, j).isDoorway()) {
					doorwayCount++;
				}
			}
		}
		assertEquals(doorwayCount, NUM_DOORWAYS);					//asserts that the loaded amount is equal to the expected
	}
	
	@Test
	public void initialTest() {
		assertEquals(board.getCellAt(9, 3).getInitial(), 'G');					//test that tests various boardcells if they have the correct initials
		assertEquals(board.getCellAt(9, 10).getInitial(), 'X');
		assertEquals(board.getCellAt(2, 18).getInitial(), 'M');
		assertEquals(board.getCellAt(18, 13).getInitial(), 'S');
		assertEquals(board.getCellAt(16, 6).getInitial(), 'W');
	}
}

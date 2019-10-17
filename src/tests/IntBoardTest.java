package tests;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Set;

import org.junit.Before;
import org.junit.jupiter.api.Test;

import experiment.BoardCell;
import experiment.IntBoard;

class IntBoardTest {
	
	private IntBoard board;

	@Before
	public void beforeAll() {
		board = new IntBoard();               //sets up a new IntBoard to be used in tests
	}
	
	/*
	 * Will first test calcAdjacencies function
	 * Will test the adjacencies for top left
	 */
	
	@Test
	public void testAdjacency0() {
		BoardCell cell = board.getCell(0,0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertEquals(2, testList.size());
	}
	
	/*
	 * Will test the adjacencies for bottom right 
	 */
	
	@Test
	public void testAdjacency1() {
		BoardCell cell = board.getCell(3,3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertEquals(2, testList.size());
	}
	
	/*
	 * Will test the adjacencies for right edge
	 */
	
	@Test
	public void testAdjacency2() {
		BoardCell cell = board.getCell(2,3);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(3, 3)));
		assertTrue(testList.contains(board.getCell(1, 3)));
		assertTrue(testList.contains(board.getCell(2, 2)));
		assertEquals(3, testList.size());
	}
	
	/*
	 * Will test the adjacencies for left edge
	 */
	
	@Test
	public void testAdjacency3() {
		BoardCell cell = board.getCell(2,0);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(3, 0)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertEquals(3, testList.size());
	}
	
	/*
	 * Will test second column middle of grid
	 */
	
	@Test
	public void testAdjacency4() {
		BoardCell cell = board.getCell(1,1);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 0)));
		assertTrue(testList.contains(board.getCell(0, 1)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertEquals(4, testList.size());
	}
	
	/*
	 * Will test middle of the grid
	 */
	
	@Test
	public void testAdjacency5() {
		BoardCell cell = board.getCell(2,2);
		Set<BoardCell> testList = board.getAdjList(cell);
		assertTrue(testList.contains(board.getCell(1, 2)));
		assertTrue(testList.contains(board.getCell(2, 1)));
		assertTrue(testList.contains(board.getCell(2, 3)));
		assertTrue(testList.contains(board.getCell(3, 2)));
		assertEquals(4, testList.size());
	}
	
	/*
	 * Now will test calcTarget function
	 * Will test first a starting cell of 0,1 and a pathlength of 2
	 */
	
	@Test
	public void testTargets01_2() {
		BoardCell cell = board.getCell(0,1);
		board.calcTargets(cell,2);
		Set<BoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(1, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertEquals(4, targets.size());
	}
	
	/*
	 * Now will test starting cell at 3,3 and pathlength of 3
	 */
	
	@Test
	public void testTargets33_3() {
		BoardCell cell = board.getCell(3,3);
		board.calcTargets(cell,3);
		Set<BoardCell> targets = board.getTargets();
		assertTrue(targets.contains(board.getCell(0, 3)));
		assertTrue(targets.contains(board.getCell(1, 2)));
		assertTrue(targets.contains(board.getCell(3, 0)));
		assertTrue(targets.contains(board.getCell(2, 1)));
		assertTrue(targets.contains(board.getCell(3, 2)));
		assertTrue(targets.contains(board.getCell(2, 3)));
		assertEquals(6, targets.size());
	}
}

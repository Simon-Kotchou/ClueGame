package tests;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import clueGame.Board;

public class BoardAdjTargetTests {

	private static Board board;
	@BeforeClass
	public static void setUp() {
		
		board = Board.getInstance();														//getting single instance
		board.setConfigFiles("data/CTest_ClueLayout.csv", "data/CTest_ClueLegend.txt");		//setting config file names
		board.initialize();																	//initializing board, legend, and adjMatrix
	}

}

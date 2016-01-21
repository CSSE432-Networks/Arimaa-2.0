package testing;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import game.*;
import game.Piece.PieceType;

import org.junit.Test;

public class TestGame {
	BoardState blank = new BoardState(new char[][] { 
		{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, 
		{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
		{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, 
		{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
		{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, 
		{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
		{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, 
		{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, }, 0);
	
	BoardState b = new BoardState(new char[][] {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'C', 'E', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', 'K', 'R' }, }, 0);

	BoardState b2 = new BoardState(new char[][] {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'k', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'C', 'E', 'd', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'r', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', 'r', 'K', 'R' }, }, 0);
	
	BoardState standard = new BoardState(new char[][] {
			{ 'K', 'D', 'H', 'C', 'E', 'H', 'D', 'K' },
			{ 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ 'r', 'r', 'r', 'r', 'r', 'r', 'r', 'r' },
			{ 'k', 'd', 'h', 'c', 'e', 'h', 'd', 'k' }, }, 0);

	@Test
	public void testInitializes() {
		Game g = new Game(null);
		assertNotNull(g);
	}

	@Test
	public void testInitializesWithBoardState() {
		Game g = new Game(b);

		assertEquals(g.currentBoard.getBoardArray()[4][3], 'C');
		assertEquals(g.currentBoard.getBoardArray()[4][4], 'E');
		assertEquals(g.currentBoard.getBoardArray()[7][3], ' ');
		assertEquals(g.currentBoard.getBoardArray()[7][7], 'R');
	}

	@Test
	public void testGetPieceExists() {
		Game g = new Game(b);
		assertEquals(g.getSpace(4, 3), new Piece(PieceType.Camel, null,
				Piece.Owner.Player1));
	}

	@Test
	public void testGetPieceNotExists() {
		Game g = new Game(b);
		assertEquals(g.getSpace(0, 0), null);
	}

	@Test
	public void testGetPieceExistsAgain() {
		Game g = new Game(b);
		assertEquals(g.getSpace(7, 7), new Piece(PieceType.Rabbit, null,
				Piece.Owner.Player1));
		assertEquals(g.getSpace(7, 7), new Piece(PieceType.Rabbit, null,
				Piece.Owner.Player1));
	}

	@Test
	public void testGetSpaceInvalidRow1() {
		Game g = new Game();
		assertNull(g.getSpace(-1, 0));
	}

	@Test
	public void testGetSpaceInvalidRow2() {
		Game g = new Game();
		assertNull(g.getSpace(8, 0));
	}

	@Test
	public void testGetSpaceInvalidColumn1() {
		Game g = new Game();
		assertNull(g.getSpace(0, -1));
	}

	@Test
	public void testGetSpaceInvalidColumn2() {
		Game g = new Game();
		assertNull(g.getSpace(0, 8));
	}

	//testing Move
	@Test
	public void testMoveLegal() {
		Game g = new Game(b);
		assertEquals(new Piece(PieceType.Rabbit, null, Piece.Owner.Player1),
				g.getSpace(7, 7));
		assertTrue(g.move(7, 7, 0));
		assertEquals(new Piece(PieceType.Rabbit, null, Piece.Owner.Player1),
				g.getSpace(6, 7));
		assertEquals(null, g.getSpace(7, 7));
	}
	
	@Test
	public void testMoveIllegalNotYourTurn() {
		Game g = new Game();
		assertFalse(g.move(7, 7, 2));
	}

	@Test
	public void testMoveIllegalDown() {
		Game g = new Game();
		g.setPlayerTurn(2);
		assertFalse(g.move(7, 7, 2));
	}

	@Test
	public void testMoveIllegalUp() {
		Game g = new Game();
		assertFalse(g.move(0, 0, 0));

	}

	@Test
	public void testMoveIllegalRight() {
		Game g = new Game();
		g.setPlayerTurn(2);
		assertFalse(g.move(7, 7, 1));
	}

	@Test
	public void testMoveIllegalLeft() {
		Game g = new Game();
		assertFalse(g.move(0, 0, 3));
	}

	@Test
	public void testCannotMoveUpIntoOccupiedSpace() {
		Game g = new Game(standard);
		assertEquals(new Piece(PieceType.Rabbit, null, Piece.Owner.Player1),
				g.getSpace(1, 0));
		assertFalse(g.move(1, 0, 0));
		assertEquals(new Piece(PieceType.Rabbit, null, Piece.Owner.Player1),
				g.getSpace(1, 0));
	}

	@Test
	public void testCannotMoveRightIntoOccupiedSpace() {
		Game g = new Game(standard);
		assertEquals(new Piece(PieceType.Dog, null, Piece.Owner.Player1),
				g.getSpace(0, 1));
		assertFalse(g.move(0, 1, 1));
		assertEquals(new Piece(PieceType.Dog, null, Piece.Owner.Player1),
				g.getSpace(0, 1));
	}

	@Test
	public void testCannotMoveDownIntoOccupiedSpace() {
		Game g = new Game(standard);
		assertEquals(new Piece(PieceType.Dog, null, Piece.Owner.Player1),
				g.getSpace(0, 1));
		assertFalse(g.move(0, 1, 2));
		assertEquals(new Piece(PieceType.Dog, null, Piece.Owner.Player1),
				g.getSpace(0, 1));
	}

	@Test
	public void testCannotMoveLeftIntoOccupiedSpace() {
		Game g = new Game(standard);
		assertEquals(new Piece(PieceType.Dog, null, Piece.Owner.Player1),
				g.getSpace(0, 1));
		assertFalse(g.move(0, 1, 3));
		assertEquals(new Piece(PieceType.Dog, null, Piece.Owner.Player1),
				g.getSpace(0, 1));
	}
	
	@Test
	public void testEndTurn(){
		Game g = new Game();
		assertTrue(1 == g.getPlayerTurn());
		g.endTurn();
		assertTrue(2 == g.getPlayerTurn());
		g.endTurn();
		assertTrue(1 == g.getPlayerTurn());
	}

	
	BoardState freezingBoard = new BoardState(new char[][] {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'R', 'E', ' ', ' ' },
			{ ' ', ' ', ' ', 'R', 'c', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', 'K', 'R' }, }, 0);
	
	@Test
	public void testCannotMoveIfFrozenByStrongerOpposingPiece(){
		Game g = new Game(freezingBoard);
		assertFalse(g.move(4, 3, 0));
	}
	
	@Test
	public void testCanMoveIfFrozenByStrongerOpposingPieceButThawedByFriendlyPiece(){
		Game g = new Game(freezingBoard);
		assertTrue(g.move(3, 4, 0));
	}
	
	@Test
	public void testCreateConstructor() {
		Game g = new Game(standard);
		assertEquals(new Piece(PieceType.Rabbit, null, Piece.Owner.Player1),
				g.getSpace(1, 1));
	}

	@Test
	public void testNullMove() {
		Game g = new Game();
		assertFalse(g.move(4, 4, 0));
	}
	
	@Test 
	public void testInvalidMoveDirection(){
		Game g = new Game();
		assertFalse(g.move(1,0,5));
	}
	
	@Test
	public void testCannotMoveWithNoMovesLeft(){
		Game g = new Game(standard);
		assertTrue(g.move(1, 0, 2));
		assertTrue(g.move(2, 0, 0));
		assertTrue(g.move(1, 0, 2));
		assertTrue(g.move(2, 0, 0));

		assertFalse(g.move(1, 0, 2));
	}

	// Testing push method

	BoardState pushTestingBoard = new BoardState(new char[][] {
			{ ' ', 'r', ' ', ' ', ' ', ' ', 'R', 'e' },
			{ 'r', ' ', 'R', ' ', 'E', ' ', 'E', 'R' },
			{ ' ', 'R', 'E', 'R', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', 'R', ' ', ' ', 'd', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'k', 'E', 'c', ' ' },
			{ ' ', ' ', ' ', 'C', ' ', 'k', ' ', ' ' },
			{ 'r', ' ', 'C', 'r', 'C', ' ', ' ', ' ' },
			{ 'E', 'r', ' ', 'C', ' ', ' ', ' ', ' ' }, }, 0);
	
	@Test
	public void testPushNotEnoughMoves() {
		Game g = new Game(pushTestingBoard);
		assertTrue(g.move(1, 7, 2));
		assertTrue(g.move(2, 7, 2));
		assertTrue(g.move(3, 7, 2));
		assertFalse(g.push(4, 5, 0, 0));
	}

	@Test
	public void testPushInvalid() {
		Game g = new Game(b2);
		assertFalse(g.push(3, 3, 0, 0));
		assertFalse(g.push(4, 3, 1, 1));
		assertFalse(g.push(4, 3, 1, 0));
	}

	@Test
	public void testPushUp() {
		Game g = new Game(b2);
		assertTrue(g.push(4, 4, 0, 0));
		assertTrue(g.push(3, 4, 0, 0));
		g.endTurn();
		g.setPlayerTurn(1);
		assertTrue(g.push(2, 4, 0, 0));
		assertFalse(g.push(1, 4, 0, 0));
	}

	@Test
	public void testPushDown() {
		Game g = new Game(b2);
		assertTrue(g.push(4, 4, 2, 2));
		assertTrue(g.push(5, 4, 2, 2));
		assertFalse(g.push(6, 4, 2, 2));
		assertFalse(g.push(7, 4, 2, 2));
	}

	@Test
	public void testPushRight() {
		Game g = new Game(b2);
		assertTrue(g.push(4, 4, 1, 1));
		assertTrue(g.push(4, 5, 1, 1));
		assertFalse(g.push(4, 6, 1, 1));
		assertFalse(g.push(4, 7, 1, 1));
	}

	@Test
	public void testPushLeft() {
		Game g = new Game(b2);
		assertTrue(g.push(7, 6, 3, 3));
		assertTrue(g.push(7, 5, 3, 3));
		g.endTurn();
		g.setPlayerTurn(1);
		assertTrue(g.push(7, 4, 3, 3));
		assertTrue(g.push(7, 3, 3, 3));
		g.endTurn();
		g.setPlayerTurn(1);
		assertTrue(g.push(7, 2, 3, 3));
		assertFalse(g.push(7, 1, 3, 3));
	}

	@Test
	public void testPushWithDifferentDirections() {
		Game g = new Game(pushTestingBoard);
		assertTrue(g.push(4, 5, 1, 0));
		assertTrue(g.getSpace(4, 6).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
		assertTrue(g.getSpace(3, 6).equals(
				new Piece(PieceType.Camel, null, Piece.Owner.Player2)));
	}

	@Test
	public void testPushUpWithSamePlayersPieces() {
		Game g = new Game(pushTestingBoard);
		assertFalse(g.push(2, 2, 0, 0));
		assertTrue(g.getSpace(2, 2).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
		assertTrue(g.getSpace(1, 2).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player1)));
	}

	@Test
	public void testPushRightWithSamePlayersPieces() {
		Game g = new Game(pushTestingBoard);
		assertFalse(g.push(2, 2, 1, 1));
		assertTrue(g.getSpace(2, 2).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
		assertTrue(g.getSpace(2, 3).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player1)));
	}

	@Test
	public void testPushDownWithSamePlayersPieces() {
		Game g = new Game(pushTestingBoard);
		assertFalse(g.push(2, 2, 2, 2));
		assertTrue(g.getSpace(2, 2).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
		assertTrue(g.getSpace(3, 2).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player1)));
	}

	@Test
	public void testPushLeftWithSamePlayersPieces() {
		Game g = new Game(pushTestingBoard);
		assertFalse(g.push(2, 2, 3, 3));
		assertTrue(g.getSpace(2, 2).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
		assertTrue(g.getSpace(2, 1).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player1)));
	}

	@Test
	public void testThatPiecesMustBeStrongerToPushUp() {
		Game g = new Game(pushTestingBoard);
		assertFalse(g.push(6, 3, 0, 0));
		assertTrue(g.getSpace(6, 3).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player2)));
		assertTrue(g.getSpace(5, 3).equals(
				new Piece(PieceType.Camel, null, Piece.Owner.Player1)));
	}

	@Test
	public void testThatPiecesMustBeStrongerToPushRight() {
		Game g = new Game(pushTestingBoard);
		assertFalse(g.push(6, 3, 1, 1));
		assertTrue(g.getSpace(6, 3).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player2)));
		assertTrue(g.getSpace(6, 4).equals(
				new Piece(PieceType.Camel, null, Piece.Owner.Player1)));
	}

	@Test
	public void testThatPiecesMustBeStrongerToPushDown() {
		Game g = new Game(pushTestingBoard);
		assertFalse(g.push(6, 3, 2, 0));
		assertFalse(g.push(6, 3, 2, 1));
		assertFalse(g.push(6, 3, 2, 2));
		assertFalse(g.push(6, 3, 2, 3));
		assertTrue(g.getSpace(6, 3).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player2)));
		assertTrue(g.getSpace(7, 3).equals(
				new Piece(PieceType.Camel, null, Piece.Owner.Player1)));
	}

	@Test
	public void testThatPiecesMustBeStrongerToPushLeft() {
		Game g = new Game(pushTestingBoard);
		assertFalse(g.push(6, 3, 3, 0));
		assertFalse(g.push(6, 3, 3, 1));
		assertFalse(g.push(6, 3, 3, 2));
		assertFalse(g.push(6, 3, 3, 3));
		assertTrue(g.getSpace(6, 3).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player2)));
		assertTrue(g.getSpace(6, 2).equals(
				new Piece(PieceType.Camel, null, Piece.Owner.Player1)));
	}

	// Testing the pull method
	BoardState pullTestingBoard = new BoardState(new char[][] {
			{ ' ', 'r', ' ', ' ', ' ', ' ', 'R', 'e' },
			{ 'r', 'E', 'E', ' ', 'E', ' ', 'E', 'R' },
			{ ' ', 'E', 'E', 'r', ' ', 'r', 'r', ' ' },
			{ ' ', ' ', 'r', ' ', ' ', 'E', ' ', ' ' },
			{ ' ', ' ', ' ', 'r', 'E', ' ', 'E', 'r' },
			{ ' ', ' ', ' ', ' ', ' ', 'E', 'r', ' ' },
			{ 'r', 'e', ' ', 'E', 'e', 'r', ' ', ' ' },
			{ 'E', 'r', ' ', 'R', 'e', ' ', ' ', ' ' }, }, 0);

	@Test
	public void testBasicPullUp() {
		Game g = new Game(pullTestingBoard);
		assertTrue(g.pull(5, 5, 6, 5, 0));
		assertTrue(g.getSpace(5, 5).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player2)));
		assertTrue(g.getSpace(4, 5).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
	}

	@Test
	public void testBasicPullRight() {
		Game g = new Game(pullTestingBoard);
		assertTrue(g.pull(4, 4, 4, 3, 1));
		assertTrue(g.getSpace(4, 4).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player2)));
		assertTrue(g.getSpace(4, 5).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
	}

	@Test
	public void testBasicPullDown() {
		Game g = new Game(pullTestingBoard);
		// g.currentBoard.printBoard();
		assertTrue(g.pull(3, 5, 2, 5, 2));
		// g.currentBoard.printBoard();
		assertTrue(g.getSpace(3, 5).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player2)));
		assertTrue(g.getSpace(4, 5).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
	}

	@Test
	public void testBasicPullLeft() {
		Game g = new Game(pullTestingBoard);
		assertTrue(g.pull(4, 6, 4, 7, 3));
		assertTrue(g.getSpace(4, 6).equals(
				new Piece(PieceType.Rabbit, null, Piece.Owner.Player2)));
		assertTrue(g.getSpace(4, 5).equals(
				new Piece(PieceType.Elephant, null, Piece.Owner.Player1)));
	}

	@Test
	public void testPullWithNullPiece() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(0, 4, 0, 5, 3));
	}

	@Test
	public void testPullUpOffBoard() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(0, 7, 1, 7, 0));
	}

	@Test
	public void testPullRightOffBoard() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(0, 7, 0, 6, 1));
	}

	@Test
	public void testPullDownOffBoard() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(7, 0, 6, 0, 2));
	}

	@Test
	public void testPullLeftOffBoard() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(7, 0, 7, 1, 3));
	}

	@Test
	public void testPullUpIntoOccupiedSpace() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(2, 2, 3, 2, 0));
	}

	@Test
	public void testPullRightIntoOccupiedSpace() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(1, 1, 1, 0, 1));
	}

	@Test
	public void testPullDownIntoOccupiedSpace() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(1, 1, 0, 1, 2));
	}

	@Test
	public void testPullLeftIntoOccupiedSpace() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(2, 2, 2, 3, 3));
	}

	@Test
	public void testPullNothingUp() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(1, 4, 2, 4, 0));
	}

	@Test
	public void testPullNothingRight() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(1, 4, 1, 3, 1));
	}

	@Test
	public void testPullNothingDown() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(1, 4, 0, 4, 2));
	}

	@Test
	public void testPullNothingLeft() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(1, 4, 1, 5, 3));
	}

	@Test
	public void testPullOwnPieceUp() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(6, 1, 7, 1, 0));
		assertTrue(g.getSpace(5, 1) == null);
	}

	@Test
	public void testPullOwnPieceRight() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(6, 1, 6, 0, 1));
		assertTrue(g.getSpace(6, 2) == null);
	}

	@Test
	public void testPullOwnPieceDown() {
		Game g = new Game(pullTestingBoard);
		// g.currentBoard.printBoard();
		assertFalse(g.pull(1, 6, 0, 6, 2));
		// g.currentBoard.printBoard();
		assertFalse(g.getSpace(2, 6) == null);
	}

	@Test
	public void testPullOwnPieceLeft() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(1, 6, 1, 7, 3));
		assertTrue(g.getSpace(1, 5) == null);
	}

	@Test
	public void testBidirectionalPullUpRight() {
		Game g = new Game(pullTestingBoard);
		Piece p1 = g.getSpace(4, 4);
		Piece p2 = g.getSpace(4, 3);
		assertTrue(g.pull(4, 4, 4, 3, 0));
		assertTrue(g.getSpace(3, 4).equals(p1));
		assertTrue(g.getSpace(4, 4).equals(p2));
	}

	@Test
	public void testBidirectionalPullRightDown() {
		Game g = new Game(pullTestingBoard);
		Piece p1 = g.getSpace(3, 5);
		Piece p2 = g.getSpace(2, 5);
		assertTrue(g.pull(3, 5, 2, 5, 1));
		assertTrue(g.getSpace(3, 6).equals(p1));
		assertTrue(g.getSpace(3, 5).equals(p2));
	}

	@Test
	public void testBidirectionalPullDownLeft() {
		Game g = new Game(pullTestingBoard);
		Piece p1 = g.getSpace(4, 6);
		Piece p2 = g.getSpace(4, 7);
		// g.currentBoard.printBoard();
		assertTrue(g.pull(4, 6, 4, 7, 0));
		// g.currentBoard.printBoard();
		assertTrue(g.getSpace(3, 6).equals(p1));
		assertTrue(g.getSpace(4, 6).equals(p2));
	}

	@Test
	public void testBidirectionalPullLeftUp() {
		Game g = new Game(pullTestingBoard);
		// g.currentBoard.printBoard();
		Piece p1 = g.getSpace(5, 5);
		Piece p2 = g.getSpace(6, 5);
		assertTrue(g.pull(5, 5, 6, 5, 3));
		// g.currentBoard.printBoard();
		assertTrue(g.getSpace(5, 4).equals(p1));
		assertTrue(g.getSpace(5, 5).equals(p2));
	}

	@Test
	public void testPullPieceOfGreaterStrength() {
		Game g = new Game(pullTestingBoard);
		assertFalse(g.pull(7, 3, 7, 4, 3));
	}

	// Testing getDirection
	@Test
	public void testGetDirectionUp() {
		Game g = new Game();
		assertEquals(0, g.getDirection(1, 1, 0, 1));
	}

	@Test
	public void testGetDirectionRight() {
		Game g = new Game();
		assertEquals(1, g.getDirection(1, 1, 1, 2));
	}

	@Test
	public void testGetDirectionDown() {
		Game g = new Game();
		assertEquals(2, g.getDirection(1, 1, 2, 1));
	}

	@Test
	public void testGetDirectionLeft() {
		Game g = new Game();
		assertEquals(3, g.getDirection(1, 1, 1, 0));
	}

	@Test
	public void testGetDirectionNonAdjacent1() {
		Game g = new Game();
		assertEquals(-1, g.getDirection(1, 1, 7, 7));
	}
	
	@Test
	public void testGetDirectionNonAdjacent2() {
		Game g = new Game();
		assertEquals(-1, g.getDirection(1, 1, 1, 7));
	}
	
	@Test
	public void testGetDirectionNonAdjacent3() {
		Game g = new Game();
		assertEquals(-1, g.getDirection(1, 1, 7, 1));
	}

	// Testing saveFile
	@Test
	public void testSaveFile() throws IOException {
		FileWriter fw = null;
		File saveFile = new File("Resources/SaveTest1.txt");
		try {
			fw = new FileWriter(saveFile);
		} catch (IOException e) {
			e.printStackTrace();
		}
		Game g = new Game(b);
		assertTrue(g.saveFile(fw));
	}
	
	@Test
	public void testSaveFile2() throws IOException{
		Game g = new Game();
		assertFalse(g.saveFile(null));
	}

	// Testing loadFile
	@Test
	public void testLoadFileLoadsBoardState() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadTest1.txt"));
		BoardState board = new BoardState(new char[][] {
				{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
				{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, }, 0);
		Game g = new Game();
		assertTrue(g.loadFile(scanner));
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				assertEquals(board.getBoardArray()[i][k],
						g.currentBoard.getBoardArray()[i][k]);
			}
		}
		scanner.close();
	}

	@Test
	public void testLoadFileLoadsTurnCounter1() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadTest1.txt"));
		int turnCounter = 7;
		Game g = new Game();
		assertTrue(g.loadFile(scanner));
		assertEquals(turnCounter, g.getTurnCounter());
		scanner.close();
	}
	
	@Test
	public void testLoadFileLoadsTurnCounter2() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadTest2.txt"));
		int turnCounter = 10;
		Game g = new Game();
		assertTrue(g.loadFile(scanner));
		assertEquals(turnCounter, g.getTurnCounter());
		scanner.close();
	}

	@Test
	public void testLoadFileLoadsTurnTimer() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadTest1.txt"));
		int turnTimer = 120;
		Game g = new Game();
		assertTrue(g.loadFile(scanner));
		assertEquals(turnTimer, g.getTurnTimer());
		scanner.close();
	}

	@Test
	public void testLoadFileLoadsPlayer1Name() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadTest1.txt"));
		String name = "Jesse";
		Game g = new Game();
		assertTrue(g.loadFile(scanner));
		assertEquals(name, g.getP1Name());
		scanner.close();
	}

	@Test
	public void testLoadFileLoadsPlayer2Name() throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadTest1.txt"));
		String name = "Tayler";
		Game g = new Game();
		assertTrue(g.loadFile(scanner));
		assertEquals(name, g.getP2Name());
		scanner.close();
	}

	@Test
	public void testLoadFileReturnsFalseOnFailure1()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadFailure1.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}

	@Test
	public void testLoadFileReturnsFalseOnFailure2()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadFailure2.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}

	@Test
	public void testLoadFileReturnsFalseOnFailure3()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadFailure3.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}

	@Test
	public void testLoadFileReturnsFalseOnFailure4()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadFailure4.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}
	
	@Test
	public void testLoadFileReturnsFalseOnFailure5()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadFailure5.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}
	
	@Test
	public void testLoadFileReturnsFalseOnFailure6()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadFailure6.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}
	
	@Test
	public void testLoadFileReturnsFalseOnFailure7()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File("resources/LoadFailure7.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}

	@Test
	public void testLoadFileInvalidBoardCharacters1()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(
				"resources/LoadInvalidBoard1.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}

	@Test
	public void testLoadFileInvalidBoardCharacters2()
			throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(
				"resources/LoadInvalidBoard2.txt"));
		Game g = new Game();
		assertFalse(g.loadFile(scanner));
		scanner.close();
	}

	// Testing remove piece checks
	BoardState removeBoard = new BoardState(new char[][] {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', 'C', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'k', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'C', 'E', 'd', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'r', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', 'r', 'K', 'R' }, }, 0);
	BoardState removeBoard2 = new BoardState(new char[][] {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', 'D', ' ', ' ' },
			{ ' ', ' ', 'C', ' ', ' ', 'C', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'k', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'C', 'E', 'd', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'r', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', 'r', 'K', 'R' }, }, 0);

	@Test
	public void testRemovePieceValid() {
		Game g = new Game(removeBoard);
//		g.currentBoard.printBoard();
		assertTrue(g.move(6, 3, 0));
//		g.currentBoard.printBoard();
		assertEquals(null, g.getSpace(2, 2));
	}

	@Test
	public void testRemovePiece2() {
		Game g = new Game(removeBoard2);
		g.move(2, 2, 0);
		assertEquals(g.getSpace(2, 5), g.getSpace(2, 5));
		g.move(1, 5, 0);
		assertEquals(null, g.getSpace(2, 5));
	}

	// Testing win checks
	BoardState winBoardState = new BoardState(new char[][] {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', 'r', ' ', ' ', ' ', 'D', ' ', ' ' },
			{ ' ', ' ', 'C', ' ', ' ', 'C', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'k', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'C', 'E', 'd', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'r', ' ', ' ', ' ' },
			{ ' ', 'R', ' ', 'D', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', 'K', ' ' }, }, 0);

	@Test
	public void testPlayer2Win() {
		Game g = new Game(winBoardState);
		g.setPlayerTurn(2);
		assertTrue(g.move(1, 1, 0));
		assertEquals(2, g.getWinner());
	}

	@Test
	public void testPlayer1Win() {
		Game g = new Game(winBoardState);
		assertTrue(g.move(6, 1, 2));
		assertEquals(1, g.getWinner());
	}
	
	//Testing undoMove
	@Test
	public void testBaseUndoCase(){
		Game standardStart = new Game();
		Game g = new Game();
		g.move(1, 0, 2);
		g.undoMove();
		assertEquals(standardStart.getSpace(1, 0), g.getSpace(1,0));
	}
	
	@Test
	public void testUndoTwoMoves() {
		Game g = new Game(standard);
		g.move(1, 0, 2);
		g.move(2, 0, 2);
		g.undoMove();
		g.undoMove();
		assertEquals(new Piece('R'), g.getSpace(1,0));	}
	
	@Test
	public void testUndoThreeMoves() {
		Game g = new Game(standard);
		g.move(1, 0, 2);
		g.move(2, 0, 2);
		g.move(3, 0, 2);
		g.undoMove();
		g.undoMove();
		g.undoMove();
		assertEquals(new Piece('R'), g.getSpace(1,0));	}
	
	@Test
	public void testThatUndoCantCrossTurns(){
		Game g = new Game(standard);
		g.move(1, 0, 2);
		g.move(2, 0, 2);
		g.move(3, 0, 2);
		g.move(4, 0, 2);
		g.endTurn();
		g.undoMove();
		assertEquals(new Piece('R'), g.getSpace(5,0));
	}
	
	@Test
	public void testThatUndoGrantsMoves(){
		Game g = new Game();
		g.move(1, 0, 2);
		g.undoMove();
		assertEquals(4, g.getNumMoves());
	}
	
	@Test
	public void testSetWinner(){
		Game g = new Game();
		g.setWinner(1);
		assertEquals(1,g.getWinner());
	}
	
	BoardState win2BoardState = new BoardState(new char[][] {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', 'C', ' ', ' ', 'e', 'D', ' ', ' ' },
			{ ' ', ' ', 'C', ' ', ' ', 'C', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'k', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'C', 'E', 'd', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'r', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', 'K', ' ' }, }, 0);
	
	
	@Test 
	public void testWinWhenP1HasNoRabbits(){
		Game g = new Game(win2BoardState);
		g.move(1,1,2);
		assertEquals(2, g.getWinner());
	}
	
	@Test
	public void testCheckFriendlyAdjacentDownCase(){
		Game g = new Game(win2BoardState);
		assertTrue(g.move(1,5,1));		
	}
	
	@Test
	public void testEndMove(){
		Game g = new Game(standard);
		assertTrue(g.move(1,0,2));
		assertTrue(g.move(2,0,2));
		assertTrue(g.move(3,0,2));
		assertTrue(g.move(4,0,2));
		g.endTurn();
		assertTrue(g.move(6,1,0));
		assertTrue(g.move(5,1,0));
		assertTrue(g.move(4,1,0));
		assertTrue(g.move(3,1,0));
	}
	
	BoardState pushPullTestBoardState = new BoardState(new char[][] {
			{ ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' },
			{ ' ', 'C', ' ', 'r', ' ', ' ', ' ', 'D' },
			{ ' ', ' ', 'C', ' ', ' ', 'C', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'k', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'C', 'E', 'd', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', 'r', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', 'D', ' ', ' ', ' ', ' ' },
			{ ' ', ' ', ' ', ' ', ' ', ' ', 'K', ' ' }, }, 0);
	
	@Test
	public void testCantPushPullWith1Move(){
		Game g = new Game(pushPullTestBoardState);
		g.setPlayerTurn(1);
		assertTrue(g.move(1, 7, 3));
		assertTrue(g.move(1, 6, 3));
		assertTrue(g.move(1, 5, 3));
		assertFalse(g.pull(1, 4, 1, 3, 1));
	}
	
	@Test
	public void testCantPushPullWith1Move2(){
		Game g = new Game(pushPullTestBoardState);
		g.setPlayerTurn(1);
		assertTrue(g.move(1, 7, 3));
		assertTrue(g.move(1, 6, 3));
		assertTrue(g.move(1, 5, 3));
		assertFalse(g.push(1, 4, 3, 3));
	}
	
	@Test
	public void testSetTurnTimer(){
		Game g = new Game(standard);
		g.setTurnTimer(15);
		assertTrue(g.getTurnTimer() == 15);
	}
	
	@Test
	public void testPieceInventoryEmpty(){
		Game g = new Game(blank);
		assertFalse(g.pieceInventoryEmpty(1));
	}
	
	@Test
	public void testPieceInventoryEmpty2(){
		Game g = new Game(blank);
		assertTrue(g.placePiece(0, 0, 'R'));
		assertTrue(g.placePiece(0, 1, 'R'));
		assertTrue(g.placePiece(0, 2, 'R'));
		assertTrue(g.placePiece(0, 3, 'R'));
		assertTrue(g.placePiece(0, 4, 'R'));
		assertTrue(g.placePiece(0, 5, 'R'));
		assertTrue(g.placePiece(0, 6, 'R'));
		assertTrue(g.placePiece(0, 7, 'R'));
		
		assertTrue(g.placePiece(1, 0, 'C'));
		assertTrue(g.placePiece(1, 1, 'E'));
		assertTrue(g.placePiece(1, 2, 'H'));
		assertTrue(g.placePiece(1, 3, 'H'));
		assertTrue(g.placePiece(1, 4, 'K'));
		assertTrue(g.placePiece(1, 5, 'K'));
		assertTrue(g.placePiece(1, 6, 'D'));
		assertTrue(g.placePiece(1, 7, 'D'));
		assertTrue(g.pieceInventoryEmpty(1));
	}
	
	@Test
	public void testPlacePiece(){
		Game g = new Game(blank);
		assertTrue(g.placePiece(0, 2, 'K')); //K for kat...
		assertTrue(g.placePiece(1, 2, 'K'));
	}
	
	@Test
	public void testPlacePieceOnOccupiedSpace(){
		Game g = new Game(blank);
		assertTrue(g.placePiece(0, 2, 'K')); //K for kat...
		assertFalse(g.placePiece(0, 2, 'K'));
	}
	
	@Test
	public void testPlacePieceWhenAllPiecesPlaced(){
		Game g = new Game(blank);
		assertTrue(g.placePiece(0, 2, 'K'));
		assertTrue(g.placePiece(1, 2, 'K'));
		assertFalse(g.placePiece(1, 3, 'K'));
	}
	
	@Test
	public void testRemovePiece() {
		Game g = new Game(blank);
		g.placePiece(0, 2, 'K');
		assertTrue(g.removePiece(0, 2, 1));
	}
	
	@Test
	public void testRemovePieceOnEmptySpace() {
		Game g = new Game(blank);
		assertFalse(g.removePiece(0, 1, 1));
	}
	
	@Test
	public void testRemovePieceP1CantRemoveP2() {
		Game g = new Game(blank);
		g.placePiece(7, 1, 'c');
		assertFalse(g.removePiece(7, 1, 1));
	}
	
	@Test
	public void testRemovePieceP2CantRemoveP1() {
		Game g = new Game(blank);
		g.placePiece(7, 1, 'C');
		assertFalse(g.removePiece(7, 1, 2));
	}
	
}

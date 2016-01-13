package game;

/**
 * This class represents the states of the board.
 * 
 * @author shellajt
 *
 */
public class BoardState {
	// Fields
	private char[][] boardArray = new char[8][8]; // Represents the current
													// state of the board
	private int turnNumber;

	public BoardState(char[][] map, int turnNumber) {
		for (int i = 0; i < 8; i++) {
			for (int k = 0; k < 8; k++) {
				this.boardArray[i][k] = map[i][k];
			}
		}
		this.turnNumber = turnNumber;
	}

	public char[][] getBoardArray() {
		return boardArray;
	}

	public void setBoardArray(char[][] boardArray) {
		this.boardArray = boardArray;
	}

	public void setBoardSpace(int row, int column, String piece) {
		this.boardArray[row][column] = piece.charAt(0);
	}

	public int getTurnNumber() {
		return turnNumber;
	}

	public void setTurnNumber(int turnNumber) {
		this.turnNumber = turnNumber;
	}

	public void incrementTurn() {
		this.turnNumber++;
	}

	@Override
	public BoardState clone() {
		return new BoardState(boardArray, turnNumber);
	}
}

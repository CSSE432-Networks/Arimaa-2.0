package game;

import game.Piece.Owner;
import game.Piece.PieceType;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public class Game {
    // fields
    ArrayList<BoardState> boards = new ArrayList<BoardState>();
    public BoardState currentBoard = null;
    int moveTimer = 0;
    int p1TimeBank = 0;
    int p2TimeBank = 0;
    int turnCounter = 0;
    String p1Name = "Player1";
    String p2Name = "Player2";
    // 0 is nobody, 1 is player1, 2 is player2
    private int winner = 0;
    private int numMoves = 4;
    private int playerTurn = 1;
    public boolean isPushPull;
    private HashMap<Character, Integer> pieceInventory;

    public Game(BoardState b) {
        currentBoard = b;
        initializeInventory();
    }

    public boolean pieceInventoryEmpty(int player) {
        if (player == 1) {
            if ((this.pieceInventory.get('R') == 0) && (this.pieceInventory.get('C') == 0)
                    && (this.pieceInventory.get('E') == 0) && (this.pieceInventory.get('H') == 0)
                    && (this.pieceInventory.get('K') == 0) && (this.pieceInventory.get('D') == 0)) {
                return true;
            }
        } else if (player == 2) {
            if ((this.pieceInventory.get('r') == 0) && (this.pieceInventory.get('c') == 0)
                    && (this.pieceInventory.get('e') == 0) && (this.pieceInventory.get('h') == 0)
                    && (this.pieceInventory.get('k') == 0) && (this.pieceInventory.get('d') == 0)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a board with a default starting layout
     */
    public Game() {
        /*
		 * currentBoard = new BoardState(new char[][] { { 'K', 'D', 'H', 'C',
		 * 'E', 'H', 'D', 'K' }, { 'R', 'R', 'R', 'R', 'R', 'R', 'R', 'R' }, { '
		 * ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, { ' ', ' ', ' ', ' ', ' ', '
		 * ', ' ', ' ' }, { ' ', ' ', ' ', ' ', ' ', ' ', ' ', ' ' }, { ' ', '
		 * ', ' ', ' ', ' ', ' ', ' ', ' ' }, { 'r', 'r', 'r', 'r', 'r', 'r',
		 * 'r', 'r' }, { 'k', 'd', 'h', 'c', 'e', 'h', 'd', 'k' }, }, 0);
		 */
        currentBoard = new BoardState(
                new char[][]{{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},},
                0);
        initializeInventory();
    }

    /**
     * Initializes player inventories at the start of the game.
     */
    private void initializeInventory() {
        this.pieceInventory = new HashMap<Character, Integer>();
        this.pieceInventory.put('E', 1);
        this.pieceInventory.put('C', 1);
        this.pieceInventory.put('H', 2);
        this.pieceInventory.put('D', 2);
        this.pieceInventory.put('K', 2);
        this.pieceInventory.put('R', 8);
        this.pieceInventory.put('e', 1);
        this.pieceInventory.put('c', 1);
        this.pieceInventory.put('h', 2);
        this.pieceInventory.put('d', 2);
        this.pieceInventory.put('k', 2);
        this.pieceInventory.put('r', 8);
    }

    public boolean placePiece(int row, int column, char piece) {
        if (pieceInventory.get(piece) <= 0) {
            return false;
        }

        char[][] boardArray = this.currentBoard.getBoardArray();
        if (boardArray[row][column] != ' ') {
            return false;
        }

        this.pieceInventory.put(piece, this.pieceInventory.get(piece) - 1);

        boardArray[row][column] = piece;
        this.currentBoard.setBoardArray(boardArray);

        return true;
    }

    public boolean removePiece(int row, int column, int player) {
        char[][] boardArray = this.currentBoard.getBoardArray();
        if (boardArray[row][column] == ' ') {
            return false;
        }

        char removedPiece = boardArray[row][column];

        if (player == 1 && !Character.isUpperCase(removedPiece)) return false;
        if (player == 2 && !Character.isLowerCase(removedPiece)) return false;

        this.pieceInventory.put(removedPiece, this.pieceInventory.get(removedPiece) + 1);

        boardArray[row][column] = ' ';
        this.currentBoard.setBoardArray(boardArray);

        return true;
    }

    /**
     * @param x
     * @param y
     * @return
     */
    public Piece getSpace(int row, int column) {
        if (row < 0 || row > 7 || column < 0 || column > 7)
            return null;
        if (currentBoard.getBoardArray()[row][column] == ' ')
            return null;
        return new Piece(currentBoard.getBoardArray()[row][column]);

    }

    /**
     * @param row
     * @param column
     * @param dir    0: up, 1: right, 2: down, 3: left
     * @return
     */
    public boolean move(int row, int column, int dir) {
        if (numMoves <= 0) {
            return false;
        }
        if (!isValidMoveFromSquare(row, column)) {
            return false;
        }
        boards.add(currentBoard);
        currentBoard = currentBoard.clone();
        switch (dir) {
            case 0:
                // Moving UP
                if (isValidMoveSquare(row - 1, column)) {
                    switchPiece(row, column, row - 1, column);
                    endMove();
                    return true;
                }
                return false;
            case 1:
                // Moving RIGHT
                if (isValidMoveSquare(row, column + 1)) {
                    switchPiece(row, column, row, column + 1);
                    endMove();
                    return true;
                }
                return false;
            case 2:
                // Moving DOWN
                if (isValidMoveSquare(row + 1, column)) {
                    switchPiece(row, column, row + 1, column);
                    endMove();
                    return true;
                }
                return false;
            case 3:
                // Moving LEFT
                if (isValidMoveSquare(row, column - 1)) {
                    switchPiece(row, column, row, column - 1);
                    endMove();
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean isValidMoveFromSquare(int row, int column) {
        if (getSpace(row, column) == null) {
            return false;
        }
        if (getSpace(row, column).getOwner() != Owner.values()[(getPlayerTurn() - 1)] && !isPushPull) {
            return false;// not your turn
        }
        if ((checkStrongerAdjacent(row, column) && !checkFriendlyAdjacent(row, column)) && !isPushPull) {
            return false;// can't move
        }
        return true;
    }

    public boolean isValidMoveSquare(int row, int column) {
        if (row >= 0 && row < 8 && column >= 0 && column < 8 && currentBoard.getBoardArray()[row][column] == ' ')
            return true;
        return false;
    }

    /**
     * This methods checks piece death and victory conditions
     */
    private void endMove() {
        checkTrapSquares();
        checkWin();
        numMoves--;
        isPushPull = false;
    }

    public void endTurn() {
        if (getPlayerTurn() == 1) {
            setPlayerTurn(2);
        } else {
            setPlayerTurn(1);
        }
        numMoves = 4;
        turnCounter++;
        checkTrapSquares();
        isPushPull = false;
    }

    //DOCME: extracted this into its own method! tests still work - Tayler
    private void checkTrapSquares() {
        checkDeaths(2, 2);
        checkDeaths(2, 5);
        checkDeaths(5, 2);
        checkDeaths(5, 5);
    }

    // This method checks both rows for rabbits of the opposite side
    private void checkWin() {

        if (checkTopAndBottomRows())
            return;

        if (checkIfRabbitsExist(Piece.Owner.Player1))
            return;

        checkIfRabbitsExist(Piece.Owner.Player2);

    }

    // Checks if a player has gotten a rabbit to the opponents home row
    private boolean checkTopAndBottomRows() {
        // check top row
        for (int i = 0; i < 8; i++) {
            if (getSpace(0, i) != null) {
                if (getSpace(0, i).equals(new Piece(PieceType.Rabbit, null, Piece.Owner.Player2))) {
                    winner = 2;
                    return true;
                }
            }

            if (getSpace(7, i) != null) {
                if (getSpace(7, i).equals(new Piece(PieceType.Rabbit, null, Piece.Owner.Player1))) {
                    winner = 1;
                    return true;
                }
            }
        }
        return false;
    }

    // Checks to see if players are out of rabbits
    private boolean checkIfRabbitsExist(Owner player) {
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // and short circuits if null preventing nullpointerexception
                if (getSpace(i, j) != null && getSpace(i, j).equals(new Piece(PieceType.Rabbit, null, player))) {
                    return false;
                }
            }
        }
        if (player.equals(Piece.Owner.Player1))
            winner = 2;
        else
            winner = 1;
        return true;
    }

    /**
     * Piece death occurs when pieces are on the squares (2,2), (2,5), (5,2),
     * (5,5), and has no friendly adjacent pieces to it
     */
    private void checkDeaths(int row, int col) {
        if (this.getSpace(row, col) == (null))
            return;// an empty piece doesn't need to be checked

        if (checkFriendlyAdjacent(row, col)) {
            return;
        }
        // no adjacent friendly pieces, remove this one
        char[][] temp = this.currentBoard.getBoardArray();
        temp[row][col] = ' ';
        this.currentBoard.setBoardArray(temp);
    }

    private boolean checkFriendlyAdjacent(int row, int col) {
        Piece cen = this.getSpace(row, col);
        Piece up = this.getSpace(row - 1, col);
        Piece down = this.getSpace(row + 1, col);
        Piece left = this.getSpace(row, col - 1);
        Piece right = this.getSpace(row, col + 1);
        Owner own = cen.getOwner();
        if (up != null) {
            if (up.getOwner() == own)
                return true;
        }
        if (down != null) {
            if (down.getOwner() == own)
                return true;
        }
        if (right != null) {
            if (right.getOwner() == own)
                return true;
        }
        if (left != null) {
            if (left.getOwner() == own)
                return true;
        }
        return false;
    }

    private boolean checkStrongerAdjacent(int row, int col) {
        Piece cen = this.getSpace(row, col);
        Piece up = this.getSpace(row - 1, col);
        Piece down = this.getSpace(row + 1, col);
        Piece left = this.getSpace(row, col - 1);
        Piece right = this.getSpace(row, col + 1);
        if (up != null) {
            if (checkStrong(up, cen)) return true;
        }
        if (down != null) {
            if (checkStrong(down, cen)) return true;
        }
        if (right != null) {
            if (checkStrong(right, cen)) return true;
        }
        if (left != null) {
            if (checkStrong(left, cen)) return true;
        }
        return false;
    }

    private boolean checkStrong(Piece one, Piece two) {
        if (one.getOwner() != two.getOwner() && one.isStrongerThan(two)) {
            return true;
        }
        return false;
    }

    // helper for move
    private void switchPiece(int row1, int column1, int row2, int column2) {
        char[][] boardArray = currentBoard.getBoardArray();
        char temp = boardArray[row1][column1];

        boardArray[row1][column1] = boardArray[row2][column2];
        boardArray[row2][column2] = temp;

        currentBoard.setBoardArray(boardArray);
    }

    /**
     * 0: up, 1: right, 2: down, 3: left
     *
     * @param row
     * @param column
     * @param dir1   the direction the pushing piece will move
     * @param dir2   the direction the pushed piece will move
     * @return
     */
    public boolean push(int row, int column, int dir1, int dir2) {
        if (!isValidSquareToPushFrom(row, column))
            return false;
        isPushPull = true;
        boolean worked = false;
        switch (dir1) {
            case 0:
                if (row - 1 >= 0) {
                    worked = enactPush(row, column, row - 1, column, dir1, dir2);
                }
                break;
            case 1:
                if (column + 1 <= 7) {
                    worked = enactPush(row, column, row, column + 1, dir1, dir2);
                }
                break;
            case 2:
                if (row + 1 <= 7) {
                    worked = enactPush(row, column, row + 1, column, dir1, dir2);
                }
                break;
            case 3:
                if (column - 1 >= 0) {
                    worked = enactPush(row, column, row, column - 1, dir1, dir2);
                }
                break;
        }
        isPushPull = worked;
        if (worked == true) {
            this.currentBoard.setPushPull(true);
        }
        isPushPull = false;
        return worked;
    }

    private boolean enactPush(int rowPushing, int columnPushing, int rowPushed, int columnPushed, int dir1, int dir2) {
        Piece pushingPiece = getSpace(rowPushing, columnPushing);
        Piece pushedPiece = getSpace(rowPushed, columnPushed);
        if (pieceCanPush(pushingPiece, pushedPiece) && move(rowPushed, columnPushed, dir2)) {
            this.currentBoard.setPushPull(true);
            isPushPull = true;
            // should always be true
            return move(rowPushing, columnPushing, dir1);
        }
        return false;
    }

    private boolean pieceCanPush(Piece pushingPiece, Piece pushedPiece) {
        return pushedPiece != null && pushingPiece.isStrongerThan(pushedPiece)
                && pushingPiece.getOwner() != pushedPiece.getOwner();
    }

    private boolean isValidSquareToPushFrom(int row, int column) {
        if (numMoves <= 1)
            return false; // can't push/pull with only one move
        if (getSpace(row, column) == null) {
            return false; // trying to push with an empty square
        }
        if (getSpace(row, column).getOwner() != Owner.values()[(getPlayerTurn() - 1)])
            return false;// not your turn
        return true;
    }

    /**
     * 0: up, 1: right, 2: down, 3: left
     *
     * @param row        : row that contains the pulling piece
     * @param column     : column that contains the pulling piece
     * @param direction1 : direction the pulling piece will move
     * @param direction2 : direction the piece being pulled will move
     * @return True if pull succeeds, False if it fails
     */
    public boolean pull(int row1, int column1, int row2, int column2, int direction1) {
        if (!isValidSquaretoPullFrom(row1, column1, row2, column2)) {
            return false;
        }
        // Get direction that pulled piece will move
        int direction2 = getDirection(row2, column2, row1, column1);
        getClass();

        // Check that getDirection didn't fail
        isPushPull = true;
        boolean worked = false;
        // Attempt to perform move operations on both pieces
        switch (direction1) {
            case 0:
                if (tryPull(getSpace(row1, column1), getSpace(row2, column2), row1, column1, direction1)) {// pieceCanPush(getSpace(row1, direction1)
                    this.currentBoard.setPushPull(true);
                    isPushPull = true;
                    if (move(row2, column2, direction2)) {
                        this.currentBoard.setPushPull(true);
                        worked = true;
                    } else {
                        isPushPull = false;
                        undoMove();
                        worked = false;
                    }
                }
                break;
            case 1:
                if (tryPull(getSpace(row1, column1), getSpace(row2, column2), row1, column1, direction1)) {
                    this.currentBoard.setPushPull(true);
                    isPushPull = true;
                    if (move(row2, column2, direction2)) {
                        this.currentBoard.setPushPull(true);
                        ;
                        worked = true;
                    } else {
                        isPushPull = false;
                        undoMove();
                        worked = false;
                    }
                }
                break;
            case 2:
                if (tryPull(getSpace(row1, column1), getSpace(row2, column2), row1, column1, direction1)) {
                    this.currentBoard.setPushPull(true);
                    isPushPull = true;
                    if (move(row2, column2, direction2)) {
                        this.currentBoard.setPushPull(true);
                        worked = true;
                    } else {
                        isPushPull = false;
                        undoMove();
                        worked = false;
                    }
                }
                break;
            case 3:
                if (tryPull(getSpace(row1, column1), getSpace(row2, column2), row1, column1, direction1)) {
                    this.currentBoard.setPushPull(true);
                    isPushPull = true;
                    if (move(row2, column2, direction2)) {
                        this.currentBoard.setPushPull(true);
                        worked = true;
                    } else {
                        isPushPull = false;
                        undoMove();
                        worked = false;
                    }
                }
                break;
        }
        isPushPull = worked;
        isPushPull = false;
        return worked;
    }

    private boolean tryPull(Piece space, Piece space2, int row1, int column1, int direction1) {
        return pieceCanPush(space, space2) && move(row1, column1, direction1);
    }

    private boolean isValidSquaretoPullFrom(int row1, int column1, int row2, int column2) {
        if (numMoves <= 1)
            return false; // can't push/pull with only one move
        // Check that both pieces exist
        if (getSpace(row1, column1) == null || getSpace(row2, column2) == null) {
            return false;
        }

        // Check that pulling piece is strong than other piece
        if (!getSpace(row1, column1).isStrongerThan(getSpace(row2, column2))) {
            return false;
        }

        if (getSpace(row1, column1).getOwner() != Owner.values()[(getPlayerTurn() - 1)])
            return false;// not your turn
        return true;
    }

    /**
     * 0: up, 1: right, 2: down, 3: left
     *
     * @param row1    : row of space1
     * @param column1 : column of space1
     * @param row2    : row of space2
     * @param column2 : column of space2
     * @return integer representing the direction required to move from space1
     * to space2
     */
    public int getDirection(int row1, int column1, int row2, int column2) {
        if (row1 == row2) {
            if (column1 - 1 == column2)
                return 3;
            else if (column1 + 1 == column2)
                return 1;
        }
        if (column1 == column2) {
            if (row1 - 1 == row2)
                return 0;
            else if (row1 + 1 == row2)
                return 2;
        }
        return -1;
    }

    // EDITED 2015-12-09: Changed functionality so undo only reverts one move at
    // a time, not the player's whole turn.
    public void undoMove() {
        if (this.numMoves == 4)
            return;

        if (this.currentBoard.getPushPull()) {
            this.currentBoard = this.boards.get(boards.size() - 2);
            this.boards.remove(this.boards.size() - 1);
            this.boards.remove(this.boards.size() - 1);
            this.numMoves += 2;
        } else {
            this.currentBoard = this.boards.get(boards.size() - 1);
            this.boards.remove(this.boards.size() - 1);
            this.numMoves += 1;
        }
    }

    public boolean loadFile(Scanner scanner) {
        // Setup to use Scanner
        scanner.useDelimiter(",");
        BoardState boardToSet = new BoardState(
                new char[][]{{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},},
                0);
        if (!parseLoadedBoardState(scanner, boardToSet)) return false;


        // Parse turnCounter, p1Name, p2Name
        if (!verifyScanner(scanner)) return false;
        int turnCounter = scanner.nextInt();

        if (!verifyScanner(scanner)) return false;
        int turnTimer = scanner.nextInt();

        if (!verifyScanner(scanner)) return false;
        String p1name = scanner.next();

        if (!verifyScanner(scanner)) return false;
        String p2name = scanner.next();

        scanner.close();

        // Successful load! Push all changes to game permanently
        this.currentBoard = boardToSet;
        this.turnCounter = turnCounter;
        this.moveTimer = turnTimer;
        this.p1Name = p1name;
        this.p2Name = p2name;

        if (this.turnCounter % 2 == 1) {
            this.playerTurn = 2;
        } else {
            this.playerTurn = 1;
        }
        return true;
    }

    //extracted this! - Tayler
    private boolean parseLoadedBoardState(Scanner scanner, BoardState bs) {
        String[] validBoardCharactersArray = {" ", "E", "C", "H", "D", "K", "R", "e", "c", "h", "d", "k", "r"};
        ArrayList<String> vbc = new ArrayList<String>();
        for (String s : validBoardCharactersArray) {
            vbc.add(s);
        }

        // Parse boardState
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                if (!scanner.hasNext()) {
                    scanner.close();
                    return false;
                }
                String next = scanner.next();
                if (!vbc.contains(next)) {
                    scanner.close();
                    return false;
                }
                bs.setBoardSpace(i, k, next);
            }
        }

        return true;
    }

    //and this! - Tayler
    private boolean verifyScanner(Scanner scanner) {
        if (!scanner.hasNext()) {
            scanner.close();
            return false;
        }
        return true;
    }

    // new for networks
    public boolean loadFileFromString(String boardstate) {
        // assumes be have valid boardstate string
        String[] values = boardstate.split(",");
        BoardState boardToSet = new BoardState(
                new char[][]{{' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},
                        {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '}, {' ', ' ', ' ', ' ', ' ', ' ', ' ', ' '},},
                0);

        String[] validBoardCharactersArray = {" ", "E", "C", "H", "D", "K", "R", "e", "c", "h", "d", "k", "r"};
        ArrayList<String> vbc = new ArrayList<String>();
        for (String s : validBoardCharactersArray) {
            vbc.add(s);
        }

        // Parse boardState
        int count = 0;
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                String next = values[count];
                if (!vbc.contains(next)) {
                    return false;
                }
                boardToSet.setBoardSpace(i, k, next);
                count++;
            }
        }

        // Parse turnCounter, p1Name, p2Name
        int turnCounter = Integer.parseInt(values[count]);
        count++;

        int turnTimer = Integer.parseInt(values[count]);
        count++;

        String p1name = values[count];
        count++;

        String p2name = values[count];
        count++;

        // Successful load! Push all changes to game permanently
        this.currentBoard = boardToSet;
        this.turnCounter = turnCounter;
        this.moveTimer = turnTimer;
        this.p1Name = p1name;
        this.p2Name = p2name;

        if (this.turnCounter % 2 == 1) {
            this.playerTurn = 2;
        } else {
            this.playerTurn = 1;
        }
        return true;
    }

    // Refactored by Jesse
    // Previously copied the string ~80 times and wrote to the file ~80 times...
    public String saveFile() {
        StringBuilder str = new StringBuilder();
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                str.append("" + this.currentBoard.getBoardArray()[i][j]);
                str.append(",");
            }
        }

        str.append("" + this.turnCounter);
        str.append(",");
        str.append(this.moveTimer);
        str.append(",");
        str.append(this.p1Name);
        str.append(",");
        str.append(this.p2Name);
        String s = str.toString();

        return s;
    }

    // Getters & Setters

    public int getTurnCounter() {
        return this.turnCounter;
    }

    public String getP1Name() {
        return this.p1Name;
    }

    public String getP2Name() {
        return this.p2Name;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public int getNumMoves() {
        return numMoves;
    }

    public int getTurnTimer() {
        return moveTimer;
    }

    public void setTurnTimer(int time) {
        this.moveTimer = time;
    }

    /**
     * @return the winner: 0 is nobody, 1 is player1, 2 is player2
     */
    public int getWinner() {
        return winner;
    }

    /**
     * @return the playerTurn
     */
    public int getPlayerTurn() {
        return playerTurn;
    }

    /**
     * @param playerTurn the playerTurn to set
     */
    public void setPlayerTurn(int playerTurn) {
        this.playerTurn = playerTurn;
    }

}

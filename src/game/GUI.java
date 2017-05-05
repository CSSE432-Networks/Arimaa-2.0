package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

public class GUI {
    private String p1Name;
    private String p2Name;
    private int playerCurrentlyPlacingPieces;
    private char pieceToBePlaced;
    private ArrayList<JFrame> activeFrames;
    private Game game;
    private ImagePanel gameBoardPanel = null;
    private ImagePanel[][] boardPieces;
    private JTextField p1TextField;
    private JTextField p2TextField;
    private JTextField ipAddressTextField;
    private JComboBox<Integer> timerComboBox;
    private JComboBox<String> gameModeNetworkComboBox;
    private JLabel moveCountLabel;
    private JLabel turnCountLabel;
    private JLabel turnIndicatorLabel;
    private JLabel timerLabel;
    private TimePanel timePanel;
    private static final int PORT = 9001;
    private Socket communicationSocket = null;
    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    // These used to be directly in the code. Refactored and pulled them out -
    // Jesse
    private final String WHITE_ELEPHANT_PIC_LOCATION = "resources/White elephant.png";
    private final String WHITE_CAMEL_PIC_LOCATION = "resources/White camel.png";
    private final String WHITE_HORSE_PIC_LOCATION = "resources/White horse.png";
    private final String WHITE_DOG_PIC_LOCATION = "resources/White dog.png";
    private final String WHITE_CAT_PIC_LOCATION = "resources/White cat.png";
    private final String WHITE_RABBIT_PIC_LOCATION = "resources/White rabbit.png";
    private final String BLACK_ELEPHANT_PIC_LOCATION = "resources/Black elephant.png";
    private final String BLACK_CAMEL_PIC_LOCATION = "resources/Black camel.png";
    private final String BLACK_HORSE_PIC_LOCATION = "resources/Black horse.png";
    private final String BLACK_DOG_PIC_LOCATION = "resources/Black dog.png";
    private final String BLACK_CAT_PIC_LOCATION = "resources/Black cat.png";
    private final String BLACK_RABBIT_PIC_LOCATION = "resources/Black rabbit.png";
    private final String BOARD_BACKGROUND = "resources/board.jpg";
    private final String NEW_GAME_SETTINGS_BACKGROUND = "resources/BoardStoneBigCropped.jpg";
    private static final String INITIAL_WINDOW_BACKGROUND = "resources/BoardStoneBig.jpg";
    private final String WINNER_BACKGROUND = "resources/BoardStoneBigCropped.jpg";

    public GUI() {
        this.p1Name = "Player 1";
        this.p2Name = "Player 2";
        p2TextField = null;
        p1TextField = null;
        this.game = new Game();
        this.boardPieces = new ImagePanel[8][8];
        this.activeFrames = new ArrayList<JFrame>();
        JFrame mainMenuFrame = new JFrame();
        this.activeFrames.add(mainMenuFrame);
        mainMenuFrame.setTitle("Welcome to Arimaa!");
        mainMenuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainMenuFrame.setResizable(false);
    }

    public static void main(String[] args) {
        GUI g = new GUI();

        // Add MAIN MENU panel with appropriate background image
        ImagePanel panel = new ImagePanel(INITIAL_WINDOW_BACKGROUND);
        g.activeFrames.get(0).getContentPane().add(panel);
        g.activeFrames.get(0).pack();
        panel.setVisible(true);

        // Add the NEW GAME button to the Main Menu]
        JButton newGameButton = g.createButton("New Game", 4, 20, 150, 75, (panel.getWidth() / 4) - 35,
                (panel.getHeight() / 2) - 37, g.new NewGameListener());
        panel.add(newGameButton);

        // Add the LOAD GAME button to the Main Menu
        JButton loadGameButton = g.createButton("Load Game", 4, 20, 150, 75, (panel.getWidth() / 4) * 3 - 110,
                (panel.getHeight() / 2) - 37, g.new LoadGameListener());
        panel.add(loadGameButton);

        g.activeFrames.get(0).setVisible(true);
    }

    public String getP1name() {
        return p1Name;
    }

    public void setP1name(String p1name) {
        this.p1Name = p1name;
    }

    public String getP2name() {
        return p2Name;
    }

    public void setP2name(String p2name) {
        this.p2Name = p2name;
    }

    public ArrayList<JFrame> getActiveFrames() {
        return activeFrames;
    }

    public void setActiveFrames(ArrayList<JFrame> frames) {
        this.activeFrames = frames;
    }

    // refactored this to clean up huge switch statement - Jesse
    private void renderInitialBoard() {
        if (game.getWinner() != 0)
            createWinWindow();
        char[][] boardArray = this.game.currentBoard.getBoardArray();
        for (int row = 0; row < 8; row++) {
            for (int column = 0; column < 8; column++) {
                char c = boardArray[row][column];
                switch (c) {
                    case 'E':
                        createPieceIcon(row, column, WHITE_ELEPHANT_PIC_LOCATION);
                        break;
                    case 'C':
                        createPieceIcon(row, column, WHITE_CAMEL_PIC_LOCATION);
                        break;
                    case 'H':
                        createPieceIcon(row, column, WHITE_HORSE_PIC_LOCATION);
                        break;
                    case 'D':
                        createPieceIcon(row, column, WHITE_DOG_PIC_LOCATION);
                        break;
                    case 'K':
                        createPieceIcon(row, column, WHITE_CAT_PIC_LOCATION);
                        break;
                    case 'R':
                        createPieceIcon(row, column, WHITE_RABBIT_PIC_LOCATION);
                        break;
                    case 'e':
                        createPieceIcon(row, column, BLACK_ELEPHANT_PIC_LOCATION);
                        break;
                    case 'c':
                        createPieceIcon(row, column, BLACK_CAMEL_PIC_LOCATION);
                        break;
                    case 'h':
                        createPieceIcon(row, column, BLACK_HORSE_PIC_LOCATION);
                        break;
                    case 'd':
                        createPieceIcon(row, column, BLACK_DOG_PIC_LOCATION);
                        break;
                    case 'k':
                        createPieceIcon(row, column, BLACK_CAT_PIC_LOCATION);
                        break;
                    case 'r':
                        createPieceIcon(row, column, BLACK_RABBIT_PIC_LOCATION);
                        break;
                    default:
                }
            }
        }
    }

    private void createPieceIcon(int row, int column, String imageLocation) {
        ImagePanel whiteElephantPanel = new ImagePanel(imageLocation);
        this.gameBoardPanel.add(whiteElephantPanel);
        whiteElephantPanel.setRow(row);
        whiteElephantPanel.setColumn(column);
        whiteElephantPanel.setLocation(whiteElephantPanel.getPixelX(), whiteElephantPanel.getPixelY());
        whiteElephantPanel.setVisible(true);
        this.boardPieces[row][column] = whiteElephantPanel;
    }

    protected void renderBoard() {
        for (int i = 0; i < 8; i++) {
            for (int k = 0; k < 8; k++) {
                if (boardPieces[i][k] != null) {
                    this.gameBoardPanel.remove(this.boardPieces[i][k]);
                }
                this.boardPieces[i][k] = null;
            }
        }
        moveCountLabel.setText("<html> <b>" + "Moves Left: \n" + game.getNumMoves() + "</b></html>");
        turnCountLabel.setText("<html> <b>" + "Turn: " + game.getTurnCounter() + "</b></html>");
        if (game.getPlayerTurn() == 1) {
            turnIndicatorLabel.setText("<html> <b>" + game.getP1Name() + "'s turn" + "</b></html>");
        } else {
            turnIndicatorLabel.setText("<html> <b>" + game.getP2Name() + "'s turn" + "</b></html>");
        }
        renderInitialBoard();
    }

    public void createWinWindow() {
        String playerName = "";
        if (this.game.getWinner() == 1)
            playerName = game.getP1Name();
        else if (this.game.getWinner() == 2)
            playerName = game.getP2Name();

        JFrame winnerFrame = createFrame("Winner", 650 / 2 - 324 / 2 + 75, 650 / 2 - 324 / 2 + 0);
        ImagePanel panel = new ImagePanel(WINNER_BACKGROUND);
        winnerFrame.getContentPane().add(panel);
        winnerFrame.pack();
        // winnerFrame.setResizable(false);
        panel.setVisible(true);

        // Set Up winner name Label
        JLabel winnerLabel = createLabel(
                "<html> <div style=\"text-align: center;\"> <b>" + playerName + " Wins!" + "</b></html>", Color.WHITE,
                24, 150, 150, winnerFrame.getWidth() / 2 - 75, winnerFrame.getHeight() / 2 - 87);
        panel.add(winnerLabel);
        panel.repaint();
    }

    // Method extracted from duplicate code in StartGameListener and
    // LoadGameListener. yay!
    public void setupForGame() {
        JFrame mainMenu = activeFrames.get(0);
        activeFrames.remove(0);
        mainMenu.dispose();

        JFrame gameFrame = new JFrame();
        activeFrames.add(gameFrame);
        gameFrame.setTitle("Let's Play!");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        ImagePanel panel = new ImagePanel(BOARD_BACKGROUND);
        activeFrames.get(0).getContentPane().add(panel);
        activeFrames.get(0).pack();
        panel.setVisible(true);
        gameBoardPanel = panel;

        gameBoardPanel.addMouseListener(new MovementListener());
        activeFrames.get(0).setBackground(Color.BLACK);

        gameFrame.setVisible(true);

        // Set Up Player1 Label
        JLabel player1Label = createLabel("<html> <b>Player 1: </b></html>", Color.BLACK, 22, 110, 25, 675, 25);
        gameBoardPanel.add(player1Label);

        // Set Up Player1 name Label
        JLabel player1NameLabel = createLabel("<html> <b>" + game.p1Name + "</b></html>", Color.BLACK, 18, 110, 100,
                675, 25);
        gameBoardPanel.add(player1NameLabel);

        // Set Up Player2 Label
        JLabel player2Label = createLabel("<html> <b>Player 2: </b></html>", Color.BLACK, 22, 110, 25, 675, 550);
        gameBoardPanel.add(player2Label);

        // Set Up Player2 name Label
        JLabel player2NameLabel = createLabel("<html> <b>" + game.p2Name + "</b></html>", Color.BLACK, 18, 110, 100,
                675, 550);
        gameBoardPanel.add(player2NameLabel);

        // Set up Turn Counter label
        turnCountLabel = createLabel("<html> <b>" + "Turn: " + game.getTurnCounter() + "</b></html>", Color.BLACK, 18,
                110, 25, 675, 130);
        gameBoardPanel.add(turnCountLabel);

        // Set up Player Turn label
        String playerTurnLabelText = "";
        if (game.getPlayerTurn() == 1) {
            playerTurnLabelText = "<html> <b>" + game.getP1Name() + "'s turn" + "</b></html>";
        } else {
            playerTurnLabelText = "<html> <b>" + game.getP2Name() + "'s turn" + "</b></html>";
        }
        turnIndicatorLabel = createLabel(playerTurnLabelText, Color.BLACK, 18, 110, 50, 675, 200);
        gameBoardPanel.add(turnIndicatorLabel);

        // Set up move counter label
        moveCountLabel = createLabel("<html> <b>" + "Moves Left: \n" + game.getNumMoves() + "</b></html>", Color.BLACK,
                18, 110, 50, 675, 370);
        gameBoardPanel.add(moveCountLabel);

        // Set up turn timer name label
        JLabel turnTimerLabel = createLabel("<html> <b>" + "Turn Time:" + "</b></html>", Color.BLACK, 18, 110, 25, 675,
                450);
        gameBoardPanel.add(turnTimerLabel);

        // Set up actual timer label
        timerLabel = createLabel("", Color.BLACK, 18, 110, 25, 675, 475);
        gameBoardPanel.add(timerLabel);

        // P1 Time Panel
        this.timePanel = new TimePanel(GUI.this, game, game.getTurnTimer(), timerLabel);

        // Set up Save Game Button
        JButton saveButton = createButton("Save", 1, 12, 65, 50, 657, gameFrame.getHeight() / 2 - 90,
                new SaveGameListener());
        gameBoardPanel.add(saveButton);

        // Set up Undo Button
        JButton undoButton = createButton("Undo", 1, 12, 65, 50, 727, gameFrame.getHeight() / 2 - 90,
                new UndoListener());
        gameBoardPanel.add(undoButton);

        // Set up End Turn Button
        JButton endTurnButton = createButton("End Turn", 1, 12, 137, 50, 655, gameFrame.getHeight() / 2 - 37,
                new EndTurnListener());
        gameBoardPanel.add(endTurnButton);

        renderBoard();
    }

    // Written for the addition of our new feature - Jesse
    private void setupPiecePlacingWindow() {
        // set up internal stuff
        this.playerCurrentlyPlacingPieces = 1;
        this.pieceToBePlaced = '!';

        // Construct a frame to display piece placing mechanics.
        JFrame piecePlacingFrame = createFrame("Piece Placing Controls", 900, 325);
        piecePlacingFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        piecePlacingFrame.setResizable(false);

        // Construct a panel for piece placing shenanigans <--- totally spelled
        // right, eclipse is silly
        ImagePanel piecePanel = new ImagePanel(WINNER_BACKGROUND);
        activeFrames.get(1).getContentPane().add(piecePanel);

        piecePanel.setVisible(true);

        // Set up Place Rabbit Button
        JButton placeRabbitButton = createButton("Place Rabbit", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 - 150, new PlacePieceListener("r"));
        piecePanel.add(placeRabbitButton);
        placeRabbitButton.setVisible(true);

        // Set up Place Cat Button
        JButton placeCatButton = createButton("Place Cat", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 - 115, new PlacePieceListener("k"));
        piecePanel.add(placeCatButton);
        placeCatButton.setVisible(true);

        // Set up Place Dog Button
        JButton placeDogButton = createButton("Place Dog", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 - 80, new PlacePieceListener("d"));
        piecePanel.add(placeDogButton);
        placeDogButton.setVisible(true);

        // Set up Place Horse Button
        JButton placeHorseButton = createButton("Place Horse", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 - 45, new PlacePieceListener("h"));
        piecePanel.add(placeHorseButton);
        placeHorseButton.setVisible(true);

        // Set up Place Camel Button
        JButton placeCamelButton = createButton("Place Camel", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 - 10, new PlacePieceListener("c"));
        piecePanel.add(placeCamelButton);
        placeCamelButton.setVisible(true);

        // Set up Place Elephant Button
        JButton placeElephantButton = createButton("Place Elephant", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 + 25, new PlacePieceListener("e"));
        piecePanel.add(placeElephantButton);
        placeElephantButton.setVisible(true);

        // Set up Remove Button
        JButton removeButton = createButton("Remove", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 + 60, new PlacePieceListener(" "));
        piecePanel.add(removeButton);
        removeButton.setVisible(true);

        // Set up Switch Player Button
        JButton switchPlayerButton = createButton("Switch Player", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 + 95, new SwitchPlayerListener());
        piecePanel.add(switchPlayerButton);
        switchPlayerButton.setVisible(true);

        // Set up Done Button
        JButton doneButton = createButton("Done", 1, 12, 125, 25, piecePanel.getWidth() / 2 - 62,
                piecePanel.getHeight() / 2 + 130, new FinishPiecePlacementListener());
        piecePanel.add(doneButton);
        doneButton.setVisible(true);

        piecePlacingFrame.pack();
    }

    // Label & Button create for convenience in Action Listeners
    // DOCME: Long Parameter List bad smell, decided against making a parameter
    // object, since this code is unlikely to change much in the futures
    public JLabel createLabel(String text, Color fontColor, int fontSize, int width, int height, int x, int y) {
        JLabel label = new JLabel();
        label.setText(text);
        label.setForeground(fontColor);
        Font labelFont = label.getFont();
        label.setFont(new Font(labelFont.getName(), 4, fontSize));
        label.setSize(width, height);
        label.setLocation(x, y);
        label.setVisible(true);
        return label;
    }

    public JButton createButton(String text, int fontStyle, int fontSize, int width, int height, int x, int y,
                                ActionListener listener) {
        JButton button = new JButton();
        button.setSize(width, height);
        button.setText(text);
        Font buttonFont = button.getFont();
        button.setFont(new Font(buttonFont.getName(), fontStyle, fontSize));
        button.setLocation(x, y);
        button.addActionListener(listener);
        button.setVisible(true);
        return button;

    }

    public JFrame createFrame(String text, int x, int y) {
        JFrame frame = new JFrame();
        activeFrames.add(frame);
        frame.setTitle(text);
        frame.setLocation(x, y);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        return frame;
    }

    public JTextField createField(int width, int height, int x, int y) {
        JTextField field = new JTextField();
        field.setSize(width, height);
        field.setLocation(x, y);
        field.setVisible(true);
        return field;
    }

    // ACTION LISTENERS
    private class NewGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame settingsFrame = createFrame("New Game Options", 650 / 2 - 324 / 2 + 5, 650 / 2 - 324 / 2 + 44);
            ImagePanel panel = new ImagePanel(NEW_GAME_SETTINGS_BACKGROUND);
            settingsFrame.getContentPane().add(panel);
            settingsFrame.pack();
            panel.setVisible(true);

            // Set up Game Mode Label and Text Field
            JLabel gameModeLabel = createLabel("<html><b>Game Mode:</b></html>", Color.WHITE, 14, 110, 25,
                    panel.getWidth() / 2 - 110, panel.getHeight() / 2 - 25 * 4);
            panel.add(gameModeLabel);

            String[] gameModePresets = {"Local", "Online"};
            JComboBox<String> gameModeComboBox = new JComboBox<String>(gameModePresets);
            gameModeNetworkComboBox = gameModeComboBox;
            gameModeComboBox.setEditable(false);
            gameModeComboBox.setSize(110, 25);
            panel.add(gameModeComboBox);
            gameModeComboBox.setLocation(panel.getWidth() / 2, panel.getHeight() / 2 - 25 * 4);
            gameModeComboBox.setVisible(true);

            // Set up IP Address Name Label and Text Field
            JLabel ipAddressLabel = createLabel("<html><b>IP Address:</b><html>", Color.WHITE, 14, 110, 25,
                    panel.getWidth() / 2 - 110, panel.getHeight() / 2 - 25 * 3);
            panel.add(ipAddressLabel);

            JTextField ipAddressField = createField(110, 25, panel.getWidth() / 2, panel.getHeight() / 2 - 25 * 3);
            ipAddressLabel.setFont(new Font(ipAddressField.getFont().getName(), 4, 14));
            panel.add(ipAddressField);
            ipAddressTextField = ipAddressField;

            // Set up Player 1 Name Label and Text Field
            JLabel p1NameLabel = createLabel("<html><b>Player 1 Name:</b><html>", Color.WHITE, 14, 110, 25,
                    panel.getWidth() / 2 - 110, panel.getHeight() / 2 - 25 * 2);
            panel.add(p1NameLabel);

            JTextField p1NameField = createField(110, 25, panel.getWidth() / 2, panel.getHeight() / 2 - 25 * 2);
            p1NameLabel.setFont(new Font(p1NameField.getFont().getName(), 4, 14));
            panel.add(p1NameField);
            p1TextField = p1NameField;

            // Set up Player 2 Name Label and Text Field
            JLabel p2NameLabel = createLabel("<html><b>Player 2 Name:</b></hmtl>", Color.WHITE, 14, 110, 25,
                    panel.getWidth() / 2 - 110, panel.getHeight() / 2 - 25);
            panel.add(p2NameLabel);

            JTextField p2NameField = createField(110, 25, panel.getWidth() / 2, panel.getHeight() / 2 - 25);
            p2NameLabel.setFont(new Font(p2NameField.getFont().getName(), 4, 14));
            panel.add(p2NameField);
            p2TextField = p2NameField;

            // Set up Turn Timer Label and Text Field
            JLabel turnTimerLabel = createLabel("<html><b>Turn Timer:</b></html>", Color.WHITE, 14, 110, 25,
                    panel.getWidth() / 2 - 110, panel.getHeight() / 2);
            panel.add(turnTimerLabel);

            Integer[] turnTimerPresets = {30, 45, 60, 75, 90, 105, 120, 135, 150, 165, 180};
            JComboBox<Integer> turnTimerComboBox = new JComboBox<Integer>(turnTimerPresets);
            timerComboBox = turnTimerComboBox;
            turnTimerComboBox.setEditable(false);
            turnTimerComboBox.setSize(110, 25);
            panel.add(turnTimerComboBox);
            turnTimerComboBox.setLocation(panel.getWidth() / 2, panel.getHeight() / 2);
            turnTimerComboBox.setVisible(true);

            // Set up Start Game Button
            JButton startGameButton = createButton("Start Game", 1, 12, 110, 25, (panel.getWidth() / 2) - 110,
                    (panel.getHeight() / 2) + (2 * 25), new StartGameListener());
            panel.add(startGameButton);

            // Set up Cancel Button
            JButton cancelButton = createButton("Cancel", 1, 12, 110, 25, (panel.getWidth() / 2),
                    (panel.getHeight() / 2) + (2 * 25), new CancelListener());
            panel.add(cancelButton);
        }
    }

    public void setPlacementPiece(String character) {
        if (playerCurrentlyPlacingPieces == 1) {
            pieceToBePlaced = character.toUpperCase().charAt(0);
        } else if (playerCurrentlyPlacingPieces == 2) {
            pieceToBePlaced = character.charAt(0);
        }
    }

    private class PlacePieceListener implements ActionListener {

        String pieceChar;

        public PlacePieceListener(String pieceChar) {
            this.pieceChar = pieceChar;
        }

        @Override
        public void actionPerformed(ActionEvent arg0) {
            setPlacementPiece(pieceChar);
        }

    }

    private class SwitchPlayerListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if ((playerCurrentlyPlacingPieces == 1) && game.pieceInventoryEmpty(playerCurrentlyPlacingPieces)) {
                playerCurrentlyPlacingPieces = 2;
            }
        }
    }

    private class FinishPiecePlacementListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (game.pieceInventoryEmpty(1) && game.pieceInventoryEmpty(2)) {
                playerCurrentlyPlacingPieces = 0;
                activeFrames.get(1).dispose();
                timePanel.unpause();
            }
        }
    }

    private class LoadGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            int result = fileChooser.showOpenDialog(activeFrames.get(0));
            if (result == JFileChooser.APPROVE_OPTION) {
                File selectedFile = fileChooser.getSelectedFile();
                try {
                    loadGame(selectedFile);
                    p1Name = game.getP1Name();
                    p2Name = game.getP2Name();
                } catch (FileNotFoundException e1) {
                }
            }
        }

        private void loadGame(File file) throws FileNotFoundException {
            Scanner scanner = new Scanner(file);
            if (game.loadFile(scanner)) {
                setupForGame();
                timePanel.unpause();

            } else {
            }
        }
    }

    private class CancelListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame settingsFrame = activeFrames.get(1);
            activeFrames.remove(1);
            settingsFrame.dispose();
        }
    }

    private class StartGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            game.p1Name = p1TextField.getText();
            game.p2Name = p2TextField.getText();
            game.moveTimer = (int) timerComboBox.getSelectedItem();
            boolean networked = gameModeNetworkComboBox.getSelectedItem().equals("Online");
            String ip = ipAddressTextField.getText();

            if (game.p1Name.equals(""))
                game.p1Name = "Player 1";
            if (game.p2Name.equals(""))
                game.p2Name = "Player 2";

            JFrame settings = activeFrames.get(1);
            activeFrames.remove(1);
            settings.dispose();

            game.setTurnTimer((int) timerComboBox.getSelectedItem());
            if (networked) {
                boolean host = setupNetworkConnection(ip);
                if (host) {
                    setupForGame();
                    setupPiecePlacingWindow();
                } else {
                    setupForGame();
                    // TODO: skipping piece placement process for now
                    // wait for host to take first turn
                    try {
                        // should block
                        String boardState = bufferedReader.readLine();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            } else {
                // play locally
                setupForGame();
                setupPiecePlacingWindow();
            }
        }
    }

    private boolean setupNetworkConnection(String ip) {
        try {
            if (ip.equals("")) {
                // host
                // setup socket & IO
                ServerSocket sock = new ServerSocket(PORT);
                communicationSocket = sock.accept();
                bufferedReader = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
                printWriter = new PrintWriter(communicationSocket.getOutputStream(), true);

                //test
                String message = bufferedReader.readLine();
                System.out.println(message);

                // return true since host
                return true;
            } else {
                // not host
                // setup socket & IO
                communicationSocket = new Socket(ip, PORT);
                bufferedReader = new BufferedReader(new InputStreamReader(communicationSocket.getInputStream()));
                printWriter = new PrintWriter(communicationSocket.getOutputStream(), true);

                // test
                printWriter.println("Connection established");

                // return false since not host
                return false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
            // will not execute
            return false;
        }
    }


    private class SaveGameListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            File selectedFile = null;
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setCurrentDirectory(new File(System.getProperty("user.home")));
            fileChooser.setApproveButtonText("Save");
            timePanel.pause();
            int result = fileChooser.showOpenDialog(gameBoardPanel);
            if (result == JFileChooser.APPROVE_OPTION) {
                selectedFile = fileChooser.getSelectedFile();
                FileWriter fw = null;
                try {
                    fw = new FileWriter(selectedFile);
                    String saveString = game.saveFile();
                    fw.write(saveString);
                    fw.close();
                } catch (IOException e) {
                    // Shouldn't ever happen...?
                }
            }
            timePanel.unpause();
        }
    }

    private class UndoListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            game.undoMove();
            renderBoard();
        }
    }

    private class EndTurnListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            // handles internal turn switching logic
            game.endTurn();

            //pack & ship gamestate
            String boardstateToSend = game.saveFile();
            printWriter.write(boardstateToSend);
            printWriter.flush();

            // block while other player takes turn
            try {
                String boardstateReceived = bufferedReader.readLine();
                game.loadFileFromString(boardstateReceived);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // read in gamestate and display
            renderBoard();
        }
    }

    private class MovementListener implements MouseListener {
        ImagePanel selectedPiece;
        ImagePanel secondSelectedPiece;

        private MovementListener() {
            this.selectedPiece = null;
            this.secondSelectedPiece = null;
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            // Not needed
        }

        @Override
        public void mouseReleased(MouseEvent e) {
            // Not needed
        }

        @Override
        public void mouseEntered(MouseEvent e) {
            // Not needed
        }

        @Override
        public void mouseExited(MouseEvent e) {
            // Not needed
        }

        // Refactored to clarify method - Jesse
        @Override
        public void mousePressed(MouseEvent e) {
            int sourceX = (int) e.getPoint().getX();
            int sourceY = (int) e.getPoint().getY();

            // Get rid of X and Y ASAP!!!
            int rowClicked = (sourceY - 10) / 80;
            int columnClicked = (sourceX - 10) / 80;

            if (playerCurrentlyPlacingPieces != 0) {
                if (pieceToBePlaced == '!') {
                    return;
                } else if (pieceToBePlaced != ' ') {
                    if (playerCurrentlyPlacingPieces == 1 && rowClicked < 2)
                        game.placePiece(rowClicked, columnClicked, pieceToBePlaced);
                    if (playerCurrentlyPlacingPieces == 2 && rowClicked > 5)
                        game.placePiece(rowClicked, columnClicked, pieceToBePlaced);
                } else {
                    game.removePiece(rowClicked, columnClicked, playerCurrentlyPlacingPieces);
                }
                renderBoard();
                gameBoardPanel.repaint();
                pieceToBePlaced = '!';
                return;
            }

            // Beginning movement, nothing yet selected
            // Selecting piece to interact with
            if (clickOnBoard(rowClicked, columnClicked)) {

                // No piece has been selected yet
                if (noPieceSelectedAndPieceClicked(rowClicked, columnClicked)) {
                    this.selectedPiece = boardPieces[rowClicked][columnClicked];
                }

                // If a piece is selected and an empty space is clicked
                // AKA move
                else if (selectedPieceAndEmptySpaceClicked(rowClicked, columnClicked)) {
                    handleMove(rowClicked, columnClicked);
                }

                // Piece already selected, clicked a second piece
                else if (pieceSelectedAndSecondPieceClicked(rowClicked, columnClicked)) {
                    this.secondSelectedPiece = boardPieces[rowClicked][columnClicked];

                    // Piece selected, Second piece selected, empty square
                    // selected
                } else if (twoPieceSelectedAndEmptySpaceClicked(rowClicked, columnClicked)) {
                    if (checkForPull(rowClicked, columnClicked)) {
                        handlePull(rowClicked, columnClicked);

                    } else if (checkForPush(rowClicked, columnClicked)) {
                        handlePush(rowClicked, columnClicked);
                    }
                }

                // Invalid selection, clear data
                else {
                    this.selectedPiece = null;
                    this.secondSelectedPiece = null;
                }
            }
        }

        private void handlePush(int rowClicked, int columnClicked) {
            Integer calculatedDirection1 = null;
            Integer calculatedDirection2 = null;
            try {
                calculatedDirection1 = moveDirectionOnePush(selectedPiece, secondSelectedPiece);
                calculatedDirection2 = moveDirectionTwoPush(secondSelectedPiece, rowClicked, columnClicked);
            } catch (ArimaaException e1) {
                return;
            }

            if (calculatedDirection1 != null && calculatedDirection2 != null) {
                if (game.push(this.selectedPiece.getRow(), this.selectedPiece.getColumn(), calculatedDirection1,
                        calculatedDirection2)) {
                    renderBoard();
                }
                this.selectedPiece = null;
                this.secondSelectedPiece = null;
            }
        }

        private void handlePull(int rowClicked, int columnClicked) {
            Integer calculatedDirection = null;
            try {
                calculatedDirection = moveDirection(selectedPiece, rowClicked, columnClicked);
            } catch (ArimaaException e1) {
                e1.printStackTrace();
                System.err.println("Arimaa Exception: " + e1.getMessage());
            }

            if (calculatedDirection != null) {
                if (game.pull(this.selectedPiece.getRow(), this.selectedPiece.getColumn(),
                        this.secondSelectedPiece.getRow(), this.secondSelectedPiece.getColumn(), calculatedDirection)) {
                    renderBoard();
                }
                this.selectedPiece = null;
                this.secondSelectedPiece = null;
            }
        }

        private void handleMove(int rowClicked, int columnClicked) {
            Integer calculatedDirection = null;
            try {
                calculatedDirection = moveDirection(selectedPiece, rowClicked, columnClicked);
            } catch (ArimaaException e1) {
                e1.printStackTrace();
                System.err.println("Arimaa Exception: " + e1.getMessage());
            }
            // Using move to check for valid move
            if (calculatedDirection != null) {
                if (game.move(selectedPiece.getRow(), selectedPiece.getColumn(), calculatedDirection)) {
                    renderBoard();
                    game.isPushPull = false;
                }
                this.selectedPiece = null;
                this.secondSelectedPiece = null;
            }
        }

        private int moveDirectionTwoPush(ImagePanel secondSelectedPiece2, int rowClicked, int columnClicked)
                throws ArimaaException {

            if (secondSelectedPiece.getRow() - 1 == rowClicked && secondSelectedPiece.getColumn() == columnClicked)
                return 0;
            else if (secondSelectedPiece.getColumn() + 1 == columnClicked && secondSelectedPiece.getRow() == rowClicked)
                return 1;
            else if (secondSelectedPiece.getRow() + 1 == rowClicked && secondSelectedPiece.getColumn() == columnClicked)
                return 2;
            else if (secondSelectedPiece.getColumn() - 1 == columnClicked && secondSelectedPiece.getRow() == rowClicked)
                return 3;
            else {
                throw new ArimaaException("GUI.moveDirectionTwoPush(): Invalid push movement :(");
            }
        }

        private int moveDirectionOnePush(ImagePanel selectedPiece2, ImagePanel secondSelectedPiece2)
                throws ArimaaException {

            if (selectedPiece.getRow() - 1 == secondSelectedPiece.getRow()
                    && selectedPiece.getColumn() == secondSelectedPiece.getColumn())
                return 0;
            else if (selectedPiece.getColumn() + 1 == secondSelectedPiece.getColumn()
                    && selectedPiece.getRow() == secondSelectedPiece.getRow())
                return 1;
            else if (selectedPiece.getRow() + 1 == secondSelectedPiece.getRow()
                    && selectedPiece.getColumn() == secondSelectedPiece.getColumn())
                return 2;
            else if (selectedPiece.getColumn() - 1 == secondSelectedPiece.getColumn()
                    && selectedPiece.getRow() == secondSelectedPiece.getRow())
                return 3;
            else {

                throw new ArimaaException("GUI.moveDirectionOnePush(): Invalid push movement :(");
            }
        }

        private boolean twoPieceSelectedAndEmptySpaceClicked(int rowClicked, int columnClicked) {
            return this.selectedPiece != null && this.secondSelectedPiece != null
                    && boardPieces[rowClicked][columnClicked] == null;
        }

        private boolean pieceSelectedAndSecondPieceClicked(int rowClicked, int columnClicked) {
            return this.selectedPiece != null && this.secondSelectedPiece == null
                    && boardPieces[rowClicked][columnClicked] != null
                    && this.selectedPiece != boardPieces[rowClicked][columnClicked];
        }

        private int moveDirection(ImagePanel selectedPiece2, int rowClicked, int columnClicked) throws ArimaaException {

            if (selectedPiece.getRow() - 1 == rowClicked && selectedPiece.getColumn() == columnClicked)
                return 0;
            else if (selectedPiece.getColumn() + 1 == columnClicked && selectedPiece.getRow() == rowClicked)
                return 1;
            else if (selectedPiece.getRow() + 1 == rowClicked && selectedPiece.getColumn() == columnClicked)
                return 2;
            else if (selectedPiece.getColumn() - 1 == columnClicked && selectedPiece.getRow() == rowClicked)
                return 3;
            else {
                return -1;
                // Removed
                // throw new ArimaaException("GUI.moveDirection(): Invalid
                // direction :(");
            }
        }

        private boolean selectedPieceAndEmptySpaceClicked(int rowClicked, int columnClicked) {
            return this.selectedPiece != null && this.secondSelectedPiece == null
                    && boardPieces[rowClicked][columnClicked] == null;
        }

        private boolean noPieceSelectedAndPieceClicked(int rowClicked, int columnClicked) {
            return boardPieces[rowClicked][columnClicked] != null && this.selectedPiece == null
                    && this.secondSelectedPiece == null;
        }

        private boolean clickOnBoard(int rowClicked, int columnClicked) {
            return rowClicked <= 7 && rowClicked >= 0 && columnClicked <= 7 && columnClicked >= 0;
        }

        private boolean checkForPush(int rowClicked, int columnClicked) {
            if (!game.isValidMoveSquare(rowClicked, columnClicked))
                return false;
            boolean secondSelectedPieceCanMove = false;
            if (this.secondSelectedPiece.getRow() + 1 == rowClicked
                    && this.secondSelectedPiece.getColumn() == columnClicked) {
                secondSelectedPieceCanMove = true;
            } else if (this.secondSelectedPiece.getRow() - 1 == rowClicked
                    && this.secondSelectedPiece.getColumn() == columnClicked) {
                secondSelectedPieceCanMove = true;
            } else if (this.secondSelectedPiece.getRow() == rowClicked
                    && this.secondSelectedPiece.getColumn() + 1 == columnClicked) {
                secondSelectedPieceCanMove = true;
            } else if (this.secondSelectedPiece.getRow() == rowClicked
                    && this.secondSelectedPiece.getColumn() - 1 == columnClicked) {
                secondSelectedPieceCanMove = true;
            }
            boolean selectedPieceCanMove = false;
            if (selectedPiece.getRow() + 1 == secondSelectedPiece.getRow() && selectedPiece.getColumn() == secondSelectedPiece.getColumn()) {
                selectedPieceCanMove = true;
            } else if (selectedPiece.getRow() - 1 == secondSelectedPiece.getRow() && selectedPiece.getColumn() == secondSelectedPiece.getColumn()) {
                selectedPieceCanMove = true;
            } else if (selectedPiece.getRow() == secondSelectedPiece.getRow() && selectedPiece.getColumn() + 1 == secondSelectedPiece.getColumn()) {
                selectedPieceCanMove = true;
            } else if (selectedPiece.getRow() == secondSelectedPiece.getRow() && selectedPiece.getColumn() - 1 == secondSelectedPiece.getColumn()) {
                selectedPieceCanMove = true;
            }
            return secondSelectedPieceCanMove && selectedPieceCanMove;
        }

        private boolean checkForPull(int rowClicked, int columnClicked) {
            if (!game.isValidMoveSquare(rowClicked, columnClicked))
                return false;
            boolean canSelectedPieceMove = false;
            if (this.selectedPiece.getRow() + 1 == rowClicked && this.selectedPiece.getColumn() == columnClicked) {
                canSelectedPieceMove = true;
            } else if (this.selectedPiece.getRow() - 1 == rowClicked
                    && this.selectedPiece.getColumn() == columnClicked) {
                canSelectedPieceMove = true;
            } else if (this.selectedPiece.getRow() == rowClicked
                    && this.selectedPiece.getColumn() + 1 == columnClicked) {
                canSelectedPieceMove = true;
            } else if (this.selectedPiece.getRow() == rowClicked
                    && this.selectedPiece.getColumn() - 1 == columnClicked) {
                canSelectedPieceMove = true;
            }
            boolean canSecondSelectedPieceMove = false;
            if (secondSelectedPiece.getRow() + 1 == selectedPiece.getRow()
                    && secondSelectedPiece.getColumn() == selectedPiece.getColumn()) {
                canSecondSelectedPieceMove = true;
            } else if (secondSelectedPiece.getRow() - 1 == selectedPiece.getRow()
                    && secondSelectedPiece.getColumn() == selectedPiece.getColumn()) {
                canSecondSelectedPieceMove = true;
            } else if (secondSelectedPiece.getRow() == selectedPiece.getRow()
                    && secondSelectedPiece.getColumn() + 1 == selectedPiece.getColumn()) {
                canSecondSelectedPieceMove = true;
            } else if (secondSelectedPiece.getRow() == selectedPiece.getRow()
                    && secondSelectedPiece.getColumn() - 1 == selectedPiece.getColumn()) {
                canSecondSelectedPieceMove = true;
            }
            return canSelectedPieceMove && canSecondSelectedPieceMove;
        }
    }
}
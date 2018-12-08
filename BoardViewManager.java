import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import java.util.List;

public class BoardViewManager {

  private static BoardViewManager instance = null;

  private Board gameBoard;
  private int totalPlayers;
  private Room[] roomArr;

  JFrame bFrame;
  JLayeredPane bPane;

  JLabel boardlabel;
  JLabel playerLabels[];
  JLabel mLabel;
  JLabel roomLabels[];
  JLabel trailerLabel;
  JLabel officeLabel;
  JLabel cardLabels[];

  JButton bAct;
  JButton bRehearse;
  JButton bMove;
  JButton bEnd;
  JButton bUpgrade;
  JButton bTakeRole;

  JTextArea bTextArea;
  JScrollPane bScrollPane;

  ImageIcon boardIcon;

  String playerImages[] = {"media/dice/b1.png", "media/dice/c1.png", "media/dice/g1.png", "media/dice/o1.png", "media/dice/p1.png", "media/dice/r1.png"};

  Player currPlayer;
  Room currRoom;
  boolean playerMoving = false;
  boolean playerMoved = false;
  boolean turnContinue = true;
  boolean takingRole = false;
  boolean playerTurn = false;

  private BoardViewManager() {

  }

  public static BoardViewManager getInstance() {
    if(instance == null) {
      instance = new BoardViewManager();
    }

    return instance;
  }

  public void initGUI(Board board, int players) {
    bFrame = new JFrame("Deadwood");
    bFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    totalPlayers = players;
    gameBoard = board;
    roomArr = gameBoard.getRooms();

    bPane = bFrame.getLayeredPane();

    initLabels();
    initPlayers();
    initMenu();
    initTextArea();
    //initRoomRoles();
    //initCards();

    bFrame.setVisible(true);
    println("Welcome to Deadwood!\n");
  }

  private void initLabels() {
    // Create labels for each room
    roomLabels = new JLabel[12];
    for(int i = 0; i < 10; i++) {
        roomLabels[i] = new JLabel();
        roomLabels[i].setBounds(roomArr[i].getXy()[0], roomArr[i].getXy()[1], roomArr[i].getHw()[1], roomArr[i].getHw()[0]);
        //roomLabels[i].setBackground(Color.white);
        roomLabels[i].addMouseListener(new boardMouseListener());
        roomLabels[i].setVisible(true);
        roomLabels[i].setOpaque(false);
        bPane.add(roomLabels[i], new Integer(2));
    }

    // Add trailer and office room labels
    Room trailerRoom = gameBoard.getRoomByName("trailer");
    trailerLabel = new JLabel();
    trailerLabel.setBounds(trailerRoom.getXy()[0], trailerRoom.getXy()[1], trailerRoom.getHw()[1], trailerRoom.getHw()[0]);
    trailerLabel.addMouseListener(new boardMouseListener());
    trailerLabel.setVisible(true);
    roomLabels[10] = trailerLabel;

    bPane.add(trailerLabel, new Integer(2));

    Room officeRoom = gameBoard.getRoomByName("office");
    officeLabel = new JLabel();
    officeLabel.setBounds(officeRoom.getXy()[0], officeRoom.getXy()[1], officeRoom.getHw()[1], officeRoom.getHw()[0]);
    officeLabel.addMouseListener(new boardMouseListener());
    officeLabel.setVisible(true);
    roomLabels[11] = officeLabel;

    bPane.add(officeLabel, new Integer(2));

    // Create the deadwood board
    boardlabel = new JLabel();
    boardIcon =  new ImageIcon("media/board.jpg");
    boardlabel.setIcon(boardIcon);
    boardlabel.setBounds(0,0,boardIcon.getIconWidth(),boardIcon.getIconHeight());

    // Add the board to the lowest layer
    bPane.add(boardlabel, new Integer(0));

    // Set the size of the GUI
    bFrame.setSize(boardIcon.getIconWidth()+200,boardIcon.getIconHeight());
  }

  private void initPlayers() {
    playerLabels = new JLabel[6];
    Room trailerRoom = gameBoard.getRoomByName("trailer");

    // Add a dice to represent a player.
    for (int i = 0; i < totalPlayers; i++) {
        playerLabels[i] = new JLabel();
        playerLabels[i].setIcon(new ImageIcon(playerImages[i]));

        playerLabels[i].setBounds(trailerRoom.getXy()[0] + 50*i, trailerRoom.getXy()[1],46,46);
        playerLabels[i].setVisible(true);
        bPane.add(playerLabels[i],new Integer(3));
    }
  }

  private void initMenu() {
    // Create the Menu for action buttons
    mLabel = new JLabel("MENU");
    mLabel.setBounds(boardIcon.getIconWidth()+80,0,150,20);
    bPane.add(mLabel,new Integer(2));

    // Create Action buttons
    bAct = new JButton("ACT");
    bAct.setBackground(Color.white);
    bAct.setBounds(boardIcon.getIconWidth()+25, 30,150, 20);
    bAct.addMouseListener(new boardMouseListener());

    bRehearse = new JButton("REHEARSE");
    bRehearse.setBackground(Color.white);
    bRehearse.setBounds(boardIcon.getIconWidth()+25,60,150, 20);
    bRehearse.addMouseListener(new boardMouseListener());

    bMove = new JButton("MOVE");
    bMove.setBackground(Color.white);
    bMove.setBounds(boardIcon.getIconWidth()+25,90,150, 20);
    bMove.addMouseListener(new boardMouseListener());

    bEnd = new JButton("END");
    bEnd.setBackground(Color.white);
    bEnd.setBounds(boardIcon.getIconWidth()+25,120,150,20);
    bEnd.addMouseListener(new boardMouseListener());

    bTakeRole = new JButton("TAKE ROLE");
    bTakeRole.setBackground(Color.white);
    bTakeRole.setBounds(boardIcon.getIconWidth()+25,150,150,20);
    bTakeRole.addMouseListener(new boardMouseListener());

    bUpgrade = new JButton("UPGRADE");
    bUpgrade.setBackground(Color.white);
    bUpgrade.setBounds(boardIcon.getIconWidth()+25,180,150,20);
    bUpgrade.addMouseListener(new boardMouseListener());

    // Place the action buttons in the top layer
    bPane.add(bAct, new Integer(2));
    bPane.add(bRehearse, new Integer(2));
    bPane.add(bMove, new Integer(2));
    bPane.add(bEnd, new Integer(2));
    bPane.add(bUpgrade, new Integer(2));
    bPane.add(bTakeRole, new Integer(2));
  }

  private void initCards() {
    cardLabels = new JLabel[10];

    ImageIcon cIcon =  new ImageIcon("media/cards/CardBack.jpg");

    for(int i = 0; i < 10; i++) {
      cardLabels[i] = new JLabel();
      cardLabels[i].setIcon(cIcon);
      cardLabels[i].setBounds(roomArr[i].getXy()[0], roomArr[i].getXy()[1], roomArr[i].getHw()[1], roomArr[i].getHw()[0]);
      cardLabels[i].setOpaque(true);
      bPane.add(cardLabels[i], new Integer(1));
    }
  }

/*
  private void initRoomRoles(Room currRoom) {
	  List<Role> offCardRoles = currRoom.getOffCardRoles();
	  JLabel offCardLabels[] = new JLabel[offCardRoles.size()];

	  List<Role> sceneRoles = currRoom.getSceneRoles();
	  JLabel sceneLabels[] = new JLabel[sceneRoles.size()];

	  for(int i = 0; i < offCardRoles.size(); i++) {
		  Role curr = offCardRoles.get(i);
		  offCardLabels[i] = new JLabel();
		  offCardLabels[i].setBounds(curr.getXy()[0], curr.getXy()[1], curr.getHw()[1], curr.getHw()[0]);
		  offCardLabels[i].setOpaque(true);
		  bPane.add(offCardLabels[i], new Integer(1));
	  }

	  for(int i = 0; i < sceneRoles.size(); i++) {
		  Role curr = sceneRoles.get(i);
		  sceneLabels[i] = new JLabel();
		  sceneLabels[i].setBounds(curr.getXy()[0], curr.getXy()[1], curr.getHw()[1], curr.getHw()[0]);
		  sceneLabels[i].setOpaque(true);
		  bPane.add(sceneLabels[i], new Integer(1));
	  }
  }
  */

  private void initTextArea() {
    bTextArea = new JTextArea(5, 20);
    bTextArea.setEditable(false);
    bTextArea.setLineWrap(true);
    bTextArea.setWrapStyleWord(true);
    bTextArea.setCaretPosition(bTextArea.getDocument().getLength());

    bScrollPane = new JScrollPane(bTextArea);
    bScrollPane.setBounds(boardIcon.getIconWidth() + 10, boardIcon.getIconHeight() - 500, 180, 500);
    bPane.add(bScrollPane, new Integer(2));
  }

  public void println(String text) {
    bTextArea.append(text + "\n");
    bTextArea.setCaretPosition(bTextArea.getDocument().getLength());
  }

  public void print(String text) {
    bTextArea.append(text);
    bTextArea.setCaretPosition(bTextArea.getDocument().getLength());
  }

  public String[] getPlayerNames() {
    String[] playerNames = new String[totalPlayers];

    for(int i = 0; i < totalPlayers; i++) {
      String temp = JOptionPane.showInputDialog(null, "Enter player name");
      if(temp != null && temp.length() > 0) {
        playerNames[i] = temp;
        print(String.format("Player %d: %s\n", i + 1, temp));
      }
      else {
        i--;
      }
    }

    return playerNames;
  }

  public void movePlayersTrailer() {
    List<Player> players = gameBoard.getPlayers();

    for(int i = 0; i < totalPlayers; i++) {
      Player currPlayer = players.get(i);
      movePlayerRoom(currPlayer.getRoom(), "trailer", i);
    }
  }

  private void movePlayerRoom(Room currRoom, String targetRoomName, int playerIndex) {
    int roomIndex = gameBoard.getRoomIndexByName(targetRoomName);

    // Read just other players in curr room
    List<Player> otherPlayers = gameBoard.getPlayersInRoom(currRoom);
    for(int j = 0; j < otherPlayers.size(); j++)
    {
      Rectangle loc = new Rectangle(
              currRoom.getXy()[0] + j * 50,
              currRoom.getXy()[1],
              46, 46);
      playerLabels[gameBoard.getPlayerIndexByName(otherPlayers.get(j).getName())].setBounds(loc);
    }

    // Adjust location for other players in the set
    int xOffset = gameBoard.getPlayersInRoom(roomArr[roomIndex]).size() - 1;
    Rectangle loc = new Rectangle(
      gameBoard.getRoomByName(targetRoomName).getXy()[0] + xOffset * 50,
      gameBoard.getRoomByName(targetRoomName).getXy()[1],
      46, 46);
    playerLabels[playerIndex].setBounds(loc);
  }

  private void turnEnd() {
    if(!gameBoard.checkDayCont()) {
      gameBoard.newDay();
      gameBoard.checkGameEnd();
    }
    else {
      gameBoard.nextTurn();
    }
  }

  class boardMouseListener implements MouseListener {
    List<String> neighbors;

    public void mouseClicked(MouseEvent e) {
      neighbors = gameBoard.getCurrentPlayer().getRoom().getNeighbors();
      if(playerMoving) {
        for(int j = 0; j < roomLabels.length; j++) {
          roomLabels[j].setIcon(null);
        }
      }
      if(gameBoard.isActiveGame()) {
        currPlayer = gameBoard.getCurrentPlayer();
        currRoom = currPlayer.getRoom();

        if(e.getSource() == bAct) {
          turnContinue = gameBoard.playerAct(currPlayer);
        }
        else if(e.getSource() == bRehearse) {
          turnContinue = gameBoard.playerRehearse(currPlayer);
        }
        else if(e.getSource() == bMove) {
          if(!playerMoved) {
            playerMoving = true;
            println("Select a room to move to");

            // Highlight available rooms
            for(int j = 0; j < neighbors.size(); j++) {
              int ind = gameBoard.getRoomIndexByName(neighbors.get(j));
              roomLabels[ind].setIcon(new ImageIcon("media/cards/OpenCard.png"));
            }

          }
          else {
            println("Player has already moved this turn");
          }
        }
        else if(e.getSource() == bEnd) {
          turnContinue = false;
        }
        else if(e.getSource() == bUpgrade) {
          if(currRoom.getName().equals("office")) {
            int targetRank = Integer.parseInt(JOptionPane.showInputDialog(null, "What rank? (2-6)"));
            int moneyType = Integer.parseInt(JOptionPane.showInputDialog(null, "What currency? (0: dollar, 1: credit)"));

            gameBoard.playerUpgrade(currPlayer, targetRank, moneyType);
          }
          else {
            println("Player is not at the Casting Office");
          }
        }
        else if(e.getSource() == bTakeRole) {
          takingRole = true;
          println("Select a role to take");
        }
        else if(takingRole) {
          takingRole = false;
        }
        else if(playerMoving) {
          List<String> neighbors = currRoom.getNeighbors();
          for(int i = 0; i < neighbors.size(); i++) {

            int roomIndex = gameBoard.getRoomIndexByName(neighbors.get(i));
            JLabel targetRoomLabel = null;
            String targetRoomName = "";

            if(neighbors.get(i).equals("office")) {
              targetRoomLabel = officeLabel;
              targetRoomName = "office";
            }
            else if(neighbors.get(i).equals("trailer")) {
              targetRoomLabel = trailerLabel;
              targetRoomName = "trailer";
            }
            else {
              targetRoomLabel = roomLabels[roomIndex];
              targetRoomName = neighbors.get(i);
            }

            if(e.getSource() == targetRoomLabel) {
              playerMoving = false;
              playerMoved = gameBoard.movePlayer(currPlayer, targetRoomName);

              if(playerMoved) {
                movePlayerRoom(currRoom, targetRoomName, gameBoard.getCurrentPlayerIndex());
              }
            }
          }
        }

        if(!turnContinue) {
          turnEnd();
          playerMoving = false;
          playerMoved = false;
          takingRole = false;
          turnContinue = true;
        }
      }
    }

    public void mousePressed(MouseEvent e) { }

    public void mouseReleased(MouseEvent e) { }

    public void mouseEntered(MouseEvent e) { }

    public void mouseExited(MouseEvent e) { }
  }
}

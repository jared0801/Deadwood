import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import java.util.List;

public class BoardViewManager {

  private static BoardViewManager instance = null;

  // references to game attributes
  private Board gameBoard;
  private int totalPlayers;
  private Room[] roomArr;
  private Player currPlayer;
  private Room currRoom;

  // top layer containers
  JFrame bFrame;
  JLayeredPane bPane;

  // labels for reference
  JLabel boardlabel;
  JLabel playerLabels[];
  JLabel mLabel;
  JLabel roomLabels[];
  JLabel takeLabels[][];
  JLabel roleLabels[][];
  JLabel sceneRoleLabels[][];
  JLabel trailerLabel;
  JLabel officeLabel;
  JLabel sceneLabels[];

  // info panel objects
  JLabel dayLabel;
  JTextArea infoTextArea;
  JLabel currPlayerLabel;

  // menu buttons
  JButton bAct;
  JButton bRehearse;
  JButton bMove;
  JButton bEnd;
  JButton bUpgrade;
  JButton bTakeRole;

  // console box
  JTextArea bTextArea;
  JScrollPane bScrollPane;

  // deadwood image
  ImageIcon boardIcon;

  // player image prefixes
  String playerImages[] = {"media/dice/b", "media/dice/c", "media/dice/g", "media/dice/o", "media/dice/p", "media/dice/r"};

  // turn taking booleans
  boolean playerMoving = false;
  boolean playerMoved = false;

  boolean takingRole = false;
  boolean tookRole = false;

  boolean playerActed = false;

  boolean turnContinue = true;


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
    initPlayerDisplay();
    initTextArea();
    initScenes();

    bFrame.setVisible(true);
    println("Welcome to Deadwood!\n");
  }

  private void initLabels() {
    // Create labels for each room
    roomLabels = new JLabel[12];
    takeLabels = new JLabel[10][];
    roleLabels = new JLabel[10][];
    sceneRoleLabels = new JLabel[10][];

    for(int i = 0; i < 10; i++) {
        roomLabels[i] = new JLabel();
        roomLabels[i].setBounds(roomArr[i].getXy()[0], roomArr[i].getXy()[1], roomArr[i].getHw()[1], roomArr[i].getHw()[0]);
        roomLabels[i].addMouseListener(new boardMouseListener());
        roomLabels[i].setVisible(true);
        roomLabels[i].setOpaque(false);
        bPane.add(roomLabels[i], new Integer(2));

        // shot counter labels
        takeLabels[i] = new JLabel[roomArr[i].getShotCounter()];
        for(int j = 0; j < takeLabels[i].length; j++) {
          takeLabels[i][j] = new JLabel();
          takeLabels[i][j].setBounds(roomArr[i].getTakeXy()[0][j], roomArr[i].getTakeXy()[1][j], roomArr[i].getTakeHw()[1][j], roomArr[i].getTakeHw()[0][j]);
          takeLabels[i][j].addMouseListener(new boardMouseListener());
          takeLabels[i][j].setIcon(new ImageIcon("media/shot.png"));
          takeLabels[i][j].setVisible(true);
          bPane.add(takeLabels[i][j], new Integer(2));
        }

        // off card role labels
        roleLabels[i] = new JLabel[roomArr[i].getOffCardRoles().size()];
        for(int j = 0; j < roleLabels[i].length; j++) {
          roleLabels[i][j] = new JLabel();
          roleLabels[i][j].setBounds(roomArr[i].getOffCardRoles().get(j).getXy()[0],
                  roomArr[i].getOffCardRoles().get(j).getXy()[1],
                  roomArr[i].getOffCardRoles().get(j).getHw()[1],
                  roomArr[i].getOffCardRoles().get(j).getHw()[0]);
          roleLabels[i][j].addMouseListener(new boardMouseListener());
          roleLabels[i][j].setOpaque(false);
          roleLabels[i][j].setVisible(true);
          bPane.add(roleLabels[i][j], new Integer(2));
        }
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
    bFrame.setSize(boardIcon.getIconWidth()+250,boardIcon.getIconHeight());
  }

  /* function initPlayers
     purpose: intializes the player label objects
  */
  private void initPlayers() {
    playerLabels = new JLabel[6];
    Room trailerRoom = gameBoard.getRoomByName("trailer");

    // Add a dice to represent a player.
    for (int i = 0; i < totalPlayers; i++) {
        playerLabels[i] = new JLabel();
        playerLabels[i].setIcon(new ImageIcon(playerImages[i] + "1.png"));

        playerLabels[i].setBounds(trailerRoom.getXy()[0] + 50*i, trailerRoom.getXy()[1],46,46);
        playerLabels[i].setVisible(true);
        bPane.add(playerLabels[i],new Integer(3));
    }
  }

  /* function initMenu
     purpose: intializes the menu button objects
  */
  private void initMenu() {
    // Create the Menu for action buttons
    mLabel = new JLabel("MENU");
    mLabel.setBounds(boardIcon.getIconWidth()+105,0,150,20);
    bPane.add(mLabel,new Integer(2));

    // Create Action buttons
    bAct = new JButton("ACT");
    bAct.setBackground(Color.white);
    bAct.setBounds(boardIcon.getIconWidth()+25,30,200,20);
    bAct.addMouseListener(new boardMouseListener());

    bRehearse = new JButton("REHEARSE");
    bRehearse.setBackground(Color.white);
    bRehearse.setBounds(boardIcon.getIconWidth()+25,60,200,20);
    bRehearse.addMouseListener(new boardMouseListener());

    bMove = new JButton("MOVE");
    bMove.setBackground(Color.white);
    bMove.setBounds(boardIcon.getIconWidth()+25,90,200,20);
    bMove.addMouseListener(new boardMouseListener());

    bEnd = new JButton("END");
    bEnd.setBackground(Color.white);
    bEnd.setBounds(boardIcon.getIconWidth()+25,120,200,20);
    bEnd.addMouseListener(new boardMouseListener());

    bTakeRole = new JButton("TAKE ROLE");
    bTakeRole.setBackground(Color.white);
    bTakeRole.setBounds(boardIcon.getIconWidth()+25,150,200,20);
    bTakeRole.addMouseListener(new boardMouseListener());

    bUpgrade = new JButton("UPGRADE");
    bUpgrade.setBackground(Color.white);
    bUpgrade.setBounds(boardIcon.getIconWidth()+25,180,200,20);
    bUpgrade.addMouseListener(new boardMouseListener());

    // Place the action buttons in the top layer
    bPane.add(bAct, new Integer(2));
    bPane.add(bRehearse, new Integer(2));
    bPane.add(bMove, new Integer(2));
    bPane.add(bEnd, new Integer(2));
    bPane.add(bUpgrade, new Integer(2));
    bPane.add(bTakeRole, new Integer(2));
  }

  /* function initPlayerDisplay
     purpose: intializes the info panel objects
  */
  private void initPlayerDisplay() {
    dayLabel = new JLabel("Day 1");
    dayLabel.setBounds(boardIcon.getIconWidth()+105,210,150,20);
    bPane.add(dayLabel,new Integer(2));

    currPlayerLabel = new JLabel();
    currPlayerLabel.setIcon(new ImageIcon(playerImages[0] + "1.png"));
    currPlayerLabel.setBounds(boardIcon.getIconWidth()+105,231,46,46);
    bPane.add(currPlayerLabel,new Integer(2));

    infoTextArea = new JTextArea(5, 20);
    infoTextArea.setEditable(false);
    infoTextArea.setBounds(boardIcon.getIconWidth() + 10,285,230,105);
    bPane.add(infoTextArea, new Integer(2));
  }

  /* function updateInfoPanel
     purpose: updates the text in the info panel based on current conditions
     parameters: day, current game day
                 playerIndex, index into the player array
                 player, currently active Player object
  */
  public void updateInfoPanel(int day, int playerIndex, Player player) {
    currPlayerLabel.setIcon(new ImageIcon(playerImages[playerIndex] + player.getRank() + ".png"));
    dayLabel.setText("Day " + day);
    infoTextArea.setText(String.format(" Name: %s\n Rank: %d\n Money: $%d, %dcr\n Chips: %d\n Room: %s\n",
                                      player.getName(), player.getRank(), player.getDollars(), player.getCredits(), player.getChips(), player.getRoom().getName()));
    if(player.hasRole()) {
      infoTextArea.append(String.format(" Role: %s\n Line: %s", player.getRole().getName(), player.getRole().getLine()));
    }
  }

  /* function showPopUp
     purpose: wrapper for the JOptionPane message dialog method
     parameters: msg, String form of the message to display
  */
  public void showPopUp(String msg) {
    JOptionPane.showMessageDialog(null, msg);
  }

  /* function initScenes
     purpose: intializes the scene labels
  */
  private void initScenes() {
    sceneLabels = new JLabel[10];

    ImageIcon cIcon =  new ImageIcon("media/cards/CardBack.jpg");

    for(int i = 0; i < 10; i++) {
      sceneLabels[i] = new JLabel();
      sceneLabels[i].setIcon(cIcon);
      sceneLabels[i].setBounds(roomArr[i].getXy()[0], roomArr[i].getXy()[1], roomArr[i].getHw()[1], roomArr[i].getHw()[0]);
      sceneLabels[i].setOpaque(true);
      bPane.add(sceneLabels[i], new Integer(1));
    }
  }

  /* function initSceneRoles
     purpose: intializes the role labels inside the scenes
  */
  private void initSceneRoles(Room r) {
    int roomIndex = gameBoard.getRoomIndexByName(r.getName());

    List<Role> sceneRoles = r.getSceneRoles();
    sceneRoleLabels[roomIndex] = new JLabel[sceneRoles.size()];

    for (int j = 0; j < sceneRoles.size(); j++) {
      sceneRoleLabels[roomIndex][j] = new JLabel();
      sceneRoleLabels[roomIndex][j].setBounds(sceneRoles.get(j).getXy()[0] + sceneLabels[roomIndex].getX(),
              sceneRoles.get(j).getXy()[1] + sceneLabels[roomIndex].getY(),
              sceneRoles.get(j).getHw()[1],
              sceneRoles.get(j).getHw()[0]);
      sceneRoleLabels[roomIndex][j].addMouseListener(new boardMouseListener());
      sceneRoleLabels[roomIndex][j].setOpaque(false);
      sceneRoleLabels[roomIndex][j].setVisible(true);
      bPane.add(sceneRoleLabels[roomIndex][j], new Integer(3));
    }
  }

  /* function resetScenes
     purpose: resets the scene labels for the new day
  */
  public void resetCards() {
    for(int i = 0; i < 10; i++) {
      sceneLabels[i].setIcon(new ImageIcon("media/cards/CardBack.jpg"));
      sceneLabels[i].setVisible(true);
      for(int j = 0; j < takeLabels[i].length; j++) {
        takeLabels[i][j].setVisible(true);
      }
    }
  }

  /* function initTextArea
     purpose: intializes the console text area
  */
  private void initTextArea() {
    bTextArea = new JTextArea(5, 20);
    bTextArea.setEditable(false);
    bTextArea.setLineWrap(true);
    bTextArea.setWrapStyleWord(true);
    bTextArea.setCaretPosition(bTextArea.getDocument().getLength());

    bScrollPane = new JScrollPane(bTextArea);
    bScrollPane.setBounds(boardIcon.getIconWidth() + 10, boardIcon.getIconHeight() - 500, 230, 490);
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

  /* function getPlayerNames
     purpose: gets input from the players and passes the result to the board controller
     returns: String array of player names
  */
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

  /* function movePlayersTrailer
     purpose: positions the player labels inside the trailer room
  */
  public void movePlayersTrailer() {
    List<Player> players = gameBoard.getPlayers();

    for(int i = 0; i < totalPlayers; i++) {
      Player currPlayer = players.get(i);
      movePlayerRoom(currPlayer.getRoom(), "trailer", i);
    }
  }

  /* function movePlayerRoom
     purpose: moves a specific player label to a specified room, adjusting the position based on the other players
     parameters: currRoom, room player is currently in
                 targetRoomName, String name of the desired room
                 playerIndex, index of the active player into the player array
  */
  private void movePlayerRoom(Room currRoom, String targetRoomName, int playerIndex) {
    int roomIndex = gameBoard.getRoomIndexByName(targetRoomName);
    Room targetRoom = roomArr[roomIndex];
    if(!targetRoom.getName().equals("office") && !targetRoom.getName().equals("trailer")) {
      if(!targetRoom.getVisited() && targetRoom.getSceneActive()) {
        targetRoom.visit();
        sceneLabels[roomIndex].setIcon(new ImageIcon("media/cards/" + targetRoom.getSceneImg()));
        initSceneRoles(targetRoom);
      }
    }

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

  /* class boardMouseListener
     purpose: observes MouseEvents that occur and trigger certain functions
  */
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

        /* player acting */
        if(e.getSource() == bAct) {
          if(currPlayer.hasRole()) {
            if(!tookRole) {
              if(!playerActed) {
                boolean successful = gameBoard.playerAct(currPlayer);
                playerActed = true;
                if(successful) {
                  int roomIndex = gameBoard.getRoomIndexByName(currRoom.getName());
                  takeLabels[roomIndex][(currRoom.getCurrentShots() > 0 ? currRoom.getCurrentShots() - 1 : 0)].setVisible(false);
                  if(!currRoom.getSceneActive()) {
                    sceneLabels[roomIndex].setVisible(false);

                    for(int i = 0; i < sceneRoleLabels[roomIndex].length; i++) {
                      sceneRoleLabels[roomIndex][i].setVisible(false);
                    }
                    movePlayerRoom(currRoom, currRoom.getName(), gameBoard.getCurrentPlayerIndex());
                  }
                }
              }
              else {
                println("Player has already acted this turn");
              }
            }
            else {
                println("Player took a role this turn");
            }
          }
          else {
            println("Player does not have a role");
          }
        }

        /* player rehearsing */
        else if(e.getSource() == bRehearse) {
          if(!tookRole) {
            if(!playerActed) {
              playerActed = gameBoard.playerRehearse(currPlayer);
            }
            else {
              println("Player has already acted this turn");
            }
          }
          else {
            println("Player took a role this turn");
          }
        }

        /* player moving */
        else if(e.getSource() == bMove) {
          if(!playerActed) {
            if(!playerMoved && !currPlayer.hasRole()) {
              playerMoving = true;
              println("Select a room to move to");

              // Highlight available rooms
              for(int j = 0; j < neighbors.size(); j++) {
                int ind = gameBoard.getRoomIndexByName(neighbors.get(j));

                roomLabels[ind].setIcon(new ImageIcon("media/cards/OpenCard.png"));
              }
            }
            else {
              println("Player has already moved or has a role");
            }
          }
          else {
            println("Player acted this turn");
          }
        }

        /* player end turn */
        else if(e.getSource() == bEnd) {
          turnContinue = false;
        }

        /* player upgrading */
        else if(e.getSource() == bUpgrade) {
          if(currRoom.getName().equals("office")) {

            int targetRank;
            int moneyType;

            do {
              String temp = JOptionPane.showInputDialog(null, "What rank? (2-6)");
              try {
                targetRank = Integer.parseInt(temp);
              } catch(NumberFormatException exception) {
                targetRank = 0;
              }
            } while (targetRank < 2 || targetRank > 6);

            do {
              String temp = JOptionPane.showInputDialog(null, "Dollars or credits? (0/1)");
              try {
                moneyType = Integer.parseInt(temp);
              } catch(NumberFormatException exception) {
                moneyType = 0;
              }
            } while (moneyType < 0 || moneyType > 1);

            if(gameBoard.playerUpgrade(currPlayer, targetRank, moneyType) == 1) {
              int cPlayerIndex = gameBoard.getCurrentPlayerIndex();
              playerLabels[cPlayerIndex].setIcon(new ImageIcon(playerImages[cPlayerIndex] + gameBoard.getPlayers().get(cPlayerIndex).getRank() + ".png"));
            }
          }
          else {
            println("Player is not at the Casting Office");
          }
        }

        /* player taking a role */
        else if(e.getSource() == bTakeRole) {
          if(!currPlayer.hasRole()) {
            if(!currRoom.getName().equals("office") && !currRoom.getName().equals("trailer")) {
              if(currRoom.getSceneActive()) {
                takingRole = true;
                println("Select a role to take");
              }
              else {
                println("Scene is already wrapped");
              }
            }
            else {
              println("No roles are available in the Trailers or Casting Office");
            }
          }
          else {
            println("Player already has a role");
          }
        }

        /* taking a role event handler */
        else if(takingRole) {
          int roomIndex = gameBoard.getRoomIndexByName(currRoom.getName());
          List<Role> roomRoles = currRoom.getOffCardRoles();
          List<Role> sceneRoles = currRoom.getSceneRoles();
          boolean found = false;

          // look through off card roles in the current room
          for(int i = 0; i < roleLabels[roomIndex].length; i++) {
            if(e.getSource() == roleLabels[roomIndex][i]) {
              tookRole = gameBoard.playerTakeRole(currPlayer, roomRoles.get(i).getName());
              if(tookRole) {
                Rectangle loc = new Rectangle(
                  roomRoles.get(i).getXy()[0],
                  roomRoles.get(i).getXy()[1],
                  46, 46);
                playerLabels[gameBoard.getCurrentPlayerIndex()].setBounds(loc);

                playerMoved = true;
              }
              found = true;
              break;
            }
          }

          // look through scene roles
          if(!found) {
            for(int i = 0; i < sceneRoleLabels[roomIndex].length; i++) {
              if(e.getSource() == sceneRoleLabels[roomIndex][i]) {
                tookRole = gameBoard.playerTakeRole(currPlayer, sceneRoles.get(i).getName());
                if(tookRole) {
                  Rectangle loc = new Rectangle(
                          sceneRoles.get(i).getXy()[0] + sceneLabels[gameBoard.getRoomIndexByName(currRoom.getName())].getX(),
                          sceneRoles.get(i).getXy()[1] + sceneLabels[gameBoard.getRoomIndexByName(currRoom.getName())].getY(),
                          46, 46);
                  playerLabels[gameBoard.getCurrentPlayerIndex()].setBounds(loc);

                  playerMoved = true;
                }
                break;
              }
            }
          }

          takingRole = false;
        }

        /* player moving event handler */
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
              break;
            }
          }
        }

        /* turn end event handler */
        if(!turnContinue) {
          gameBoard.turnEnd();
          playerMoving = false;
          playerMoved = false;

          takingRole = false;
          tookRole = false;

          playerActed = false;

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

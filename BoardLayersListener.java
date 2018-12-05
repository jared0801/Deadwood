/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/

import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import java.awt.event.*;
import java.util.List;

public class BoardLayersListener extends JFrame {

    // JLabels
    JLabel boardlabel;
    JLabel cardlabel;
    JLabel playerLabels[];
    JLabel mLabel;
    JLabel roomLabels[];

    //JButtons
    JButton bAct;
    JButton bRehearse;
    JButton bMove;

    // JLayered Pane
    JLayeredPane bPane;

    //
    Board gameBoard;
    Room[] roomArr;

    String playerImages[] = {"media/dice/b1.png", "media/dice/c1.png", "media/dice/g1.png", "media/dice/o1.png", "media/dice/p1.png", "media/dice/r1.png"};

    boolean playerMoving = false;
    int numPlayers = 0;

    // Constructor
    public BoardLayersListener(int players, Board board) {

        // Set the title of the JFrame
        super("Deadwood");
        // Set the exit option for the JFrame
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the JLayeredPane to hold the display, cards, dice and buttons
        bPane = getLayeredPane();
        //roomArr = rooms;
        gameBoard = board;
        roomArr = gameBoard.getRooms();
        playerLabels = new JLabel[6];
        numPlayers = players;

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
        JLabel trailerLabel = new JLabel();
        trailerLabel.setBounds(trailerRoom.getXy()[0], trailerRoom.getXy()[1], trailerRoom.getHw()[1], trailerRoom.getHw()[0]);
        trailerLabel.addMouseListener(new boardMouseListener());
        trailerLabel.setVisible(true);

        bPane.add(trailerLabel, new Integer(2));

        Room officeRoom = gameBoard.getRoomByName("office");
        JLabel officeLabel = new JLabel();
        officeLabel.setBounds(officeRoom.getXy()[0], officeRoom.getXy()[1], officeRoom.getHw()[1], officeRoom.getHw()[0]);
        officeLabel.addMouseListener(new boardMouseListener());
        officeLabel.setVisible(true);

        bPane.add(officeLabel, new Integer(2));

        // Create the deadwood board
        boardlabel = new JLabel();
        ImageIcon icon =  new ImageIcon("media/board.jpg");
        boardlabel.setIcon(icon);
        boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

        // Add the board to the lowest layer
        bPane.add(boardlabel, new Integer(0));

        // Set the size of the GUI
        setSize(icon.getIconWidth()+200,icon.getIconHeight());

        // Add a scene card to this room
        cardlabel = new JLabel();
        ImageIcon cIcon =  new ImageIcon("media/cards/01.png");
        cardlabel.setIcon(cIcon);
        cardlabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
        cardlabel.setOpaque(true);

        // Add the card to the lower layer
        bPane.add(cardlabel, new Integer(1));


        // Add a dice to represent a player.
        for (int i = 0; i < numPlayers; i++) {
            playerLabels[i] = new JLabel();
            playerLabels[i].setIcon(new ImageIcon(playerImages[i]));

            playerLabels[i].setBounds(trailerRoom.getXy()[0] + 50*i, trailerRoom.getXy()[1],46,46);
            playerLabels[i].setVisible(true);
            bPane.add(playerLabels[i],new Integer(3));
        }

        // Create the Menu for action buttons
        mLabel = new JLabel("MENU");
        mLabel.setBounds(icon.getIconWidth()+40,0,100,20);
        bPane.add(mLabel,new Integer(2));

        // Create Action buttons
        bAct = new JButton("ACT");
        bAct.setBackground(Color.white);
        bAct.setBounds(icon.getIconWidth()+10, 30,100, 20);
        bAct.addMouseListener(new boardMouseListener());

        bRehearse = new JButton("REHEARSE");
        bRehearse.setBackground(Color.white);
        bRehearse.setBounds(icon.getIconWidth()+10,60,100, 20);
        bRehearse.addMouseListener(new boardMouseListener());

        bMove = new JButton("MOVE");
        bMove.setBackground(Color.white);
        bMove.setBounds(icon.getIconWidth()+10,90,100, 20);
        bMove.addMouseListener(new boardMouseListener());

        // Place the action buttons in the top layer
        bPane.add(bAct, new Integer(2));
        bPane.add(bRehearse, new Integer(2));
        bPane.add(bMove, new Integer(2));
    }

    // This class implements Mouse Events

    class boardMouseListener implements MouseListener{

        // Code for the different button clicks
        public void mouseClicked(MouseEvent e) {
            //System.out.println(e.getLocationOnScreen());
            if (e.getSource()== bAct){
                System.out.println("Acting is Selected\n");
            }
            else if (e.getSource()== bRehearse){
                System.out.println("Rehearse is Selected\n");
            }
            else if (e.getSource()== bMove){
                if(gameBoard.isActiveGame()) {
                    playerMoving = true;
                }
            }

            if(playerMoving == true) {
                List<String> neighbors = gameBoard.getCurrentPlayer().getRoom().getNeighbors();
                for(int i = 0; i < neighbors.size(); i++) {
                    int ind = gameBoard.getRoomIndexByName(neighbors.get(i));
                    if(ind >= 0 && ind < 10){
                        if(!roomLabels[ind].isVisible()) {
                            roomLabels[ind].setVisible(true);
                        }
                    }

                    if(e.getSource() == roomLabels[ind]) {
                        // Move player to selected room
                        System.out.println(roomArr[ind].getName());
                        gameBoard.getCurrentPlayer().moveTo(gameBoard.getRoomByName(roomArr[ind].getName()));
                        Rectangle loc = new Rectangle(
                                gameBoard.getRoomByName(roomArr[ind].getName()).getXy()[0],
                                gameBoard.getRoomByName(roomArr[ind].getName()).getXy()[1],
                                46,46);

                        playerLabels[gameBoard.getCurrentPlayerIndex()].setBounds(loc);
                        playerMoving = false;
                        // Change turns
                        int nextPlayer = gameBoard.getCurrentPlayerIndex() + 1;
                        if(nextPlayer >= numPlayers) {
                            nextPlayer = nextPlayer % numPlayers;
                        }
                        gameBoard.setCurrentPlayer(gameBoard.getPlayers().get(nextPlayer));
                    }
                }
            }
        }
        public void mousePressed(MouseEvent e) {

        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
            /*for(int i = 0; i < 10; i++) {
                if (e.getSource()== roomLabels[i]){
                    List<String> neighbors = roomArr[i].getNeighbors();
                    for(int j = 0; j < neighbors.size(); j++) {
                        int ind = gameBoard.getRoomIndexByName(neighbors.get(j));
                        if(ind >= 0 && ind < 10){
                            if(roomLabels[ind].isVisible()) {
                                roomLabels[ind].setVisible(false);
                            }
                        }
                    }
                }
            }*/
        }
        public void mouseExited(MouseEvent e) {
            /*for(int i = 0; i < 10; i++) {
                if (e.getSource()== roomLabels[i]){
                    List<String> neighbors = roomArr[i].getNeighbors();
                    for(int j = 0; j < neighbors.size(); j++) {
                        int ind = gameBoard.getRoomIndexByName(neighbors.get(j));
                        if(ind >= 0 && ind < 10){
                            if(!roomLabels[ind].isVisible()) {
                                roomLabels[ind].setVisible(true);
                            }
                        }
                    }
                }
            }*/
        }
    }


    /* public static void main(String[] args) {

        BoardLayersListener board = new BoardLayersListener();
        board.setVisible(true);

        // Take input from the user about number of players
        JOptionPane.showInputDialog(board, "How many players?");
    } */
}

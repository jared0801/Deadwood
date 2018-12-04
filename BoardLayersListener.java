/*

   Deadwood GUI helper file
   Author: Moushumi Sharmin
   This file shows how to create a simple GUI using Java Swing and Awt Library
   Classes Used: JFrame, JLabel, JButton, JLayeredPane

*/

import java.awt.*;
import javax.swing.*;
import javax.swing.ImageIcon;
import javax.imageio.ImageIO;
import java.awt.event.*;
import javax.swing.JOptionPane;
import java.util.List;

public class BoardLayersListener extends JFrame {

    // JLabels
    JLabel boardlabel;
    JLabel cardlabel;
    JLabel playerlabel;
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

    // Constructor
    public BoardLayersListener(Board board) {

        // Set the title of the JFrame
        super("Deadwood");
        // Set the exit option for the JFrame
        setDefaultCloseOperation(EXIT_ON_CLOSE);

        // Create the JLayeredPane to hold the display, cards, dice and buttons
        bPane = getLayeredPane();
        //roomArr = rooms;
        gameBoard = board;
        roomArr = gameBoard.getRooms();

        // Create labels for each room
        roomLabels = new JLabel[10];
        for(int i = 0; i < 10; i++) {
            roomLabels[i] = new JLabel();
            roomLabels[i].setBounds(roomArr[i].getXy()[0], roomArr[i].getXy()[1], roomArr[i].getHw()[1], roomArr[i].getHw()[0]);
            roomLabels[i].setBackground(Color.white);
            roomLabels[i].addMouseListener(new boardMouseListener());
            roomLabels[i].setVisible(true);
            bPane.add(roomLabels[i], new Integer(2));
        }

        // Create the deadwood board
        boardlabel = new JLabel();
        ImageIcon icon =  new ImageIcon("board.jpg");
        boardlabel.setIcon(icon);
        boardlabel.setBounds(0,0,icon.getIconWidth(),icon.getIconHeight());

        // Add the board to the lowest layer
        bPane.add(boardlabel, new Integer(0));

        // Set the size of the GUI
        setSize(icon.getIconWidth()+200,icon.getIconHeight());

        // Add a scene card to this room
        cardlabel = new JLabel();
        ImageIcon cIcon =  new ImageIcon("01.png");
        cardlabel.setIcon(cIcon);
        cardlabel.setBounds(20,65,cIcon.getIconWidth()+2,cIcon.getIconHeight());
        cardlabel.setOpaque(true);

        // Add the card to the lower layer
        bPane.add(cardlabel, new Integer(1));




        // Add a dice to represent a player.
        // Role for Crusty the prospector. The x and y co-ordiantes are taken from Board.xml file
        playerlabel = new JLabel();
        ImageIcon pIcon = new ImageIcon("r2.png");
        playerlabel.setIcon(pIcon);
        //playerlabel.setBounds(114,227,pIcon.getIconWidth(),pIcon.getIconHeight());
        playerlabel.setBounds(114,227,46,46);
        playerlabel.setVisible(false);
        bPane.add(playerlabel,new Integer(3));

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
            System.out.println(e.getLocationOnScreen());
            if (e.getSource()== bAct){
                playerlabel.setVisible(true);
                System.out.println("Acting is Selected\n");
            }
            else if (e.getSource()== bRehearse){
                System.out.println("Rehearse is Selected\n");
            }
            else if (e.getSource()== bMove){
                System.out.println("Move is Selected\n");
            }
            else if (e.getSource()== roomLabels[0]){
                List<String> neighbors = roomArr[0].getNeighbors();
                for(int i = 0; i < neighbors.size(); i++) {
                    System.out.println(neighbors.get(i));
                }
            }
        }
        public void mousePressed(MouseEvent e) {
        }
        public void mouseReleased(MouseEvent e) {
        }
        public void mouseEntered(MouseEvent e) {
            if(e.getSource() == roomLabels[0]) {
                //roomLabel.setborder()
                System.out.println("mouse loc: " + e.getLocationOnScreen());
                System.out.println("Mouse entered woohoo!!");
            }
        }
        public void mouseExited(MouseEvent e) {
        }
    }


    /* public static void main(String[] args) {

        BoardLayersListener board = new BoardLayersListener();
        board.setVisible(true);

        // Take input from the user about number of players
        JOptionPane.showInputDialog(board, "How many players?");
    } */
}
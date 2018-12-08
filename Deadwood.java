import org.w3c.dom.Document;

import javax.swing.*;
import java.util.*;

public class Deadwood {
    public static void main(String args[]) {

      int numPlayers;

      List<Scene> sceneArr = null;
      Room[] roomArr = null;
      int[][] upgrades = null;
      Board boardCtrl;
      BoardViewManager boardView;
      //BoardLayersListener boardView;
      InputManager inputManager;

      Document sceneDoc;
      Document boardDoc;
      XMLParse parser = new XMLParse();
      try {
        sceneDoc = parser.getDocFromFile("cards.xml");
        sceneArr = parser.parseScenes(sceneDoc);

        boardDoc = parser.getDocFromFile("board.xml");
        roomArr = parser.parseBoard(boardDoc);
        upgrades = parser.parseUpgrades(boardDoc);
      } catch (Exception e) {
        System.out.println("Error = " + e);
      }

      // Take input from the user about number of players
      do {
        String temp = JOptionPane.showInputDialog(null, "How many players?");
        try {
          numPlayers = Integer.parseInt(temp);
        } catch(NumberFormatException e) {
          numPlayers = 0;
        }
      } while (numPlayers < 2 || numPlayers > 6);

      boardCtrl = new Board(numPlayers, roomArr, sceneArr, upgrades);
      boardView = BoardViewManager.getInstance();
      //boardView = new BoardLayersListener(numPlayers, boardCtrl);


      System.out.println();

      boardView.initGUI(boardCtrl, numPlayers);
      //boardView.setVisible(true);

      boardCtrl.createGame();
    }
}

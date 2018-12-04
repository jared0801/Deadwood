import org.w3c.dom.Document;

import javax.swing.*;
import java.util.*;

public class Deadwood {
    public static void main(String args[]) {
      /* if(args.length != 1) {
        System.out.println("Error: Wrong number of arguments.");
        System.out.println("Usage: java Deadwood num_players");
        System.exit(-1);
      } */

      int numPlayers;

      List<Scene> sceneArr = null;
      Room[] roomArr = null;
      int[][] upgrades = null;
      Board boardCtrl;
      BoardLayersListener boardView;
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

      System.out.println("Welcome to Deadwood!\nType \'help\' for a list of commands.");
      inputManager = InputManager.getInstance();

      // Take input from the user about number of players
      do {
        numPlayers = Integer.parseInt(JOptionPane.showInputDialog(null, "How many players?"));
      } while (numPlayers < 2 || numPlayers > 6);

      boardCtrl = new Board(numPlayers, roomArr, sceneArr, upgrades, inputManager);
      boardView = new BoardLayersListener(boardCtrl);

      System.out.println();

      boardView.setVisible(true);

      boardCtrl.createGame();
      boardCtrl.runGame();
    }
}

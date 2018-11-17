import org.w3c.dom.Document;
import java.util.Scanner;
import java.util.*;

public class Deadwood {
    public static void main(String args[]) {
      if(args.length != 1) {
        System.out.println("Error: Wrong number of arguments.");
        System.out.println("Usage: java Deadwood num_players");
        System.exit(-1);
      }

      int numPlayers = Integer.parseInt(args[0]);

      List<Scene> sceneArr = null;
      Room[] roomArr = null;
      int[][] upgrades = null;
      Board board;
      Scanner in = new Scanner(System.in);

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

      board = new Board(numPlayers, roomArr, sceneArr, upgrades);

      System.out.println();

      board.createGame();
      board.runGame();
    }
}

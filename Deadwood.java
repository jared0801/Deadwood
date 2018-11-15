import org.w3c.dom.Document;
import java.util.Scanner;
import java.util.*;

public class Deadwood {
    public static void main(String args[]) {
      if(args.length != 2) {
        System.out.println("Error: Wrong number of arguments.");
        System.out.println("Usage: java Deadwood cards.xml board.xml");
        System.exit(-1);
      }
      String sceneXml = args[0];
      String boardXml = args[1];

      List<Scene> sceneArr = null;
      Room[] roomArr = null;
      int[][] upgrades = null;
      Board board;
      Scanner in = new Scanner(System.in);

      Document sceneDoc;
      Document boardDoc;
      XMLParse parser = new XMLParse();
      try {
          sceneDoc = parser.getDocFromFile(sceneXml);
          sceneArr = parser.parseScenes(sceneDoc);

          boardDoc = parser.getDocFromFile(boardXml);
          roomArr = parser.parseBoard(boardDoc);
          upgrades = parser.parseUpgrades(boardDoc);
      } catch (Exception e) {
          System.out.println("Error = " + e);
      }

      System.out.print("Enter number of players: ");
      int numPlayers = in.nextInt();

      board = new Board(numPlayers, roomArr, sceneArr, upgrades);

      System.out.println();

      board.createGame();
      board.runGame();
    }
}

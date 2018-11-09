import org.w3c.dom.Document;
import java.util.Scanner;

public class Deadwood {
    public static void main(String args[]) {
      if(args.length != 2) {
        System.out.println("Error: Wrong number of arguments.");
        System.out.println("Usage: java Deadwood cards.xml board.xml");
        System.exit(-1);
      }
      String sceneXml = args[0];
      String boardXml = args[1];

      Scene[] sceneArr;
      Board board;
      Scanner in = new Scanner(System.in);

      Document sceneDoc;
      XMLParse parser = new XMLParse();
      try {
          sceneDoc = parser.getDocFromFile(sceneXml);
          sceneArr = parser.parseScenes(sceneDoc);

          System.out.println(sceneArr.length);
          for(int i = 0; i < sceneArr.length; i++) {
              System.out.println(sceneArr[i]);
          }
      } catch (Exception e) {
          System.out.println("Error = " + e);
      }

      System.out.print("Enter number of players: ");
      int numPlayers = in.nextInt();

      board = new Board(numPlayers);

      System.out.println();
      
      board.createGame();
      Player active = board.getCurrentPlayer();
      System.out.println("Active player: ");
      System.out.print(active);
    }
}

import java.util.*;
import java.util.Scanner;

public class Board {
    private int totalPlayers, activePlayerIndex, currentDay, totalDays;
    private List<Player> players;
    private Room[] rooms;
    private Scanner in;

    public Board(int numPlayers, Room[] roomArr) {
      totalPlayers = numPlayers;
      players = new ArrayList<Player>(numPlayers);
      rooms = roomArr;
      currentDay = 0;
      totalDays = 3;
      in = new Scanner(System.in);
    }

    public void runGame() {
      while(true) {
        runDay();
        newDay();
        if(currentDay > totalDays) {
          endGame();
          return;
        }
      }
    }

    public void runDay() {
      System.out.format("Day %d\n", currentDay);
      boolean running = true;
      while(running) {
        takeTurn();
        running = checkDayCont();
      }
    }

    public void newDay() {
      currentDay++;

      // logic for moving players to the trailer and assigning scenes to rooms

      return;
    }

    public void createGame() {
      for(int i = 0; i < totalPlayers; i++) {
        System.out.format("Enter Player %d name: ", i + 1);
        String name = in.next();
        players.add(new Player(name));
      }

      newDay();
      activePlayerIndex = 0;
      return;
    }

    public void endGame() {
      System.out.println("Gameover, thank you for playing Deadwood!");
      in.close();
      return;
    }

    public List<Player> getPlayers() {
      return players;
    }

    public Room[] getRooms() {
      return rooms;
    }

    public Player getCurrentPlayer() {
      return players.get(activePlayerIndex);
    }

    public void takeTurn() {
      System.out.format("Player %d enter move: ", activePlayerIndex + 1);
      String move = in.next();
      System.out.format("Player %d entered \"%s\"!\n", activePlayerIndex + 1, move);

      // logic for checking entered move

      activePlayerIndex = (activePlayerIndex + 1) % totalPlayers;
      return;
    }

    private boolean checkDayCont() {
      Room currentRoom;
      int scenesRemaining = 0;
      for(int i = 0; i < rooms.length; i++) {
        currentRoom = rooms[i];
        if(currentRoom.getSceneActive()) {
          scenesRemaining++;
        }
      }
      if(scenesRemaining < 2) {
        return false;
      }
      return true;
    }

    public void modifyMoney(int dollars, int credits, Player target) {
      target.modifyDollars(dollars);
      target.modifyCredits(credits);
      return;
    }
}

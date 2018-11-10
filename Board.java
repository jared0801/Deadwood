import java.util.*;
import java.util.Scanner;

public class Board {
    private int totalPlayers, activePlayerIndex, currentDay, totalDays;
    private List<Player> players;
    private Room[] rooms;
    private Scanner in;
    private List<Scene> scenes;

    public Board(int numPlayers, Room[] roomArr, List<Scene> sceneArr) {
      totalPlayers = numPlayers;
      players = new ArrayList<Player>(numPlayers);
      rooms = roomArr;
      currentDay = 0;
      totalDays = 3;
      in = new Scanner(System.in);
      scenes = sceneArr;
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

      for(int i = 0; i < totalPlayers; i++) {
        movePlayer(players.get(i), "trailer");
        System.out.format("Moved player %d to trailers\n", i + 1);
      }
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
      Player currPlayer = players.get(activePlayerIndex);
      System.out.format("Player %d enter move: ", activePlayerIndex + 1);
      String command = in.next();
      String arg = "";
      System.out.format("Player %d entered \"%s\"!\n", activePlayerIndex + 1, command);

      switch(command) {
        case "move": arg = in.next();
                     movePlayer(currPlayer, arg);
                     break;
        case "list": arg = in.next();
                     if(arg.equals("rooms")) {
                       for(int i = 0; i < rooms.length; i++) {
                         System.out.format("%s\n", rooms[i].getName());
                       }
                       break;
                     }

        default: System.out.format("Invalid command entered: %s\n", command);
                 break;
      }

      activePlayerIndex = (activePlayerIndex + 1) % totalPlayers;
      return;
    }

    public void movePlayer(Player currPlayer, String roomStr) {
      Room current = currPlayer.getRoom();
      Room target = getRoomByName(roomStr);
      if(target == null) {
        System.out.format("Error: Room %s not found\n", roomStr);
        return;
      }
      if(!roomStr.equals("trailer")) {
        if(current.checkIfNeighbor(roomStr)) {
          currPlayer.moveTo(target);
        }
      }
      else {
        currPlayer.moveTo(target);
      }
    }

    private Room getRoomByName(String name) {
      for(int i = 0; i < rooms.length; i++) {
        if(rooms[i].getName().equals(name)) {
          return rooms[i];
        }
      }
      return null;
    }

    private boolean checkDayCont() {
      Room currentRoom;
      int scenesRemaining = 0;
      for(int i = 0; i < rooms.length; i++) {
        currentRoom = rooms[i];
        System.out.println(currentRoom.getName());
        if(!currentRoom.getName().equals("trailer") && !currentRoom.getName().equals("office")) {
          if(currentRoom.getSceneActive()) {
            scenesRemaining++;
          }
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

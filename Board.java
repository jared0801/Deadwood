import java.util.*;
import java.util.Scanner;
import java.lang.Math;

public class Board {
    private int totalPlayers, activePlayerIndex, currentDay, totalDays;
    private List<Player> players;
    private Room[] rooms;
    private Scanner in;
    private Scanner lineScan;
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
      System.out.format("\nDay %d\n", currentDay);
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

      for(int i = 0; i < rooms.length; i++) {
        String name = rooms[i].getName();
        if(!name.equals("trailer") && !name.equals("office")) {
          boolean notAssigned = true;
          while(notAssigned) {
            int sceneIndex = (int) (Math.random() * 40);
            if(scenes.get(sceneIndex).getActive()) {
              rooms[i].setScene(scenes.get(sceneIndex));
              notAssigned = false;
            }
          }
        }
      }

      return;
    }

    public void createGame() {
      for(int i = 0; i < totalPlayers; i++) {
        System.out.format("Enter Player %d name: ", i + 1);
        String name = in.next();
        players.add(new Player(name));
        in.nextLine();
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
      Player currPlayer = getCurrentPlayer();
      boolean turnContinue = true;
      boolean hasMoved = false;
      String line = "";
      String command = "";
      String arg = "";
      while(turnContinue) {
        System.out.format("Player %d enter move: ", activePlayerIndex + 1);
        line = in.nextLine();
        System.out.format("Player %d entered \'%s\'!\n", activePlayerIndex + 1, line);
        lineScan = new Scanner(line);
        command = lineScan.next();

        switch(command) {
          case "help":  System.out.println("Instructions:");
                        System.out.println("- move room_name: Moves active player to room specified by room_name");
                        System.out.println("- list rooms: Prints all rooms");
                        System.out.println("- list neighbors: Prints all neighboring rooms");
                        System.out.println("- desc room [room_name]: Prints out attributes of the room specified by room_name (defaults to current room)");
                        System.out.println("- desc scene: Prints out attributes of the current room's scene");
                        System.out.println("- desc player [player_name]: Prints out attributes of the player specified by player_name (defaults to active player)");
                        System.out.println("- take role role_name: Takes the role specified by role_name");
                        System.out.println("- act: Acts under the current role");
                        System.out.println("- rehearse: Rehearses under the current role");
                        break;

          case "move":  if(lineScan.hasNext()) {
                          arg = lineScan.next();
                          while(lineScan.hasNext()) {
                            arg += " " + lineScan.next();
                          }

                          if(!hasMoved && !currPlayer.hasRole()) {
                            if(movePlayer(currPlayer, arg)) {
                              hasMoved = true;
                            }
                          }
                          else {
                            System.out.println("Player has already moved or currently has a role");
                          }
                        }
                        else {
                          System.out.println("Command requires arguments");
                        }
                        break;

          case "list":  if(lineScan.hasNext()) {
                          arg = lineScan.next();
                          if(arg.equals("rooms")) {
                            for(int i = 0; i < rooms.length; i++) {
                              System.out.format("%s\n", rooms[i].getName());
                            }
                          }
                          else if(arg.equals("neighbors")) {
                            currPlayer.getRoom().listNeighbors();
                          }
                          else {
                            System.out.format("Invalid arg entered: \'%s\'\n", arg);
                          }
                        }
                        else {
                          System.out.println("Command requires arguments");
                        }
                        break;

          case "desc":  if(lineScan.hasNext()) {
                          arg = lineScan.next();
                          if(arg.equals("room")) {
                            if(lineScan.hasNext()) {
                              arg = lineScan.next();
                              while(lineScan.hasNext()) {
                                arg += " " + lineScan.next();
                              }
                              Room target = getRoomByName(arg);
                              if(arg == null) {
                                System.out.format("Room \'%s\' not found\n", arg);
                              }
                              else {
                                System.out.print(getRoomByName(arg));
                              }
                            }
                            else {
                              System.out.print(currPlayer.getRoom());
                            }
                          }
                          else if(arg.equals("scene")) {
                            Scene curr = currPlayer.getRoom().getScene();
                            if(curr == null || !curr.getActive()) {
                              System.out.println("Room does not have a scene or scene wrapped");
                            }
                            else {
                              System.out.print(currPlayer.getRoom().getScene());
                            }
                          }
                          else if(arg.equals("player")) {
                            if(lineScan.hasNext()) {
                              arg = lineScan.next();
                              while(lineScan.hasNext()) {
                                arg += " " + lineScan.next();
                              }
                              Player target = getPlayerByName(arg);
                              if(target == null) {
                                System.out.format("Unrecognized player \'%s\'\n", arg);
                              }
                              else {
                                System.out.print(target);
                              }
                            }
                            else {
                              System.out.print(currPlayer);
                            }
                          }
                          else {
                            System.out.format("Invalid arg entered: \'%s\'\n", arg);
                          }
                        }
                        else {
                          System.out.println("Command requires arguments");
                        }
                        break;

          case "take":  if(lineScan.hasNext()) {
                          arg = lineScan.next();
                          if(arg.equals("role")) {
                            if(lineScan.hasNext()) {
                              arg = lineScan.next();
                              while(lineScan.hasNext()) {
                                arg += " " + lineScan.next();
                              }
                              Role target = currPlayer.getRoom().getRoleByName(arg);
                              if(target == null || !currPlayer.getRoom().getSceneActive()) {
                                System.out.format("Unrecognized role: \'%s\'\n", arg);
                              }
                              else if(target.getRank() <= currPlayer.getRank() && !target.checkTaken()) {
                                currPlayer.takeRole(target);
                                turnContinue = false;
                              }
                              else {
                                System.out.format("Player not high enough level for requested role, scene wrapped, or role already taken\n", target.getRank(), currPlayer.getRank());
                              }
                            }
                            else {
                              System.out.println("Command requires arguments");
                            }
                          }
                          else {
                            System.out.format("Invalid arg entered: \'%s\'\n", arg);
                          }
                        }
                        else {
                          System.out.println("Command requires arguments");
                        }
                        break;

          case "act":   int result = 0;
                        int roll = 0;
                        int budget = 0;
                        Room current = currPlayer.getRoom();
                        if(currPlayer.hasRole()) {
                          budget = current.getSceneDifficulty();
                          roll = currPlayer.act();
                          System.out.format("Player rolled %d with budget %d\n", roll, budget);
                          result = current.shootScene(roll);
                          if(result >= 0) {
                            System.out.print("Roll successful, ");
                            if(currPlayer.hasSceneRole()) {
                              System.out.println("player earns 2 credits");
                              modifyMoney(0, 2, currPlayer);
                            }
                            else {
                              System.out.println("player earns 1 credit and 1 dollar");
                              modifyMoney(1, 1, currPlayer);
                            }

                            if(result == 0) {
                              System.out.println("All shots completed, scene wrap");
                              current.wrapScene();

                              // payout bonuses and remove players from their roles
                              List<Player> playersInRoom = getPlayersInRoom(current);
                              for(int i = 0; i < playersInRoom.size(); i++) {
                                playersInRoom.get(i).leaveRole();
                              }

                              // int[] bonuses = generatePayout(budget);
                              // if(current.checkIfSceneRolesTaken()) {
                              //
                              // }
                            }
                          }
                          else if(!currPlayer.hasSceneRole()) {
                            System.out.println("Unsuccessful roll, player earns 1 dollar");
                            modifyMoney(1, 0, currPlayer);
                          }
                          else {
                            System.out.println("Unsuccessful roll");
                          }
                          turnContinue = false;
                        }
                        else {
                          System.out.println("Player does not have a role");
                        }
                        break;

          case "rehearse":  if(currPlayer.hasRole() && currPlayer.getChips() < currPlayer.getRoom().getSceneDifficulty()) {
                              currPlayer.rehearse();
                              turnContinue = false;
                            }
                            else {
                              System.out.println("Player does not have a role or has too many chips");
                            }
                            break;

          case "end":       turnContinue = false;
                            break;

          default:          System.out.format("Invalid command entered: %s\n", command);
                            break;
        }
      }

      activePlayerIndex = (activePlayerIndex + 1) % totalPlayers;
      return;
    }


    // returns true if move is valid and player has been moved
    // false otherwise
    public boolean movePlayer(Player currPlayer, String roomStr) {
      Room current = currPlayer.getRoom();
      Room target = getRoomByName(roomStr);
      if(target == null) {
        System.out.format("Error: Room \'%s\' not found\n", roomStr);
        return false;
      }

      if(!roomStr.equals("trailer")) {
        if(current.checkIfNeighbor(roomStr)) {
          currPlayer.moveTo(target);
        }
      }
      else {
        currPlayer.moveTo(target);
      }
      return true;
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

    private int[] generatePayout(int budget) {
      int ret[] = new int[budget];
      for(int i = 0; i < budget; i++) {
        ret[i] = (int) (Math.random() * 6) + 1;
      }
      return ret;
    }

    private List<Player> getPlayersInRoom(Room target) {
      List<Player> ret = new ArrayList<Player>();
      String name = target.getName();
      for(int i = 0; i < players.size(); i++) {
        if(players.get(i).getRoom().getName().equals(name)) {
          ret.add(players.get(i));
        }
      }
      return ret;
    }

    private Player getPlayerByName(String target) {
      for(int i = 0; i < players.size(); i++) {
        if(players.get(i).getName().equals(target)) {
          return players.get(i);
        }
      }
      return null;
    }
}

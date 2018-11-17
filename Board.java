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
    private int[][] upgrades;

    public Board(int numPlayers, Room[] roomArr, List<Scene> sceneArr, int[][] upgradeCosts) {
      totalPlayers = numPlayers;
      players = new ArrayList<Player>(numPlayers);
      rooms = roomArr;
      currentDay = 0;
      totalDays = 3;
      in = new Scanner(System.in);
      scenes = sceneArr;
      upgrades = upgradeCosts;
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

    private void runDay() {
      System.out.format("\nDay %d\n", currentDay);
      boolean running = true;
      while(running) {
        takeTurn();
        running = checkDayCont();
      }
    }

    private void newDay() {
      currentDay++;

      for(int i = 0; i < totalPlayers; i++) {
        forceMovePlayer(players.get(i), "trailer");
        players.get(i).leaveRole();
        //System.out.format("Moved player %d to trailers\n", i + 1);
      }

      for(int i = 0; i < rooms.length; i++) {
        String name = rooms[i].getName();
        if(!name.equals("trailer") && !name.equals("office")) {
          boolean notAssigned = true;
          while(notAssigned) {
            int sceneIndex = (int) (Math.random() * 40);
            Scene sceneRef = scenes.get(sceneIndex);
            if(sceneRef.getActive() && !sceneRef.getAssigned()) {
              rooms[i].setScene(sceneRef);
              sceneRef.assign();
              //System.out.format("Assigned scene \'%s\' to room \'%s\'\n", sceneRef.getName(), rooms[i].getName());
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

    private void endGame() {
      int maxScore = 0;
      Player winner = null;
      for(int i = 0; i < players.size(); i++) {
        int score = calcScore(players.get(i));
        System.out.format("Player %s has %d points\n", players.get(i).getName(), score);
        if(maxScore < score) {
          maxScore = score;
          winner = players.get(i);
        }
      }
      System.out.format("\nPlayer %s won with %d points\n", winner.getName(), maxScore);
      System.out.println("Gameover, thank you for playing Deadwood!");
      in.close();
      return;
    }

    private int calcScore(Player target) {
      return target.getDollars() + target.getCredits() + (target.getRank() * 5);
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

    private void takeTurn() {
      Player currPlayer = getCurrentPlayer();
      boolean turnContinue = true;
      boolean hasMoved = false;
      String line = "";
      String command = "";
      String arg = "";
      System.out.println();
      for(int i = 0; i < players.size(); i++) {
        System.out.format("Player %s is currently located in the %s\n", players.get(i).getName(), players.get(i).getRoom().getName());
      }
      while(turnContinue) {
        System.out.format("Player %s enter move: ", currPlayer.getName());
        line = in.nextLine();
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
                        System.out.println("- upgrade rank_num: Upgrades the player's rank to rank_num and deducts the appropriate amount of currency");
                        System.out.println("- end: ends the active player's turn");
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
                          System.out.println("Command requires additional arguments");
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
                          System.out.println("Command requires additional arguments");
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
                          System.out.println("Command requires additional arguments");
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
                              if(target == null) {
                                System.out.format("Unrecognized role: \'%s\'\n", arg);
                              }
                              else if(!currPlayer.getRoom().getSceneActive()) {
                                System.out.println("Current room does not have an active scene");
                              }
                              else if(target.isTaken()) {
                                System.out.println("Requested role is already taken");
                              }
                              else if(target.getRank() <= currPlayer.getRank()) {
                                currPlayer.takeRole(target);
                                turnContinue = false;
                              }
                              else {
                                System.out.format("Player not high enough level for requested role (required: %d, player rank: %d)\n", target.getRank(), currPlayer.getRank());
                              }
                            }
                            else {
                              System.out.println("Command requires additional arguments");
                            }
                          }
                          else {
                            System.out.format("Invalid arg entered: \'%s\'\n", arg);
                          }
                        }
                        else {
                          System.out.println("Command requires additional arguments");
                        }
                        break;

          case "act":   int result = 0;
                        int roll = 0;
                        int budget = 0;
                        Room current = currPlayer.getRoom();
                        if(currPlayer.hasRole()) {
                          budget = current.getSceneDifficulty();
                          roll = currPlayer.act();
                          System.out.format("Acting on role \'%s\'\n", currPlayer.getRole().getName());
                          System.out.format("Player rolled %d with budget %d\n", roll, budget);
                          result = current.shootScene(roll);
                          if(result >= 0) {
                            System.out.print("Roll successful, ");
                            if(currPlayer.hasSceneRole()) {
                              System.out.println("Player earns 2 credits");
                              modifyMoney(0, 2, currPlayer);
                            }
                            else {
                              System.out.println("Player earns 1 credit and 1 dollar");
                              modifyMoney(1, 1, currPlayer);
                            }

                            if(result == 0) {
                              System.out.println("All shots completed, scene wrap");

                              List<Player> playersInRoom = getPlayersInRoom(current);

                              // payout bonuses to players in roles
                              if(current.checkIfSceneRolesTaken()) {

                                // payout bonuses to players in scene roles
                                List<Integer> bonuses = generatePayout(budget);
                                List<Role> sceneRoles = current.getSceneRoles();
                                int roleIndex = sceneRoles.size() - 1;
                                Player target;
                                System.out.format("Bonus dice rolled: %s\n", bonuses.toString());
                                for(int i = 0; i < bonuses.size(); i++) {
                                  if(sceneRoles.get(roleIndex).isTaken()) {
                                    target = getPlayerInRole(sceneRoles.get(roleIndex), playersInRoom);
                                    System.out.format("Player %s receives %d dollars\n", target.getName(), bonuses.get(i));
                                    modifyMoney(bonuses.get(i), 0, target);
                                  }
                                  roleIndex--;
                                  if(roleIndex < 0) {
                                    roleIndex = sceneRoles.size() - 1;
                                  }
                                }

                                // payout bonuses to players in room roles
                                for(int i = 0; i < playersInRoom.size(); i++) {
                                  target = playersInRoom.get(i);
                                  if(target.hasRole() && !target.hasSceneRole()) {
                                    System.out.format("Player %s receives %d dollars\n", target.getName(), target.getRole().getRank());
                                    modifyMoney(target.getRole().getRank(), 0, target);
                                  }
                                }
                              }

                              // remove players from their roles
                              for(int i = 0; i < playersInRoom.size(); i++) {
                                playersInRoom.get(i).leaveRole();
                              }
                              current.wrapScene();
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
                              System.out.format("Player %s now has %d chips (scene budget: %d)\n", currPlayer.getName(), currPlayer.getChips(), currPlayer.getRoom().getSceneDifficulty());
                              turnContinue = false;
                            }
                            else {
                              System.out.println("Player does not have a role or has too many chips");
                            }
                            break;

          case "upgrade": if(lineScan.hasNext()) {
                            arg = lineScan.next();
                            int target = Integer.parseInt(arg);
                            if(currPlayer.getRoom().getName() == "office") {
                              if(target > 6 || target < 2) {
                                System.out.println("You can't upgrade to that level (min 2, max 6)");
                                break;
                              }
                              if(currPlayer.getRank() >= target) {
                                System.out.format("Target rank %d is lower or equal to current rank %d\n", target, currPlayer.getRank());
                                break;
                              }
                              System.out.format("Rank %d will cost you %d dollars or %d credits\n", target, upgrades[0][target-2], upgrades[1][target-2]);
                              System.out.println("Press 1 to pay with dollars, 2 to pay with credits");
                              String payline = in.nextLine();
                              Scanner paymentLineScan = new Scanner(payline);
                              if(paymentLineScan.hasNext())
                              {
                                String paycommand = paymentLineScan.next();
                                int paymentMethod = Integer.parseInt(paycommand);
                                if(paymentMethod == 1)
                                {
                                  if(currPlayer.getDollars() >= upgrades[0][target-2]) {
                                    currPlayer.modifyDollars(-upgrades[0][target-2]);
                                    currPlayer.upgradeToRank(target);
                                  } else {
                                      System.out.println("You don't have enough dollars to upgrade");
                                  }
                                } else {
                                    if(currPlayer.getCredits() >= upgrades[1][target-2]) {
                                      currPlayer.modifyCredits(-upgrades[1][target-2]);
                                      currPlayer.upgradeToRank(target);
                                  } else {
                                      System.out.println("You don't have enough credits to upgrade");
                                  }
                                }
                              } else {
                                // No upgrade value provided
                                  System.out.println("Please enter a payment type (1 for dollars, 2 for credits)");
                              }
                            } else {
                                System.out.println("You must be at the casting office to upgrade");
                              }
                            }
                            else {
                              System.out.println("Command requires additional arguments");
                            }
                            break;

          // cheats
          case "deactivate":  currPlayer.getRoom().wrapScene();
                              turnContinue = false;
                              break;

          case "money": modifyMoney(100, 100, currPlayer);
                        break;

          case "end":       turnContinue = false;
                            break;

          case "moveForce": if(lineScan.hasNext()) {
                              arg = lineScan.next();
                              while(lineScan.hasNext()) {
                                arg += " " + lineScan.next();
                              }
                              forceMovePlayer(currPlayer, arg);
                            }
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
    private boolean movePlayer(Player currPlayer, String roomStr) {
      Room currRoom = currPlayer.getRoom();
      Room target = getRoomByName(roomStr);
      if(target == null) {
        System.out.format("Error: Room \'%s\' not found\n", roomStr);
        return false;
      }

      if(currRoom.checkIfNeighbor(roomStr)) {
        System.out.format("Moving Player %s from %s to %s\n", currPlayer.getName(), currRoom.getName(), roomStr);
        currPlayer.moveTo(target);
      }
      else {
        System.out.println("Not a neighbor. Try again.");
        return false;
      }

      return true;
    }

    private void forceMovePlayer(Player currPlayer, String roomStr) {
      Room target = getRoomByName(roomStr);
      if(target == null) {
        System.out.format("Error: Room \'%s\' not found\n", roomStr);
        return;
      }

      currPlayer.moveTo(target);
      return;
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

    private void modifyMoney(int dollars, int credits, Player target) {
      target.modifyDollars(dollars);
      target.modifyCredits(credits);
      return;
    }

    private List<Integer> generatePayout(int budget) {
      List<Integer> ret = new ArrayList<Integer>();
      for(int i = 0; i < budget; i++) {
        ret.add((int) (Math.random() * 6) + 1);
      }

      Collections.sort(ret, Collections.reverseOrder());
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

    private Player getPlayerInRole(Role target, List<Player> roomPlayers) {
      for(int i = 0; i < roomPlayers.size(); i++) {
        Player curr = roomPlayers.get(i);
        if(curr.hasRole()) {
          if(curr.getRole().getName().equals(target.getName())) {
            return curr;
          }
        }
      }
      return null;
    }
}

import java.util.*;
import java.util.Scanner;

public class InputManager {
  private static InputManager instance = null;
  private Scanner in;

  private InputManager() {
    in = new Scanner(System.in);
  }

  public static InputManager getInstance() {
    if(instance == null) {
      instance = new InputManager();
    }

    return instance;
  }

  public String[] getPlayers(int totalPlayers) {
    String[] playerNames = new String[totalPlayers];
    for(int i = 0; i < totalPlayers; i++) {
      System.out.format("Enter Player %d name: ", i + 1);
      playerNames[i] = in.next();
      in.nextLine();
    }

    return playerNames;
  }

  public void takeTurn(Player currPlayer, Board currBoard) {
    String line = "";
    String[] args = null;
    boolean turnContinue = true;
    boolean hasMoved = false;
    Room currPlayerRoom = currPlayer.getRoom();

    while(turnContinue) {
      System.out.format("\n%s enter move: ", currPlayer.getName());
      line = in.nextLine();
      args = line.split("\\s+");

      if(args != null && args.length > 0) {
        switch(args[0]) {
          case "help":
            printHelp();
            break;

          case "move":
            if(args.length > 1) {
              if(!hasMoved && !currPlayer.hasRole()) {
                String targetRoomName = args[1];
                if(args.length > 2) {
                  for(int i = 2; i < args.length; i++) {
                    targetRoomName += " " + args[i];
                  }
                }

                hasMoved = currBoard.movePlayer(currPlayer, targetRoomName);
                currPlayerRoom = currPlayer.getRoom();
              }
              else {
                System.out.println("Player has already moved or currently has a role");
              }
            }
            else {
              System.out.println("Command requires additional arguments");
            }
            break;

          case "list":
            if(args.length > 1) {
              switch(args[1]) {
                case "rooms":
                  Room[] boardRooms = currBoard.getRooms();
                  for(Room currRoom : boardRooms) {
                    System.out.format("%s", currRoom.getName());
                    if(!currRoom.getName().equals("office") && !currRoom.getName().equals("trailer")) {
                      if(currRoom.getSceneActive()) {
                        System.out.println(" (active)");
                      }
                      else {
                        System.out.println(" (inactive)");
                      }
                    }
                    else {
                      System.out.println();
                    }
                  }
                  break;

                case "neighbors":
                  currPlayerRoom.listNeighbors();
                  break;

                case "upgrades":
                  currBoard.listUpgrades();
                  break;

                default:
                  System.out.format("Invalid arg entered: \'%s\'\n", args[1]);
                  break;
              }
            }
            else {
              System.out.println("Command requires additional arguments");
            }
            break;

          case "desc":
            if(args.length > 1) {
              switch(args[1]) {
                case "room":
                  if(args.length > 2) {
                    String targetRoomName = "";
                    if(args.length > 3) {
                      targetRoomName = args[2] + " " + args[3];
                    }
                    else {
                      targetRoomName = args[2];
                    }
                    Room targetRoom = currBoard.getRoomByName(targetRoomName);
                    if(targetRoom == null) {
                      System.out.format("Room \'%s\' not found\n", targetRoomName);
                    }
                    else {
                      System.out.print(targetRoom);
                    }
                  }
                  else {
                    System.out.print(currPlayerRoom);
                  }
                  break;

                case "scene":
                  Scene currScene = currPlayerRoom.getScene();
                  if(currScene == null || !currScene.getActive()) {
                    System.out.println("Room does not have a scene or scene wrapped");
                  }
                  else {
                    System.out.print(currScene);
                  }
                  break;

                case "player":
                  if(args.length > 2) {
                    String targetPlayerName = args[2];
                    for(int i = 3; i < args.length; i++) {
                      targetPlayerName += " " + args[i];
                    }
                    Player targetPlayer = currBoard.getPlayerByName(targetPlayerName);
                    if(targetPlayer == null) {
                      System.out.format("Unrecognized player \'%s\'\n", targetPlayerName);
                    }
                    else {
                      System.out.print(targetPlayer);
                    }
                  }
                  else {
                    System.out.print(currPlayer);
                  }
                  break;

                default:
                  System.out.format("Invalid arg entered: \'%s\'\n", args[1]);
                  break;
              }
            }
            else {
              System.out.println("Command requires additional arguments");
            }
            break;

          case "take":
            if(args.length > 1) {
              if(args[1].equals("role")) {
                if(args.length > 2) {
                  String targetRoleName = args[2];
                  if(args.length > 3) {
                    for(int i = 3; i < args.length; i++) {
                      targetRoleName += " " + args[i];
                    }
                  }

                  turnContinue = currBoard.playerTakeRole(currPlayer, targetRoleName);
                }
                else {
                  System.out.println("Command requires additional arguments");
                }
              }
              else {
                System.out.format("Invalid arg entered: \'%s\'\n", args[1]);
              }
            }
            else {
              System.out.println("Command requires additional arguments");
            }
            break;

          case "act":
            turnContinue = currBoard.playerAct(currPlayer);
            break;

          case "rehearse":
            turnContinue = currBoard.playerRehearse(currPlayer);
            break;

          case "upgrade":
            if(currPlayer.getRoom().getName().equals("office")) {
              if(args.length > 1) {
                int targetRank = Integer.parseInt(args[1]);
                if(args.length > 2) {
                  int moneyType = Integer.parseInt(args[2]);
                  currBoard.playerUpgrade(currPlayer, targetRank, moneyType);
                }
                else {
                  System.out.println("Command requires additional arguments");
                }
              }
              else {
                System.out.println("Command requires additional arguments");
              }
            }
            else {
              System.out.println("You must be at the casting office to upgrade");
            }
            break;

          case "end":
            turnContinue = false;
            break;

          // cheats
          case "deactivate":
            currPlayer.getRoom().wrapScene();
            turnContinue = false;
            break;

          case "money":
            currBoard.modifyMoney(100, 100, currPlayer);
            break;

          case "moveForce":
            if(args.length > 1) {
              String targetRoomName = args[1];
              if(args.length > 2) {
                for(int i = 2; i < args.length; i++) {
                  targetRoomName += " " + args[i];
                }
              }

              currBoard.forceMovePlayer(currPlayer, targetRoomName);
              currPlayerRoom = currPlayer.getRoom();
            }
            break;

          case "endDay":
            currBoard.forceEndDay();
            turnContinue = false;
            break;

          default:
            System.out.format("Invalid command entered: %s\n", line);
            break;
        }
      }
    }

    return;
  }

  private void printHelp() {
    System.out.println("Instructions:");
    System.out.println("- move room_name: Moves active player to room specified by room_name");
    System.out.println("- list rooms: Prints all rooms");
    System.out.println("- list neighbors: Prints all neighboring rooms");
    System.out.println("- desc room [room_name]: Prints out attributes of the room specified by room_name (defaults to current room)");
    System.out.println("- desc scene: Prints out attributes of the current room's scene");
    System.out.println("- desc player [player_name]: Prints out attributes of the player specified by player_name (defaults to active player)");
    System.out.println("- take role role_name: Takes the role specified by role_name");
    System.out.println("- act: Acts under the current role");
    System.out.println("- rehearse: Rehearses under the current role");
    System.out.println("- upgrade rank_num money_type: Upgrades the player's rank to rank_num and deducts the appropriate amount of currency:\n\t0 to indicate dollars, 1 to indicate credits");
    System.out.println("- end: ends the active player's turn");

    return;
  }
}

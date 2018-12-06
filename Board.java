import java.util.*;
import java.util.Scanner;
import java.lang.Math;
import javax.swing.JTextArea;

public class Board {
    private int totalPlayers, activePlayerIndex, currentDay, totalDays;
    private List<Player> players;
    private Room[] rooms;
    private List<Scene> scenes;
    private int[][] upgrades;
    private InputManager inputManager;
    private boolean activeGame;
    private JTextArea printer;
    private BoardViewManager view;

    public Board(int numPlayers, Room[] roomArr, List<Scene> sceneArr, int[][] upgradeCosts) {
      totalPlayers = numPlayers;
      players = new ArrayList<Player>(numPlayers);
      rooms = roomArr;
      currentDay = 0;
      totalDays = 3;
      scenes = sceneArr;
      upgrades = upgradeCosts;
      activeGame = false;
      view = BoardViewManager.getInstance();
    }

    /* function runGame
       purpose: runs the initilized Deadwood game, part of the public interface
    */
    public void runGame() {
      /*
      while(true) {
        runDay();
        newDay();
        if(currentDay > totalDays) {
          endGame();
          return;
        }
      }
      */
    }

    /* function runDay
       purpose: handles running each day
    */
    private void runDay() {
      //System.out.format("\nDay %d\n", currentDay);
      view.print(String.format("\nDay %d\n", currentDay));
      boolean running = true;
      while(running) {
        //inputManager.takeTurn(getCurrentPlayer(), this);
        activePlayerIndex = (activePlayerIndex + 1) % totalPlayers;
        running = checkDayCont();
      }
    }

    public void forceEndDay() {
      Room currentRoom;
      for(int i = 0; i < rooms.length; i++) {
        currentRoom = rooms[i];
        if(!currentRoom.getName().equals("trailer") && !currentRoom.getName().equals("office")) {
          currentRoom.wrapScene();
        }
      }
    }

    /* function newDay
       purpose: handles the creation of new days
    */
    private void newDay() {
      currentDay++;
      view.print(String.format("\nYour turn, %s\n", getCurrentPlayer().getName()));

      // move all players to the starting trailer room
      for(int i = 0; i < totalPlayers; i++) {
        forceMovePlayer(players.get(i), "trailer");
        players.get(i).leaveRole();
      }

      // assign scenes to rooms
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
              notAssigned = false;
            }
          }
        }
      }
      return;
    }

    /* function createGame
       purpose: handles creating the game, part of the public interface
    */
    public void createGame() {
      //String[] playerNames = inputManager.getPlayers(totalPlayers);
      String[] playerNames = view.getPlayerNames();
      for(int i = 0; i < totalPlayers; i++) {
        players.add(new Player(playerNames[i]));
      }
      activeGame = true;
      newDay();
      activePlayerIndex = 0;
      return;
    }

    /* function endGame
       purpose: handles the gameover event for Deadwood
    */
    private void endGame() {
      int maxScore = 0;
      Player winner = null;
      for(int i = 0; i < players.size(); i++) {
        int score = calcScore(players.get(i));
        //System.out.format("%s has %d points\n", players.get(i).getName(), score);
        view.print(String.format("%s has %d points\n", players.get(i).getName(), score));
        if(maxScore < score) {
          maxScore = score;
          winner = players.get(i);
        }
      }
      //System.out.format("\n%s won with %d points\n", winner.getName(), maxScore);
      view.print(String.format("\n%s won with %d points\n", winner.getName(), maxScore));
      //System.out.println("Gameover, thank you for playing Deadwood!");
      view.println("Gameover, thank you for playing Deadwood!");
      activeGame = false;
      return;
    }

    /* function calcScore
       purpose: calculates the final score for a given Player, following the score formula
       parameters: target, Player to find the score for
       returns: calculated score
    */
    private int calcScore(Player target) {
      return target.getDollars() + target.getCredits() + (target.getRank() * 5);
    }

    public List<Player> getPlayers() {
      return players;
    }

    public Room[] getRooms() {
    return rooms;
  }

    public boolean isActiveGame() { return activeGame; }

    public Player getCurrentPlayer() {
    return players.get(activePlayerIndex);
  }

    public void setCurrentPlayer(Player p) {
      activePlayerIndex = players.indexOf(p);
    }

    public void nextTurn() {
      activePlayerIndex = (activePlayerIndex + 1) % totalPlayers;
      view.print(String.format("\nYour turn, %s\n", getCurrentPlayer().getName()));
    }

    public int getCurrentPlayerIndex() { return activePlayerIndex; }

    /* function movePlayer
       purpose: attempts to move a Player to a given room
       parameters: currPlayer, Player object to move
                   roomStr, name of the target Room
       returns: true if successful, false otherwise
    */
    public boolean movePlayer(Player targetPlayer, String roomStr) {
      Room currRoom = targetPlayer.getRoom();
      Room targetRoom = getRoomByName(roomStr);
      if(targetRoom == null) {
        System.out.format("Error: Room \'%s\' not found\n", roomStr);
        return false;
      }

      if(currRoom.checkIfNeighbor(roomStr)) {
        //System.out.format("Moving Player %s from %s to %s\n", targetPlayer.getName(), currRoom.getName(), roomStr);
        view.print(String.format("Moving Player %s from %s to %s\n", targetPlayer.getName(), currRoom.getName(), roomStr));
        targetPlayer.moveTo(targetRoom);
      }
      else {
        //System.out.println("Not a neighbor. Try again.");
        view.println("Not a neighbor. Try again.");
        return false;
      }

      return true;
    }

    /* function forceMovePlayer
       purpose: forcefully moves a Player to a given room. used for testing purposes only
       parameters: currPlayer, Player object to move
                   roomStr, name of the target Room
    */
    public void forceMovePlayer(Player currPlayer, String roomStr) {
      Room targetRoom = getRoomByName(roomStr);
      if(targetRoom == null) {
        System.out.format("Error: Room \'%s\' not found\n", roomStr);
        return;
      }

      currPlayer.moveTo(targetRoom);
      return;
    }

    /* function getRoomByName
       purpose: finds a Room, given its name
       parameters: name, string name of the target room
       returns: Room if found, null otherwise
    */
    public Room getRoomByName(String name) {
        for(int i = 0; i < rooms.length; i++) {
            if(rooms[i].getName().equals(name)) {
                return rooms[i];
            }
        }
        return null;
    }

    /* function getRoomIndexByName
       purpose: finds the index of a Room, given its name
       parameters: name, string name of the target room
       returns: Index of the room if found, null otherwise
    */
    public int getRoomIndexByName(String name) {
        int i;
        for(i = 0; i < rooms.length; i++) {
            if(rooms[i].getName().equals(name)) {
                return i;
            }
        }
        return 0;
    }

    /* function checkDayCont
       purpose: checks if the day should continue or end
    */
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

    /* function modifyMoney
       purpose: dual-purpose function to modify both the dollar and credits of a given player
       parameters: dollars, number of dollars to add or subtract
                   credits, number of credits to add or subtract
                   target, Player to modify
    */
    public void modifyMoney(int dollars, int credits, Player target) {
      target.modifyDollars(dollars);
      target.modifyCredits(credits);
      return;
    }

    /* function generatePayout
       purpose: generates an Integer List of dice rolls, sorted in descending order
       parameters: budget, the length of the requested list
       returns: Integer List
    */
    private List<Integer> generatePayout(int budget) {
      List<Integer> ret = new ArrayList<Integer>();
      for(int i = 0; i < budget; i++) {
        ret.add((int) (Math.random() * 6) + 1);
      }

      Collections.sort(ret, Collections.reverseOrder());
      return ret;
    }

    /* function getPlayersInRoom
       purpose: generates a list of all players in a given room
       parameters: target, the Room to check within
       returns: List of Players in the room
    */
    public List<Player> getPlayersInRoom(Room target) {
      List<Player> ret = new ArrayList<Player>();
      String name = target.getName();
      for(int i = 0; i < players.size(); i++) {
        if(players.get(i).getRoom().getName().equals(name)) {
          ret.add(players.get(i));
        }
      }
      return ret;
    }

    /* function getPlayerByName
       purpose: finds a player given its name
       parameters: target, the name of the desired Player
       returns: Player that corresponds to the given name, null if not found
    */
    public Player getPlayerByName(String target) {
      for(int i = 0; i < players.size(); i++) {
        if(players.get(i).getName().equals(target)) {
          return players.get(i);
        }
      }
      return null;
    }

    /* function getPlayerInRole
       purpose: finds the player that has taken the given role, if any
       parameters: target, the given Role to check
                   roomPlayers, a list of Players that are in the same room as the Role
       returns: Player that has the role, null if not found
    */
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

    public void listUpgrades() {
      for(int i = 0; i < upgrades[0].length; i++) {
        System.out.format("rank %d: %d dollars, %d credits\n", i + 2, upgrades[0][i], upgrades[1][i]);
      }

      return;
    }

    public boolean playerTakeRole(Player currPlayer, String targetRoleName) {
      Room currPlayerRoom = currPlayer.getRoom();
      Role targetRole = currPlayerRoom.getRoleByName(targetRoleName);
      if(targetRole == null) {
        System.out.format("Unrecognized role: \'%s\'\n", targetRoleName);
        return true;
      }

      if(!currPlayerRoom.getSceneActive()) {
        //System.out.println("Current room does not have an active scene");
        view.println("Room does not have an active scene");
        return true;
      }

      if(targetRole.isTaken()) {
        //System.out.println("Requested role is already taken");
        view.println("Requested role is already taken");
        return true;
      }

      if(targetRole.getRank() > currPlayer.getRank()) {
        //System.out.format("Player not high enough level for requested role (required: %d, player rank: %d)\n",
          //targetRole.getRank(), currPlayer.getRank());
        view.println("Player rank not high enough");
        return true;
      }

      currPlayer.takeRole(targetRole);
      //System.out.format("%s took the role \'%s\'\n", currPlayer.getName(), targetRoleName);
      view.print(String.format("%s took the role \'%s\'\n", currPlayer.getName(), targetRoleName));
      return false;
    }

    public boolean playerAct(Player currPlayer) {
      int result = 0;
      int roll = 0;
      int budget = 0;
      Room currentRoom = currPlayer.getRoom();
      if(currPlayer.hasRole()) {
        budget = currentRoom.getSceneDifficulty();
        roll = currPlayer.act();
        System.out.format("Acting on role \'%s\', budget %d\n", currPlayer.getRole().getName(), budget);
        result = currentRoom.shootScene(roll);
        if(result >= 0) {
          System.out.print("Roll successful, ");
          if(currPlayer.hasSceneRole()) {
            System.out.format("%s earns 2 credits\n", currPlayer.getName());
            modifyMoney(0, 2, currPlayer);
          }
          else {
            System.out.format("%s earns 1 credit and 1 dollar\n", currPlayer.getName());
            modifyMoney(1, 1, currPlayer);
          }

          if(result == 0) {
            System.out.println("All shots completed, scene wrap");

            List<Player> playersInRoom = getPlayersInRoom(currentRoom);

            // payout bonuses to players in roles
            if(currentRoom.checkIfSceneRolesTaken()) {

              // payout bonuses to players in scene roles
              List<Integer> bonuses = generatePayout(budget);
              List<Role> sceneRoles = currentRoom.getSceneRoles();
              int roleIndex = sceneRoles.size() - 1;
              Player targetPlayer;
              System.out.format("Bonus dice rolled: %s\n", bonuses.toString());
              for(int i = 0; i < bonuses.size(); i++) {
                if(sceneRoles.get(roleIndex).isTaken()) {
                  targetPlayer = getPlayerInRole(sceneRoles.get(roleIndex), playersInRoom);
                  System.out.format("%s receives %d dollars\n", targetPlayer.getName(), bonuses.get(i));
                  modifyMoney(bonuses.get(i), 0, targetPlayer);
                }
                roleIndex--;
                if(roleIndex < 0) {
                  roleIndex = sceneRoles.size() - 1;
                }
              }

              // payout bonuses to players in room roles
              for(int i = 0; i < playersInRoom.size(); i++) {
                targetPlayer = playersInRoom.get(i);
                if(targetPlayer.hasRole() && !targetPlayer.hasSceneRole()) {
                  System.out.format("%s receives %d dollars\n", targetPlayer.getName(), targetPlayer.getRole().getRank());
                  modifyMoney(targetPlayer.getRole().getRank(), 0, targetPlayer);
                }
              }
            }

            // remove players from their roles
            for(int i = 0; i < playersInRoom.size(); i++) {
              playersInRoom.get(i).leaveRole();
            }
            currentRoom.wrapScene();
          }
        }
        else if(!currPlayer.hasSceneRole()) {
          System.out.format("Unsuccessful roll, %s earns 1 dollar\n", currPlayer.getName());
          modifyMoney(1, 0, currPlayer);
        }
        else {
          System.out.println("Unsuccessful roll");
        }

        return false;
      }

      //System.out.println("Player does not have a role");
      view.println("Player does not have a role");
      return true;
    }

    public boolean playerRehearse(Player currPlayer) {
      if(currPlayer.hasRole() && currPlayer.getChips() < currPlayer.getRoom().getSceneDifficulty()) {
        currPlayer.rehearse();
        System.out.format("%s now has %d chips (scene budget: %d)\n", currPlayer.getName(), currPlayer.getChips(), currPlayer.getRoom().getSceneDifficulty());
        return false;
      }

      //System.out.println("Player does not have a role or has too many chips");
      view.println("Player does not have a role or has too many chips");
      return true;
    }

    public void playerUpgrade(Player currPlayer, int targetRank, int moneyType) {
      if(currPlayer.getRoom().getName().equals("office")) {
        if(targetRank > 6 || targetRank < 2) {
          System.out.println("Player can't upgrade to that level (min 2, max 6)");
          return;
        }

        if(currPlayer.getRank() >= targetRank) {
          System.out.format("Target rank %d is lower or equal to current rank %d\n", targetRank, currPlayer.getRank());
          return;
        }

        if(moneyType == 0) {
          if(currPlayer.getDollars() >= upgrades[0][targetRank-2]) {
            currPlayer.modifyDollars(-upgrades[0][targetRank-2]);
            System.out.format("Upgrading %s from rank %d to rank %d, deducting %d dollars\n", currPlayer.getName(), currPlayer.getRank(), targetRank, upgrades[0][targetRank-2]);
            currPlayer.upgradeToRank(targetRank);
          }
          else {
            System.out.println("Player does not have enough dollars to upgrade");
          }
        }
        else if(moneyType == 1) {
          if(currPlayer.getCredits() >= upgrades[1][targetRank-2]) {
            currPlayer.modifyCredits(-upgrades[1][targetRank-2]);
            System.out.format("Upgrading %s from rank %d to rank %d, deducting %d credits\n", currPlayer.getName(), currPlayer.getRank(), targetRank, upgrades[1][targetRank-2]);
            currPlayer.upgradeToRank(targetRank);
          }
          else {
            System.out.println("Player does not have enough credits to upgrade");
          }
        }
        else {
          System.out.format("Invalid money type \'%d\' entered\n", moneyType);
        }
      }
      else {
        //System.out.println("Player is not in the Casting Office");
        view.println("Player is not in the Casting Office");
      }
      return;
    }
}

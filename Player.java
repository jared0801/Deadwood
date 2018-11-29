import java.lang.Math;

public class Player {
    private String name;
    private int dollars, credits, rank, chips;
    private boolean hasRole;
    private Role currentRole;
    private Room currentRoom;

    public Player (String newName) {
      name = newName;
      dollars = 0;
      credits = 0;
      rank = 1;
      hasRole = false;
      currentRole = null;
      currentRoom = null;
      chips = 0;
    }

    public String getName() {
      return name;
    }

    private int rollDice(int numDice) {
      int roll = 0;
      for(int i = 0; i < numDice; i++) {
        roll += (int) (Math.random() * 6) + 1;
      }
      return roll;
    }

    public int getRank() {
      return rank;
    }

    public void takeRole(Role newRole) {
      currentRole = newRole;
      currentRole.take();
      hasRole = true;
      chips = 0;
      return;
    }

    public boolean hasRole() {
      return hasRole;
    }

    public void leaveRole() {
      if(currentRole != null) {
        currentRole.leave();
        currentRole = null;
        hasRole = false;
      }
      return;
    }

    public boolean hasSceneRole() {
      if(hasRole) {
        if(currentRole.isOnScene()) {
          return true;
        }
      }
      return false;
    }

    public Role getRole() {
      return currentRole;
    }

    public Room getRoom() {
      return currentRoom;
    }

    public int act() {
      int rollAmount = rollDice(1);
      int total = rollAmount + chips;
      System.out.format("Player rolled %d (%d + %d chips)\n", total, rollAmount, chips);
      return total;
    }

    public void rehearse() {
      chips++;
      return;
    }

    public void moveTo(Room newRoom) {
      currentRoom = newRoom;
      return;
    }

    public void upgradeToRank(int newRank) {
      rank = newRank;
      return;
    }

    public int modifyDollars(int amount) {
      return dollars += amount;
    }

    public int modifyCredits(int amount) {
      return credits += amount;
    }

    public int getDollars() {
      return dollars;
    }

    public int getCredits() {
      return credits;
    }

    public int getChips() {
      return chips;
    }

    public String toString() {
      String ret = String.format("name: %s\nrank: %d\ndollars: %d\ncredits: %d\nchips: %d\ncurrent room: %s\n", name, rank, dollars, credits, chips, currentRoom.getName());
      //String ret = "name: " + name + "\nrank: " + rank + "\ndollars: " + dollars + "\ncredits: " + credits + "\nchips: " + chips + "\n";
      if(hasRole) {
        ret += "role:\n" + currentRole;
      }
      return ret;
    }
}

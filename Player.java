import java.lang.Math;

public class Player {
    private String name;
    private int dollars, credits, rank, chips;
    private boolean hasRole;
    private Role currentRole;

    public Player (String newName) {
      name = newName;
      dollars = 0;
      credits = 0;
      rank = 1;
      hasRole = false;
      currentRole = null;
      chips = 0;
    }

    private int rollDice(int numDice) {
      int roll = 0;
      for(int i = 0; i < numDice; i++) {
        roll += (int) (Math.random() * 6) + 1;
      }
      return roll;
    }

    public void takeRole(Role newRole) {
      currentRole = newRole;
      chips = 0;
      return;
    }

    public boolean hasRole() {
      return hasRole;
    }

    public Role getRole() {
      return currentRole;
    }

    public int act() {
      int result = rollDice(1) + chips;
      return result;
    }

    public void rehearse() {
      chips++;
      return;
    }

    public void move() {
      return;
    }

    public void upgradeToRank(int newRank) {
      rank = newRank;
      return;
    }

    public void modifyDollars(int amount) {
      dollars += amount;
      return;
    }

    public void modifyCredits(int amount) {
      credits += amount;
      return;
    }

    public int getDollars() {
      return dollars;
    }

    public int getCredits() {
      return credits;
    }

    public String toString() {
      String ret = "name: " + name + "\nrank: " + rank + "\ndollars: " + dollars + "\ncredits: " + credits + "\nchips: " + chips + "\n";
      if(hasRole) {
        ret += "role: " + currentRole;
      }
      return ret;
    }
}

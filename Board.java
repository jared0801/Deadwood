import java.util.*;
import java.util.Scanner;

public class Board {
    private int totalPlayers, activePlayerIndex, currentDay, totalDays;
    private List<Player> players;
    private Room[] rooms;

    public Board(int numPlayers) {
      totalPlayers = numPlayers;
      players = new ArrayList<Player>(numPlayers);
      currentDay = 0;
      totalDays = 3;
    }

    public void newDay() {
      currentDay++;
      if(currentDay > totalDays) {
        endGame();
      }
      return;
    }

    public void createGame() {
      Scanner reader = new Scanner(System.in);
      for(int i = 0; i < totalPlayers; i++) {
        System.out.format("Enter Player %d name: ", i + 1);
        String name = reader.next();
        players.add(new Player(name));
      }

      newDay();
      activePlayerIndex = 0;
      reader.close();
      return;
    }

    public void endGame() {
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

    public void modifyMoney(int dollars, int credits, Player target) {
      target.modifyDollars(dollars);
      target.modifyCredits(credits);
      return;
    }
}

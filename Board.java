import java.util.*;

public class Board {
    private int totalPlayers, activePlayerIndex, currentDay, totalDays;
    private List<Player> players;
    private Room[] rooms;

    public Board(int numPlayers) {
	totalPlayers = numPlayers;
	players = new ArrayList<Player>(numPlayers);
    }

    public void newDay() {
	return;
    }

    public void createGame() {
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

    public void payoutMoney(int dollars, int credits, Player target) {
	return;
    }

    public void takeMoney(int dollars, int credits, Player target) {
	return;
    }
}

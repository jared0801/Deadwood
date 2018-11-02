import java.lang.Object;

public class Board {
    private int totalPlayers, activePlayerIndex, currentDay, totalDays;
    private ArrayList<Player> players;
    private Room[] rooms;

    public Board(int numPlayers) {
	totalPlayers = numPlayers;
	players = new List<Player>(numPlayers);
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

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Room> getRooms() {
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

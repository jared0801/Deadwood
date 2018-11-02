import java.lang.Object;

public class Board {
    private int totalPlayers, activePlayerIndex, currentDay, totalDays;
    private ArrayList<Player> players;
    private Room[] rooms;

    public void newDay();

    public void createGame();

    public void endGame();

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public ArrayList<Room> getRooms() {
        return rooms;
    }

    public Player getCurrentPlayer() {
        return players.get(activePlayerIndex);
    }

    public void payoutMoney(int dollars, int credits, Player target);

    public void takeMoney(int dollars, int credits, Player target);
}

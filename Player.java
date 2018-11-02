public class Player {
    private String name;
    private int dollars, credits, rank;
    private boolean hasRole;
    private Role currentRole;

    public Player (String newName) {
	name = newName;
	dollars = 0;
	credits = 0;
	rank = 1;
	hasRole = false;
	currentRole = null;
    }

    private int rollDice(int numDice) {
	return 0;
    }

    public void takeRole() {
	return;
    }

    public boolean hasRole() {
        return hasRole;
    }

    public void act() {
	return;
    }

    public void rehearse() {
	return;
    }

    public void move() {
	return;
    }

    public void upgrade() {
	return;
    }

    public void modifyDollars(int amount) {
	return;
    }

    public void modifyCredits(int amount) {
	return;
    }

    public int getDollars() {
	return dollars;
    }

    public int getCredits() {
	return credits;
    }
}

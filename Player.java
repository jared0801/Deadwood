public class Player {
    private String name;
    private int money, credits, rank;
    private boolean hasRole;
    private Role currentRole;

    private int rollDice(int numDice);

    public void takeRole();

    public boolean hasRole() {
        return hasRole;
    }

    public void act();

    public void rehearse();

    public void move();

    public void upgrade();

    public void modifyDollars(int amount);

    public void modifyCredits(int amount);

    public int getDollars();

    public int getCredits();
}
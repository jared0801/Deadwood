public class Role {
    private String name;
    private int reqRank;
    private boolean isTaken;

    public int getRank() {
	return 0;
    }

    public boolean checkTaken() {
	return true;
    }

    public void take() {
	return;
    }
}

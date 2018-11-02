public class Role {
    private String name;
    private int reqRank;
    private boolean isTaken;

    public Role(String newName, int rank) {
	name = newName;
	reqRank = rank;
	isTaken = false;
    }
    
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

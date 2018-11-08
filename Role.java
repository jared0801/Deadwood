public class Role {
    private String name;
    private String line;
    private int reqRank;
    private boolean isTaken;

    public Role(String newName, int rank, String newLine) {
        name = newName;
        reqRank = rank;
        isTaken = false;
        line = newLine;
    }

    public String toString() {
        return ("name: " + name + "\nrank: " + reqRank + "\nline: " + line);
    }

    public int getRank() {
	return reqRank;
    }

    public String getLine() {
        return line;
    }

    public boolean checkTaken() {
	return isTaken;
    }

    public void take() {
	return;
    }
}

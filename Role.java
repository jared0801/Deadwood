public class Role {
    private String name;
    private String line;
    private int reqRank;
    private boolean isTaken;
    private boolean onScene;

    public Role(String newName, int rank, String newLine, boolean cardStatus) {
      name = newName;
      reqRank = rank;
      isTaken = false;
      onScene = cardStatus;
      line = newLine;
    }

    public String toString() {
      String ret = String.format("name: %s\nrank: %d\nline: %s\ntaken: %b\non scene: %b\n\n", name, reqRank, line, isTaken, onScene);
      //return "name: " + name + "\nrank: " + reqRank + "\nline: " + line + "\ntaken: " + isTaken;
      return ret;
    }

    public String getName() {
      return name;
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

    public boolean checkOnScene() {
      return onScene;
    }

    public void take() {
      isTaken = true;
      return;
    }

    public void leave() {
      isTaken = false;
      return;
    }
}

public class Role {
    private String name;
    private String line;
    private int reqRank;
    private boolean taken;
    private boolean onScene;

    public Role(String newName, int rank, String newLine, boolean cardStatus) {
      name = newName;
      reqRank = rank;
      taken = false;
      onScene = cardStatus;
      line = newLine;
    }

    public String toString() {
      String ret = String.format("name: %s\nrank: %d\nline: %s\ntaken: %b\non scene: %b\n\n", name, reqRank, line, taken, onScene);
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

    public boolean isTaken() {
      return taken;
    }

    public boolean isOnScene() {
      return onScene;
    }

    public void take() {
      taken = true;
      return;
    }

    public void leave() {
      taken = false;
      return;
    }
}

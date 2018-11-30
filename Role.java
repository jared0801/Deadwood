public class Role {
    private String name;
    private String line;
    private int reqRank;
    private boolean taken;
    private boolean onScene;
	 private int[] xy;
	 private int[] hw;

    public Role(String newName, int rank, String newLine, boolean cardStatus, int[] xyVal, int[] hwVal) {
      name = newName;
      reqRank = rank;
      taken = false;
      onScene = cardStatus;
      line = newLine;
		xy = xyVal;
		hw = hwVal;
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

	 public int[] getXy() {
		 return xy;
	 }

	 public int[] getHw() {
		 return hw;
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

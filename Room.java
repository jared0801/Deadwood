import java.util.*;

public class Room {
    private String name;
    private int shotCounter;
    private int currentShots;
    private Scene currentScene;
    private List<String> neighbors;
    private List<Role> offCardRoles;
    private int[] xy;
    private int[] hw;
    private int[][] takeXy;
    private int[][] takeHw;
    private boolean visited;

    public Room(String newName, int shots, List<String> neighborNames, List<Role> roles, int[] xyVal, int[] hwVal, int[][] takeXyVal, int[][] takeHwVal) {
      name = newName;
      shotCounter = shots;
      neighbors = neighborNames;
      offCardRoles = roles;
      xy = xyVal;
      hw = hwVal;
      takeXy = takeXyVal;
      takeHw = takeHwVal;
    }

    public Room(String newName, List<String> neighborNames, int[] xyVal, int[] hwVal) {
      name = newName;
      currentShots = 0;
      shotCounter = 0;
      neighbors = neighborNames;
      offCardRoles = null;
      xy = xyVal;
      hw = hwVal;
      takeXy = null;
      takeHw = null;
    }

    public String getName() {
      return name;
    }

    public int getShotCounter() {
        return shotCounter;
    }

    public int[] getXy() {
      return xy;
    }

    public int[] getHw() {
      return hw;
    }

    public int[][] getTakeXy() {
      return takeXy;
    }

    public int[][] getTakeHw() {
      return takeHw;
    }

    public void setScene(Scene newScene) {
      currentShots = 0;
      visited = false;
      currentScene = newScene;
      return;
    }

    public Scene getScene() {
      return currentScene;
    }

    public String getSceneImg() {
      return currentScene.getImgFile();
    }

    public boolean getVisited() {
      return visited;
    }

    public void visit() {
      visited = true;
      return;
    }

    public void resetVisited() {
      visited = false;
      return;
    }

    public Role getRoleByName(String target) {
      for(int i = 0; i < offCardRoles.size(); i++) {
        if(offCardRoles.get(i).getName().equals(target)) {
          return offCardRoles.get(i);
        }
      }

      List<Role> sceneRoles = getSceneRoles();
      for(int i = 0; i < sceneRoles.size(); i++) {
        if(sceneRoles.get(i).getName().equals(target)) {
          return sceneRoles.get(i);
        }
      }

      return null;
    }

    public List<Role> getSceneRoles() {
      return currentScene.getRoles();
    }

    public int getSceneDifficulty() {
      int diff = 0;
      if(getSceneActive()) {
        diff = currentScene.getBudget();
      }
      return diff;
    }

    public boolean checkIfSceneRolesTaken() {
      return currentScene.checkIfRolesTaken();
    }

    public List<String> getNeighbors() {
      return neighbors;
    }

    public void listNeighbors() {
      for(int i = 0; i < neighbors.size(); i++) {
        System.out.format("%s\n", neighbors.get(i));
      }
    }

    public boolean checkIfNeighbor(String name) {
      for(int i = 0; i < neighbors.size(); i++) {
        if(name.equals(neighbors.get(i))) {
          return true;
        }
      }
      return false;
    }

    public List<Role> getOffCardRoles() {
      return offCardRoles;
    }

    // increment the shot counter for the current scene.
    // returns the number of shots left or -1 on a fail
    public int shootScene(int roll) {
      int result = -1;
      if(roll >= getSceneDifficulty()) {
        currentShots++;
        result = shotCounter - currentShots;
      }
      return result;
    }

    public void wrapScene() {
      if(currentScene != null) {
        currentScene.wrap();
      }
    }

    public boolean getSceneActive() {
      return currentScene.getActive();
    }

    public int getCurrentShots() {
      return currentShots;
    }

    public String toString() {
      String ret = String.format("name: %s\ntotal shots: %d\ncurrent shots: %d\n\n", name, shotCounter, currentShots);
      if(currentScene != null) {
        if(getSceneActive()) {
          ret += "off-card roles:\n";
          for(int i = 0; i < offCardRoles.size(); i++) {
            ret += offCardRoles.get(i);
          }
          ret += "\ncurrent scene:\n";
          ret += currentScene;
        }
      }
      return ret;
    }
}

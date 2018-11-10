import java.util.*;

public class Room {
    private String name;
    private int shotCounter;
    private int currentShots;
    private Scene currentScene;
    private List<String> neighbors;
    private List<Role> offCardRoles;

    public Room(String newName, int shots, List<String> neighborNames, List<Role> roles) {
      name = newName;
      shotCounter = shots;
      neighbors = neighborNames;
      offCardRoles = roles;
    }

    public Room(String newName, List<String> neighborNames) {
      name = newName;
      shotCounter = 0;
      neighbors = neighborNames;
      offCardRoles = null;
    }

    public String getName() {
      return name;
    }

    public void setScene(Scene newScene) {
      currentScene = newScene;
      return;
    }

    public Scene getScene() {
      return currentScene;
    }

    public List<String> getNeighbors() {
      return neighbors;
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
    // returns the number of shots left
    public int shootScene() {
      currentShots++;
      return shotCounter - currentShots;
    }

    public void wrapScene() {
      currentScene.wrap();
    }

    public boolean getSceneActive() {
      return currentScene.getActive();
    }

    public int getCurrentShots() {
      return currentShots;
    }
}

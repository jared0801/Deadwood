public class Room {
    private String name;
    private int shotCounter;
    private int currentShots;
    private Scene currentScene;
    private Room[] neighborRooms;
    private Role[] offCardRoles;

    public Room(String newName, int shots) {
	name = newName;
	shotCounter = shots;
    }
    
    public void setScene(Scene newsScene) {
	return;
    }

    public Scene getScene() {
	return currentScene;
    }

    public Room[] getNeighbors() {
	return neighborRooms;
    }

    public Role[] getOffCardRoles() {
	return offCardRoles;
    }

    public int shootScene() {
	return 0;
    }

    public int getCurrentShots() {
	return currentShots;
    }
}

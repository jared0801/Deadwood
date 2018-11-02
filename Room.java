public class Room {
    private String name;
    private int shotCounter;
    private Scene currentScene;
    private Room[] neighborRooms;
    private Role[] offCardRoles;

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
}

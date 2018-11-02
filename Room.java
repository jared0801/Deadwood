public class Room {
    private String name;
    private int shotCounter;
    private Scene currentScene;
    private Room[] neighborRooms;
    private Role[] offCardRoles;

    public void setScene(Scene newsScene);

    public Scene getScene();

    public Room[] getNeighbors;

    public Role[] getOffCardRoles;

    public int shootScene();
}

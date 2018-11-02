public class Scene {
    private String name;
    private int budget;
    private boolean isActive;
    private Role[] roles;

    public Scene (String newName, int budg) {
	name = newName;
	budget = budg;
    }
    
    public int getBudget() {
	return 0;
    }

    public Role[] getRoles() {
	return null;
    }

    public boolean getActive() {
	return true;
    }

    public void wrap() {
	return;
    }
}

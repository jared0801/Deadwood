import java.util.*;

public class Scene {
    private String name;
    private String imgFile;
    private String description;
    private int number;
    private int budget;
    private boolean isActive;
    private List<Role> roles;

    public Scene (String newName, int budg, String img, List<Role> sceneRoles, int num, String desc) {
      name = newName;
      budget = budg;
      imgFile = img;
      roles = sceneRoles;
      number = num;
      description = desc;
      isActive = true;
    }

    public int getBudget() {
      return budget;
    }

    public String getName() {
      return name;
    }

    public List<Role> getRoles() {
      return roles;
    }

    public boolean checkIfRolesTaken() {
      for(int i = 0; i < roles.size(); i++) {
        if(roles.get(i).checkTaken()) {
          return true;
        }
      }
      return false;
    }

    public String getImgFile() {
      return imgFile;
    }

    public int getNum() {
      return number;
    }

    public boolean getActive() {
      return isActive;
    }

    public String getDesc() {
      return description;
    }

    public void wrap() {
      isActive = false;
      return;
    }

    public String toString() {
      String ret = String.format("name: %s\nbudget: %d\ndescription: %s\n", name, budget, description);
      //String ret = "name: " + name + "\nbudget: " + budget + "\nnumber: " + number + "\ndescription: " + description + "\n";
      for(int i = 0; i < roles.size(); i++) {
          ret += roles.get(i);
      }
      return ret;
     }
}

package shared;
import java.io.Serializable;

public class PlayerProfile implements Serializable {
    private String name;
    private int id;
    private String color;

    public PlayerProfile(String name, int id) {
        this.name = name;
        this.id = id;
        this.color = "FF0000";
    }
    public PlayerProfile(int id) {
        this.name = "name";
        this.id = id;
        this.color = "FF0000";
    }

    public String getColor() {
        return color;
    }
    public int getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Name:" + name + ", " + "ID: " + id;
    }
}


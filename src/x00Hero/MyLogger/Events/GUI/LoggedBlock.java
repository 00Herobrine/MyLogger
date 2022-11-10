package x00Hero.MyLogger.Events.GUI;

import org.bukkit.Material;

public class LoggedBlock {
    Material material;
    Integer light;
    String time, location;
    LoggedVein loggedVein;

    public LoggedBlock(Material material, Integer light, String time, String location) {
        this.material = material;
        this.light = light;
        this.time = time;
        this.location = location;
    }

    public void setLoggedVein(LoggedVein loggedVein) {
        this.loggedVein = loggedVein;
    }

    public LoggedVein getLoggedVein() {
        return loggedVein;
    }

    public Material getMaterial() {
        return material;
    }

    public Integer getLight() {
        return light;
    }

    public String getTime() {
        return time;
    }

    public String getLocation() {
        return location;
    }
}

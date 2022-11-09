package x00Hero.MyLogger;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public abstract class LoggingItem {
    boolean enabled;
    Material material;
    Location location;
    Player player;

    public Location getLocation() {
        return location;
    }
    public void setLocation(Location location) {
        this.location = location;
    }

    public Material getMaterial() {
        return material;
    }
    public void setMaterial(Material material) {
        this.material = material;
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
        this.player = player;
    }
}

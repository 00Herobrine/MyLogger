package x00Hero.MyLogger.Events.GUI;

import org.bukkit.inventory.ItemStack;

public class LoggedBlock {
    ItemStack itemStack;
    Integer light;
    String time, location;
    LoggedVein loggedVein;

    public LoggedBlock(ItemStack itemStack, Integer light, String time, String location) {
        this.itemStack = itemStack;
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

    public ItemStack getItemStack() {
        return itemStack;
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

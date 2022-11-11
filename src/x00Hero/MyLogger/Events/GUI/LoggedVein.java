package x00Hero.MyLogger.Events.GUI;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class LoggedVein {
    Material material;
    ItemStack itemStack;
    ArrayList<LoggedBlock> loggedBlocks = new ArrayList<>();

    public LoggedVein(Material material) {
        this.material = material;
    }

    public ArrayList<LoggedBlock> getLoggedBlocks() {
        return loggedBlocks;
    }

    public void setLoggedBlocks(ArrayList<LoggedBlock> loggedBlocks) {
        this.loggedBlocks = loggedBlocks;
    }

    public void addLoggedBlock(LoggedBlock loggedBlock) {
        loggedBlocks.add(loggedBlock);
    }
}

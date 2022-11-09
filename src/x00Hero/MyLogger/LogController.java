package x00Hero.MyLogger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import x00Hero.MyLogger.Events.OreLogEvent;

import java.util.ArrayList;
import java.util.UUID;

public class LogController {
    public static ArrayList<UUID> modAlerts = new ArrayList<>();

    @EventHandler
    public void OreLogEvent(OreLogEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        boolean alert = player.hasPermission("mylogger.alert." + block.getType());
        if(alert) alertMods(e);
    }

    public ArrayList<UUID> getModAlerts() {
        return modAlerts;
    }

    public static void setModAlerts(ArrayList<UUID> newModAlerts) {
        modAlerts = newModAlerts;
    }

    public static void addMod(Player p) {
        modAlerts.add(p.getUniqueId());
    }

    public void alertMods(OreLogEvent e) {
        for(UUID uuid : getModAlerts()) {
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) continue;
            Player miner = e.getPlayer();
            Location location = e.getBlock().getLocation();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            player.sendMessage(miner.getDisplayName() + " has mined " + e.getBlock().getType() + " at " + x + ", " + y + ", " + z);
        }
    }

}

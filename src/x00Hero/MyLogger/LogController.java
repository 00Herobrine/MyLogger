package x00Hero.MyLogger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import x00Hero.MyLogger.Chat.ChatController;

import java.util.ArrayList;
import java.util.UUID;

public class LogController {
    public static ArrayList<UUID> modAlerts = new ArrayList<>();

    public static ArrayList<UUID> getModAlerts() {
        return modAlerts;
    }

    public static void setModAlerts(ArrayList<UUID> newModAlerts) {
        modAlerts = newModAlerts;
    }

    public static void addMod(Player p) {
        modAlerts.add(p.getUniqueId());
    }

    public static void removeMod(Player p) {
        modAlerts.remove(p.getUniqueId());
    }

    public static void alertMods(Player miner, Block block) {
        if(getModAlerts() == null) return;
        for(UUID uuid : getModAlerts()) {
            Player player = Bukkit.getPlayer(uuid);
            if(player == null) continue;
            Location location = block.getLocation();
            int x = location.getBlockX();
            int y = location.getBlockY();
            int z = location.getBlockZ();
            String message = ChatController.getMessage(12);
            String locString = x + ", " + y + ", " + z;
            String oreColor = "&f";
            if(ChatController.hasOreColor(block.getType())) oreColor = ChatController.getOreColor(block.getType());
            message = message.replace("{player}", miner.getDisplayName()).replace("{blocktype}", block.getType().toString()).replace("{xyz}", locString).replace("{color}", ChatController.colorize(oreColor));
            player.sendMessage(message);
        }
    }

}

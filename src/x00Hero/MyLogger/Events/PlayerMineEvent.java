package x00Hero.MyLogger.Events;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import x00Hero.MyLogger.Events.GUI.LoggedVein;
import x00Hero.MyLogger.File.PlayerFile;
import x00Hero.MyLogger.Main;

import java.io.File;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.UUID;

public class PlayerMineEvent implements Listener {

    int veinDistance = Main.plugin.getConfig().getInt("vein-distance");

    public void reloadConfig() {
        veinDistance = Main.plugin.getConfig().getInt("vein-distance");
    }

    public String getTime() {
        Date date = new Date();
        LocalDateTime time = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String pattern = "E, dd MMM yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(time);
    }

    @EventHandler
    public void OreLog(OreLogEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        File file = PlayerFile.getTodaysFile(player);
        File infoFile = PlayerFile.getInfoFile(player);
        YamlConfiguration info = YamlConfiguration.loadConfiguration(infoFile);
        YamlConfiguration mineLog = YamlConfiguration.loadConfiguration(file);
        // Initial key is the player's world
        String worldName = player.getWorld().getName();
        ConfigurationSection worldSection = mineLog.getConfigurationSection(worldName);
        ArrayList<LoggedVein> loggedVeins = new ArrayList<>();
        UUID veinID = UUID.randomUUID();
        Location lastMined = info.getLocation("last-mined");
        assert lastMined != null;
        double distance = lastMined.distanceSquared(block.getLocation());
        if(lastMined.getWorld() == block.getWorld() && distance < veinDistance) {
            Location veinStart = info.getLocation("vein-start");
            assert veinStart != null;
            int x = veinStart.getBlockX();
            int y = veinStart.getBlockY();
            int z = veinStart.getBlockZ();
            String locString = x + ", " + y + ", " + z;
            String blockPath = veinID + ".blocks." + block.getWorld().getName() + "." + locString;
            mineLog.set(blockPath + ".light", 13);
            mineLog.set(blockPath + ".time", getTime());
        } else {
            info.set("vein-start", block.getLocation());
        }
        info.set("last-mined", block.getLocation());
    }

    @EventHandler
    public void PlayerMineEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if(player.hasPermission("mylogger.log." + e.getBlock().getType())) {
            Bukkit.getPluginManager().callEvent(new OreLogEvent(player, block));
        }
    }
}

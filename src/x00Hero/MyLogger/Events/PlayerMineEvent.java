package x00Hero.MyLogger.Events;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
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
import java.io.IOException;
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
        String pattern = "E, dd MMM yyyy HH:mm:ss";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(pattern);
        return simpleDateFormat.format(date);
    }

    @EventHandler
    public void PlayerBreakEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        Main.plugin.getLogger().info("doing things");
        if(player.hasPermission("mylogger.log." + e.getBlock().getType())) {
            File monthFile = PlayerFile.getMonthFile(player);
            File infoFile = PlayerFile.getInfoFile(player);
            File file = PlayerFile.getTodaysFile(player);
            try {
                if(!monthFile.exists()) monthFile.mkdir();
                if(!infoFile.exists()) infoFile.createNewFile();
                if(!file.exists()) file.createNewFile();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
            YamlConfiguration info = YamlConfiguration.loadConfiguration(infoFile);
            YamlConfiguration mineLog = YamlConfiguration.loadConfiguration(file);
            String veinID = info.getString("vein-id");
            String blockID = RandomStringUtils.randomAlphanumeric(8);
            if(veinID == null) veinID = RandomStringUtils.randomAlphanumeric(8);
            Location lastMined = info.getLocation("last-mined");
            boolean newVein = false;
            if(lastMined == null) {
                newVein = true;
                lastMined = e.getBlock().getLocation();
            }
            double distance = lastMined.distance(e.getBlock().getLocation());
            String matString = mineLog.getString(veinID + ".material");
            Material material;
            if(matString == null) material = block.getType(); else material = Material.valueOf(matString);
            if(distance > veinDistance || block.getType() != material || newVein) {
                // new vein
                veinID = RandomStringUtils.randomAlphanumeric(8);
                info.set("vein-id", veinID);
                mineLog.set(veinID + ".material", block.getType().toString());
            }
            int x = lastMined.getBlockX();
            int y = lastMined.getBlockY();
            int z = lastMined.getBlockZ();
            String locString = x + ", " + y + ", " + z;
            String blockPath = veinID + ".blocks." + blockID + ".";
            int light = player.getLocation().getBlock().getLightLevel();
            int skyLight = player.getLocation().getBlock().getLightFromSky();
            mineLog.set(blockPath + ".light", light + " (" + skyLight + ")");
            mineLog.set(blockPath + ".time", getTime());
            mineLog.set(blockPath + ".location", block.getWorld().getName() + " @ " + locString);
            info.set("last-mined", block.getLocation());
            try {
                info.save(infoFile);
                mineLog.save(file);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

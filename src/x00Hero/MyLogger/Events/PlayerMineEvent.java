package x00Hero.MyLogger.Events;

import org.apache.commons.lang.RandomStringUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import x00Hero.MyLogger.File.PlayerFile;
import x00Hero.MyLogger.LogController;
import x00Hero.MyLogger.Main;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

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
        if(player.hasPermission("mylogger.log." + e.getBlock().getType())) {
            LogController.alertMods(player, block);
            File yearFolder = PlayerFile.getYearFolder(player);
            File monthFolder = PlayerFile.getMonthFolder(player);
            File infoFile = PlayerFile.getInfoFile(player);
            File mineFile = PlayerFile.getTodaysFile(player);
            try {
                if(!yearFolder.exists()) yearFolder.mkdir();
                if(!monthFolder.exists()) monthFolder.mkdir();
                if(!infoFile.exists()) infoFile.createNewFile();
                if(!mineFile.exists()) mineFile.createNewFile();
            } catch(IOException ex) {
                ex.printStackTrace();
            }
            YamlConfiguration info = YamlConfiguration.loadConfiguration(infoFile);
            YamlConfiguration mineLog = YamlConfiguration.loadConfiguration(mineFile);
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
                mineLog.save(mineFile);
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}

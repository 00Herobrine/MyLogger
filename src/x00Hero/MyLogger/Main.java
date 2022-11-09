package x00Hero.MyLogger;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public class Main extends JavaPlugin {
    Plugin plugin;
    ArrayList<Material> logMats = new ArrayList<>();
    public void onEnable() {
        // do the things
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new PlayerMineEvent(), plugin);
    }

}
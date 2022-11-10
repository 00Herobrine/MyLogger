package x00Hero.MyLogger;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import x00Hero.MyLogger.Events.GUI.MenuClick;
import x00Hero.MyLogger.Events.PlayerMineEvent;

public class Main extends JavaPlugin {
    // add world dependent logging
    // would just give them the same perm but with the world name
    // mylogger.log.{worldname}.DIAMOND_ORE
    public static Plugin plugin;

    public void onEnable() {
        // do the things
        plugin = this;
        Bukkit.getPluginManager().registerEvents(new PlayerMineEvent(), plugin);
        Bukkit.getPluginManager().registerEvents(new MenuClick(), plugin);
    }

}
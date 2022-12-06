package x00Hero.MyLogger;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;
import x00Hero.MyLogger.Chat.Commands.CommandManager;
import x00Hero.MyLogger.Chat.Commands.TabManager;
import x00Hero.MyLogger.GUI.Events.MenuController;
import x00Hero.MyLogger.Events.PlayerMineEvent;
import x00Hero.MyLogger.GUI.PlayerMenu;

import java.io.File;

public class Main extends JavaPlugin {
    // add world dependent logging
    // would just give them the same perm but with the world name
    // mylogger.log.{worldname}.DIAMOND_ORE
    public static FileConfiguration config;
    public static Plugin plugin;
    public static File playersFile;

    public void onEnable() {
        // do the things
        plugin = this;
        config = getConfig();
        playersFile = new File(getDataFolder() + "/players");
        if(!playersFile.exists()) playersFile.mkdir();
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(new MenuController(), plugin);
        Bukkit.getPluginManager().registerEvents(new PlayerMineEvent(), plugin);
        registerCommands();
        reloadConfigs();
    }

    public void registerCommands() {
        getCommand("logger").setExecutor(new CommandManager());
        getCommand("logger").setTabCompleter(new TabManager());
    }

    public static FileConfiguration getConfigFile() {
        return config;
    }

    public static void reloadConfigs(){
        plugin.reloadConfig();
        config = plugin.getConfig();
        MenuController.cacheCustomMats();
        MenuController.cacheCustomStrings();
        PlayerMineEvent.reloadConfig();
        PlayerMenu.reloadConfigs();
    }

}
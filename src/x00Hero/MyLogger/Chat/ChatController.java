package x00Hero.MyLogger.Chat;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.yaml.snakeyaml.Yaml;
import x00Hero.MyLogger.Main;

public class ChatController {
    static String Prefix = Main.plugin.getConfig().getString("messages.0");

    public static boolean hasOreColor(Material material) {
        FileConfiguration config = Main.getConfigFile();
        String color = config.getString("UI.colors." + material.toString());
        return color != null;
    }

    public static String getOreColor(Material material) {
        FileConfiguration config = Main.getConfigFile();
        return config.getString("UI.colors." + material.toString());
    }

    public static String getMessage(Integer messageID) {
        String message = Main.getConfigFile().getString("messages." + messageID);
        assert message != null;
        return ChatColor.translateAlternateColorCodes('&', message.replace("{prefix}", Prefix));
    }

    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendMessage(Player player, Integer messageID) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage(messageID).replace("{prefix}", Prefix).replace("{player}", player.getDisplayName())));
    }
}

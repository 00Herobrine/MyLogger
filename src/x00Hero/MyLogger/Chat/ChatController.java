package x00Hero.MyLogger.Chat;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import x00Hero.MyLogger.Main;

public class ChatController {
    static String Prefix = Main.plugin.getConfig().getString("messages.0");

    public static String getMessage(Integer messageID) {
        String message = Main.plugin.getConfig().getString("messages." + messageID);
        assert message != null;
        return message.replace("{prefix}", Prefix);
    }

    public static void sendMessage(Player player, Integer messageID) {
        player.sendMessage(ChatColor.translateAlternateColorCodes('&', getMessage(messageID).replace("{prefix}", Prefix).replace("{player}", player.getDisplayName())));
    }
}

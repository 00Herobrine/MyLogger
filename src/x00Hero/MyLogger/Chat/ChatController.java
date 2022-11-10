package x00Hero.MyLogger.Chat;

import org.bukkit.entity.Player;
import x00Hero.MyLogger.Main;

public class ChatController {
    static String Prefix = getMessage(0);

    public static String getMessage(Integer messageID) {
        return Main.plugin.getConfig().getString("messages." + messageID);
    }

    public static void sendMessage(Player player, Integer messageID) {
        player.sendMessage(getMessage(messageID).replace("{prefix}", Prefix).replace("{player}", player.getDisplayName()));
    }
}

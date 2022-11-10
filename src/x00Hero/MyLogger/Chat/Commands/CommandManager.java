package x00Hero.MyLogger.Chat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandSendEvent;
import x00Hero.MyLogger.Chat.ChatController;
import x00Hero.MyLogger.LogController;

import java.util.UUID;

public class CommandManager implements CommandExecutor {

    public void CommandList(PlayerCommandSendEvent e) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("Only Players can run this command.");
            return false;
        }
        Player player = (Player) sender;
        switch(command.getName().toLowerCase()) {
            case "log":
                switch(args.length) {
                    case 0:
                        // basic gui
                    case 1:
                        // player gui
                        if(args[0].equalsIgnoreCase("admin")) {
                            if(!LogController.modAlerts.contains(player.getUniqueId())) {
                                LogController.modAlerts.add(player.getUniqueId());
                                ChatController.sendMessage(player, 0);
                            } else {
                                LogController.modAlerts.remove(player.getUniqueId());
                                ChatController.sendMessage(player, 1);
                            }
                        }
                        Player onlineTar = Bukkit.getPlayer(args[0]);
                        UUID target;
                        if(onlineTar == null) target = Bukkit.getOfflinePlayer(args[0]).getUniqueId(); else target = onlineTar.getUniqueId();
                        break;
                    default:
                        ChatController.sendMessage(player, 2);
                        break;

                }
                break;
        }
        return false;
    }
}

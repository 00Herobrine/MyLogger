package x00Hero.MyLogger.Chat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerCommandSendEvent;
import x00Hero.MyLogger.Chat.ChatController;
import x00Hero.MyLogger.File.PlayerFile;
import x00Hero.MyLogger.GUI.PlayerMenu;
import x00Hero.MyLogger.LogController;
import x00Hero.MyLogger.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class CommandManager implements CommandExecutor {

    ArrayList<UUID> debugPlayers = new ArrayList<>();

    public void CommandList(PlayerCommandSendEvent e) {

    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatController.getMessage(10));
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
                        switch(args[0]) {
                            case "admin":
                                if(player.hasPermission("mylogger.admin.overwatch")) {
                                    if(!LogController.modAlerts.contains(player.getUniqueId())) {
                                        LogController.modAlerts.add(player.getUniqueId());
                                        ChatController.sendMessage(player, 0);
                                    } else {
                                        LogController.modAlerts.remove(player.getUniqueId());
                                        ChatController.sendMessage(player, 1);
                                    }
                                } else {
                                    ChatController.sendMessage(player, 7);
                                }
                                break;
                            case "debug":
                                UUID uuid = player.getUniqueId();
                                if(debugPlayers.contains(uuid)) {
                                    debugPlayers.remove(uuid);
                                    ChatController.sendMessage(player, 5);
                                } else {
                                    debugPlayers.add(uuid);
                                    ChatController.sendMessage(player, 6);
                                }
                                break;
                            case "reload":
                                if(player.hasPermission("mylogger.admin.reload")) {
                                    Main.reloadConfigs();
                                    ChatController.sendMessage(player, 11);
                                }
                                break;
                            default:
                                Player onlineTar = Bukkit.getPlayer(args[0]);
                                UUID target;
                                if(onlineTar == null) target = Bukkit.getOfflinePlayer(args[0]).getUniqueId();
                                else target = onlineTar.getUniqueId();
                                File file = PlayerFile.getPlayerFolder(target);
                                if(file.exists()) {
                                    PlayerMenu.yearMenu(player, target);
                                    player.sendMessage(ChatController.getMessage(8).replace("{target}", target.toString()));
//                                    ChatController.sendMessage(player, 8);
                                } else {
                                    ChatController.sendMessage(player, 9);
                                }
                                break;
                        }
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

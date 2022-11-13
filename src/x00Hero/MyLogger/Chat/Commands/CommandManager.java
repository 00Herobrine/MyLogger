package x00Hero.MyLogger.Chat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
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

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatController.getMessage(10));
            return false;
        }
        Player player = (Player) sender;
        switch(command.getName().toLowerCase()) {
            case "log":
                switch(args[0]) {
                    case "admin":
                        if(player.hasPermission("mylogger.admin.overwatch")) {
                            if(!LogController.getModAlerts().contains(player.getUniqueId())) {
                                LogController.addMod(player);
                                ChatController.sendMessage(player, 1);
                            } else {
                                LogController.removeMod(player);
                                ChatController.sendMessage(player, 2);
                            }
                        } else {
                            ChatController.sendMessage(player, 7);
                        }
                        break;
                    case "debug":
                        UUID uuid = player.getUniqueId();
                        if(player.hasPermission("mylogger.admin.debug")) {
                            if(debugPlayers.contains(uuid)) {
                                debugPlayers.remove(uuid);
                                ChatController.sendMessage(player, 5);
                            } else {
                                debugPlayers.add(uuid);
                                ChatController.sendMessage(player, 6);
                            }
                        } else {
                            ChatController.sendMessage(player, 7);
                        }
                        break;
                    case "reload":
                        if(player.hasPermission("mylogger.admin.reload")) {
                            Main.reloadConfigs();
                            ChatController.sendMessage(player, 11);
                        } else {
                            ChatController.sendMessage(player, 7);
                        }
                        break;
                    case "view":
                        if(args.length > 1) {
                            Player onlineTar = Bukkit.getPlayer(args[1]);
                            UUID target;
                            if(onlineTar == null) target = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                            else target = onlineTar.getUniqueId();
                            File file = PlayerFile.getPlayerFolder(target);
                            if(file.exists()) {
                                PlayerMenu.yearMenu(player, target);
                                player.sendMessage(ChatController.getMessage(8).replace("{target}", args[1]));
//                                    ChatController.sendMessage(player, 8);
                            } else {
                                player.sendMessage(ChatController.getMessage(9).replace("{target}", args[1]));
                            }
                        } else {
                            ChatController.sendMessage(player, 3);
                        }
                        break;
                    default:
                        ChatController.sendMessage(player, 3);
                        break;
                }
                break;
            default:
                ChatController.sendMessage(player, 3);
                break;
        }
        return false;
    }
}

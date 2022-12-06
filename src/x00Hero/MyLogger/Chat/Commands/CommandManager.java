package x00Hero.MyLogger.Chat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import x00Hero.MyLogger.Chat.ChatController;
import x00Hero.MyLogger.File.PlayerFile;
import x00Hero.MyLogger.GUI.PlayerMenu;
import x00Hero.MyLogger.LogController;
import x00Hero.MyLogger.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;

public class CommandManager implements CommandExecutor {
    private static final Permission viewPermission = new Permission("mylogger.admin.view"); // possibly make this player dependent
    private static final Permission alertPermission = new Permission("mylogger.admin.overwatch");
    private static final Permission debugPermission = new Permission("mylogger.admin.debug");
    private static final Permission reloadPermission = new Permission("mylogger.admin.reload");
    ArrayList<UUID> debugPlayers = new ArrayList<>();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage(ChatController.getMessage(10));
            return false;
        }
        Player player = (Player) sender;
        switch(command.getName().toLowerCase()) {
            case "logger":
                switch(args[0]) {
                    case "admin":
                        if(player.hasPermission(alertPermission)) {
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
                        if(player.hasPermission(debugPermission)) {
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
                        if(player.hasPermission(reloadPermission)) {
                            Main.reloadConfigs();
                            ChatController.sendMessage(player, 11);
                        } else {
                            ChatController.sendMessage(player, 7);
                        }
                        break;
                    case "view":
                        if(player.hasPermission(viewPermission)) {
                            if(args.length > 1) {
                                Player onlineTar = Bukkit.getPlayer(args[1]);
                                UUID target;
                                if(onlineTar == null) target = Bukkit.getOfflinePlayer(args[1]).getUniqueId();
                                else target = onlineTar.getUniqueId();
                                File file = PlayerFile.getPlayerFolder(target);
                                if(file.exists()) {
                                    PlayerMenu.yearMenu(player, target); // possibly migrate this to ChatController.sendMessage(player, String, ReplacementString)
                                    player.sendMessage(ChatController.getMessage(8).replace("{target}", args[1]));
                                } else {
                                    player.sendMessage(ChatController.getMessage(9).replace("{target}", args[1]));
                                }
                            } else {
                                ChatController.sendMessage(player, 3);
                            }
                        } else {
                            ChatController.sendMessage(player, 7);
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

    public static Permission getAlertPermission() {
        return alertPermission;
    }
    public static Permission getDebugPermission() {
        return debugPermission;
    }
    public static Permission getReloadPermission() {
        return reloadPermission;
    }
    public static Permission getViewPermission() {
        return viewPermission;
    }
}

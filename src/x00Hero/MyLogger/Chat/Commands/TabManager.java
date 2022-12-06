package x00Hero.MyLogger.Chat.Commands;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class TabManager implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String string, String[] args) {
        ArrayList<String> arguments = new ArrayList<>();
        if(args.length == 1) {
            if(commandSender.hasPermission(CommandManager.getViewPermission())) arguments.add("view");
            if(commandSender.hasPermission(CommandManager.getAlertPermission())) arguments.add("admin");
            if(commandSender.hasPermission(CommandManager.getReloadPermission())) arguments.add("reload");
        } else if(args.length == 2 && args[0].equalsIgnoreCase("view")) {
            for(Player p : Bukkit.getOnlinePlayers()) {
                arguments.add(p.getName());
            }
        }
        return arguments;
    }
}

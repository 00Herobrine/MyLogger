package x00Hero.MyLogger.Events;

import org.bukkit.Bukkit;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerMineEvent implements Listener {

    @EventHandler
    public void PlayerMineEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
        Block block = e.getBlock();
        if(player.hasPermission("mylogger.log." + e.getBlock().getType())) {
            Bukkit.getPluginManager().callEvent(new OreLogEvent(player, block));
        }
    }
}

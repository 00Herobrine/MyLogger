package x00Hero.MyLogger;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class PlayerMineEvent implements Listener {

    @EventHandler
    public void PlayerMineEvent(BlockBreakEvent e) {
        Player player = e.getPlayer();
    }
}

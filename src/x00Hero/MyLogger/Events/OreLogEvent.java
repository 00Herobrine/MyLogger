package x00Hero.MyLogger.Events;

import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class OreLogEvent extends Event {
    HandlerList handlerList = new HandlerList();
    Player player;
    Block block;

    public OreLogEvent(Player player, Block block) {
        this.player = player;
        this.block = block;
    }

    public Player getPlayer() {
        return player;
    }

    public Block getBlock() {
        return block;
    }

    @Override
    public HandlerList getHandlers() {
        return handlerList;
    }
}

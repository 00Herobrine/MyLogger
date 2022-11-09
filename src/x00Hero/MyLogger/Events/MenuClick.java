package x00Hero.MyLogger.Events;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import x00Hero.MyLogger.GUI.Menu;
import x00Hero.MyLogger.GUI.MenuItem;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuClick implements Listener {
    private static HashMap<Player, Menu> inMenus = new HashMap<>();

    public void MenuClicked(MenuItemClickedEvent event) {
        switch(event.getID()) {
            case "cunt":
                break;
        }
    }

    public static void setMenu(Player player, Menu menu) {
        inMenus.put(player, menu);
    }

    @EventHandler
    public void inventoryClick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if(e.getCurrentItem() == null || e.getCurrentItem().getType() == Material.AIR) return;
        String title = ChatColor.stripColor(e.getView().getTitle());
        String itemName = ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName());
        if(e.getCurrentItem().equals(Menu.nothing.getItemStack())) {
            e.setCancelled(true);
            return;
        }

        if(inMenus.containsKey(p)) {
            Menu menuViewer = inMenus.get(p);
            if(e.getView().getTopInventory().equals(menuViewer.getCurrentInventory())) {
                ArrayList<MenuItem> menuItems = menuViewer.getCurrentPage().getMenuItems();
                if(menuItems != null) {
                    for(MenuItem menuItem : menuItems) {
                        if(e.getCurrentItem().equals(menuItem.getItemStack())) {
                            Bukkit.getServer().getPluginManager().callEvent(new MenuItemClickedEvent(p, menuItem, menuViewer, e));
                            return;
                        }
                    }
                }
            }
        }
    }
}

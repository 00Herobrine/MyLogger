package x00Hero.MyLogger.Events.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.Configuration;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import x00Hero.MyLogger.GUI.API.Menu;
import x00Hero.MyLogger.GUI.API.MenuItem;
import x00Hero.MyLogger.Main;

import java.util.ArrayList;
import java.util.HashMap;

public class MenuController implements Listener {
    private static HashMap<Player, Menu> inMenus = new HashMap<>();
    private static HashMap<String, Material> customDayMats = new HashMap<>();
    private static HashMap<String, Material> customMonthMats = new HashMap<>();
    private static HashMap<String, Material> customYearMats = new HashMap<>();

    public void cacheCustomMats() {
        ArrayList<String> store = new ArrayList<>();
        store.add("days");
        store.add("months");
        store.add("years");
        FileConfiguration config = Main.plugin.getConfig();
        for(String curStore : store) {
            ConfigurationSection configSection = config.getConfigurationSection("UI." + store);
            for(String key : configSection.getKeys(false)) {
                String value = configSection.getString(key);
                Material mat = Material.valueOf(value);
                switch(curStore) {
                    case "days" -> customDayMats.put(key, mat);
                    case "months" -> customMonthMats.put(key, mat);
                    case "years" -> customYearMats.put(key, mat);
                }
            }
        }
    }

    public static boolean hasCustomItem(String num, String type) { // date number, specify whether it's a day, year, or month
        return switch(type) {
            case "day", "days" -> customDayMats.containsKey(num);
            case "month", "months" -> customMonthMats.containsKey(num);
            case "year", "years" -> customYearMats.containsKey(num);
            default -> false;
        };
    }

    public static Material getCustomItem(String num, String type) {
        return switch(type) {
            case "day", "days" -> customDayMats.get(num);
            case "month", "months" -> customMonthMats.get(num);
            case "year", "years" -> customYearMats.get(num);
            default -> null;
        };
    }

    @EventHandler
    public void MenuClicked(MenuItemClickedEvent event) {
        Player player = event.getWhoClicked();
        Menu menu = event.getMenu();
        MenuItem menuItem = event.getMenuItem();
        if(menuItem.isCancelClick()) event.setCancelled(true);
        switch(event.getID()) {
            case "menu-page-next":
                menu.nextPage(player);
                break;
            case "menu-page-previous":
                menu.prevPage(player);
                break;
            default:
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

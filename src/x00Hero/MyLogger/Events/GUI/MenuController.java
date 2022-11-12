package x00Hero.MyLogger.Events.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import x00Hero.MyLogger.File.PlayerFile;
import x00Hero.MyLogger.GUI.API.ItemBuilder;
import x00Hero.MyLogger.GUI.API.Menu;
import x00Hero.MyLogger.GUI.API.MenuItem;
import x00Hero.MyLogger.GUI.API.MenuItemClickedEvent;
import x00Hero.MyLogger.GUI.PlayerMenu;
import x00Hero.MyLogger.Main;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MenuController implements Listener {
    private static HashMap<Player, Menu> inMenus = new HashMap<>();
    private static HashMap<String, Material> customDayMats = new HashMap<>();
    private static HashMap<String, Material> customMonthMats = new HashMap<>();
    private static HashMap<String, Material> customYearMats = new HashMap<>();
    // maybe temporarily store the Player with playerFile in a hashmap so I don't need to constantly store all this in NBT (can be viewed by players)
    // HashMap<Player, File> fileAccess
    public static void cacheCustomMats() {
        ArrayList<String> store = new ArrayList<>();
        store.add("days");
        store.add("months");
        store.add("years");
        FileConfiguration config = Main.plugin.getConfig();
        for(String curStore : store) {
            ConfigurationSection configSection = config.getConfigurationSection("UI." + curStore);
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

    public String getStoredString(ItemStack itemStack, String key) {
        NamespacedKey namespacedKey = new NamespacedKey(Main.plugin, key);
        ItemMeta itemMeta = itemStack.getItemMeta();
        assert itemMeta != null;
        PersistentDataContainer container = itemMeta.getPersistentDataContainer();
        if(container.has(namespacedKey, PersistentDataType.STRING)) {
            return container.get(namespacedKey, PersistentDataType.STRING);
        }
        return null;
    }

    @EventHandler
    public void MenuClicked(MenuItemClickedEvent event) {
        Player player = event.getWhoClicked();
        Menu menu = event.getMenu();
        MenuItem menuItem = event.getMenuItem();
        if(menuItem.isCancelClick()) event.setCancelled(true);
        String ID = event.getID();
        String args[] = ID.split("/");
        ItemStack itemStack = event.getMenuItem().getItemStack();
        switch(args[0]) {
            case "menu-page-next":
                menu.nextPage(player);
                break;
            case "menu-page-previous":
                menu.prevPage(player);
                break;
            case "menu-year":
                String year = getStoredString(itemStack,"year");
                String targetString = getStoredString(itemStack, "target");
                assert targetString != null;
                UUID target = UUID.fromString(targetString);
                PlayerMenu.monthMenu(player, target, year);
                break;
            case "menu-month":
                year = getStoredString(itemStack,"year");
                String month = getStoredString(itemStack,"month");
                targetString = getStoredString(itemStack, "target");
                assert targetString != null;
                target = UUID.fromString(targetString);
                PlayerMenu.dayMenu(player, target, year, month);
                break;
            case "menu-day":
                year = getStoredString(itemStack,"year");
                month = getStoredString(itemStack,"month");
                String day = getStoredString(itemStack,"day");
                targetString = getStoredString(itemStack, "target");
                assert targetString != null;
                target = UUID.fromString(targetString);
                PlayerMenu.veinMenu(player, target, year, month, day);
                // vein menu
                break;
            case "menu-vein":
                year = getStoredString(itemStack,"year");
                month = getStoredString(itemStack,"month");
                day = getStoredString(itemStack,"day");
                targetString = getStoredString(itemStack, "target");
                assert targetString != null;
                target = UUID.fromString(targetString);
                File file = PlayerFile.getFileForDay(target, year, month, day);
                String veinID = event.getMenuItem().getItemStack().getItemMeta().getDisplayName();
                PlayerMenu.blocksMenu(player, file, veinID);
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

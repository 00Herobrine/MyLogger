package x00Hero.MyLogger.GUI.Events;

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
import x00Hero.MyLogger.GUI.Constructors.Menu;
import x00Hero.MyLogger.GUI.Constructors.MenuItem;
import x00Hero.MyLogger.GUI.Constructors.MenuItemClickedEvent;
import x00Hero.MyLogger.GUI.PlayerMenu;
import x00Hero.MyLogger.Main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class MenuController implements Listener {
    private static HashMap<Player, Menu> inMenus = new HashMap<>();
    private static HashMap<String, Material> customDayMats = new HashMap<>();
    private static HashMap<String, Material> customMonthMats = new HashMap<>();
    private static HashMap<String, Material> customYearMats = new HashMap<>();
    private static String yearTitleFormat = "year title";
    private static String yearNameFormat = "year name";
    private static String yearLoreFormat = "year lore";
    private static String monthTitleFormat = "month title";
    private static String monthNameFormat = "month name";
    private static String monthLoreFormat = "month lore";
    private static String dayTitleFormat = "day title";
    private static String dayNameFormat = "day name";
    private static String dayLoreFormat = "day lore";
    private static String veinTitleFormat = "vein title";
    private static String veinNameFormat = "Vein #{num}";
    private static String veinLoreFormat = "format me lol";
    private static String blocksTitleFormat = "block title";
    private static String blocksNameFormat = "block name";
    private static String blocksLoreFormat = "block lore";
    // maybe temporarily store the Player with playerFile in a hashmap so I don't need to constantly store all this in NBT (can be viewed by players)
    // HashMap<Player, File> fileAccess

    public static void cacheCustomStrings() {
        FileConfiguration config = Main.plugin.getConfig();
        String yearTitle = config.getString("UI.years.title");
        String yearName = config.getString("UI.years.name");
        String yearLore = config.getString("UI.years.lore");
        String monthTitle = config.getString("UI.months.title");
        String monthName = config.getString("UI.months.name");
        String monthLore = config.getString("UI.months.lore");
        String dayTitle = config.getString("UI.days.title");
        String dayName = config.getString("UI.days.name");
        String dayLore = config.getString("UI.days.lore");
        String veinTitle = config.getString("UI.veins.title");
        String veinName = config.getString("UI.veins.name");
        String veinLore = config.getString("UI.veins.lore");
        String blockTitle = config.getString("UI.blocks.title");
        String blockName = config.getString("UI.blocks.name");
        String blockLore = config.getString("UI.blocks.lore");
        if(yearTitle != null) yearTitleFormat = yearTitle;
        if(yearName != null) yearNameFormat = yearName;
        if(yearLore != null) yearLoreFormat = yearLore;
        if(monthTitle != null) monthTitleFormat = monthTitle;
        if(monthName != null) monthNameFormat = monthName;
        if(monthLore != null) monthLoreFormat = monthLore;
        if(dayTitle != null) dayTitleFormat = dayTitle;
        if(dayName != null) dayNameFormat = dayName;
        if(dayLore != null) dayLoreFormat = dayLore;
        if(veinTitle != null) veinTitleFormat = veinTitle;
        if(veinName != null) veinNameFormat = veinName;
        if(veinLore != null) veinLoreFormat = veinLore;
        if(blockTitle != null) blocksTitleFormat = blockTitle;
        if(blockName != null) blocksNameFormat = blockName;
        if(blockLore != null) blocksLoreFormat = blockLore;
    }

    public static void cacheCustomMats() {
        ArrayList<String> store = new ArrayList<>();
        store.add("days");
        store.add("months");
        store.add("years");
        FileConfiguration config = Main.plugin.getConfig();
        for(String curStore : store) {
            ConfigurationSection configSection = config.getConfigurationSection("UI." + curStore + ".custom");
            if(configSection == null) continue;
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

    public static String getVeinNameFormat() {
        return veinNameFormat;
    }
    public static String getVeinTitleFormat() {
        return veinTitleFormat;
    }
    public static String getVeinLoreFormat() {
        return veinLoreFormat;
    }
    public static String getYearTitleFormat() {
        return yearTitleFormat;
    }
    public static String getYearNameFormat() {
        return yearNameFormat;
    }
    public static String getYearLoreFormat() {
        return yearLoreFormat;
    }
    public static String getMonthTitleFormat() {
        return monthTitleFormat;
    }
    public static String getMonthNameFormat() {
        return monthNameFormat;
    }
    public static String getMonthLoreFormat() {
        return monthLoreFormat;
    }
    public static String getDayTitleFormat() {
        return dayTitleFormat;
    }
    public static String getDayNameFormat() {
        return dayNameFormat;
    }
    public static String getDayLoreFormat() {
        return dayLoreFormat;
    }
    public static String getBlocksTitleFormat() {
        return blocksTitleFormat;
    }
    public static String getBlocksNameFormat() {
        return blocksNameFormat;
    }
    public static String getBlocksLoreFormat() {
        return blocksLoreFormat;
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
                String veinID = getStoredString(itemStack, "ID");
                assert targetString != null;
                target = UUID.fromString(targetString);
                PlayerMenu.blocksMenu(player, target, year, month, day, veinID);
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

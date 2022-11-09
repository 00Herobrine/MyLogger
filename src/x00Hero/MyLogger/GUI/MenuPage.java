package x00Hero.MyLogger.GUI;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;

public class MenuPage {
    Inventory inventory;
    public static int prevSlot = 45;
    public static int nextSlot = 53;
    int size, rows;
    boolean fillEmpty = true;
    boolean cancelClicks = true;
    static ItemBuilder prevBuilder = new ItemBuilder(Material.ORANGE_STAINED_GLASS_PANE, "&7Previous Page", "&8Click to return a page.");
    static ItemBuilder nextBuilder = new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE, "&aNext Page", "&8Click to forward a page.");
    public MenuItem prevPage = new MenuItem(prevBuilder.getItemStack(), prevSlot, "menu-page-previous");
    public MenuItem nextPage = new MenuItem(nextBuilder.getItemStack(), nextSlot, "menu-page-next");
    ArrayList<MenuItem> menuItems = new ArrayList<>();
    String title;

    public String Colorize(String input) {
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    public MenuPage(String title, int size, boolean fillEmpty, boolean cancelClicks) {
        this.title = title;
        this.size = size;
        this.rows = (int) Math.ceil((double) size / 9);
        this.fillEmpty = fillEmpty;
        this.cancelClicks = cancelClicks;
        int invSize = rows * 9;
        if(invSize > 54) invSize = 54;
        if(size <= 5) {
            inventory = Bukkit.createInventory(null, InventoryType.HOPPER, Colorize(title));
        } else {
            inventory = Bukkit.createInventory(null, invSize, Colorize(title));
        }
    }

    public void setTitle(String title) {
        this.title = title;
        int invSize = inventory.getSize();
        ItemStack[] contents = inventory.getContents();
        inventory = Bukkit.createInventory(null, invSize, Colorize(title));
        inventory.setContents(contents);
    }

    public String getTitle() {
        return title;
    }

    public void expandCheck(MenuItem menuItem) {
        if(menuItem.getSlot() > inventory.getSize()) {
            int slots = Menu.getAdjustedAmount(menuItem.getSlot());
            ItemStack[] items = inventory.getContents();
            inventory = Bukkit.createInventory(null, slots, Colorize(menuItem.getMenuPage().getTitle()));
            inventory.setContents(items);
        }
    }

    public void setItem(ItemStack item, Integer slot) {
        MenuItem menuItem = new MenuItem(item, slot, "default");
        menuItem.setMenuPage(this);
        expandCheck(menuItem);
        inventory.setItem(slot, item);
        menuItems.add(menuItem);
    }

    public void addItem(MenuItem menuItem) {
        menuItem.setMenuPage(this);
        expandCheck(menuItem);
        if(menuItem.isEnabled()) inventory.setItem(menuItem.getSlot(), menuItem.getItemStack());
        menuItems.add(menuItem);
    }

    public void setFillEmpty(boolean fillEmpty) {
        this.fillEmpty = fillEmpty;
    }

    public void setCancelClicks(boolean cancelClicks) {
        this.cancelClicks = cancelClicks;
    }

    public boolean isFillEmpty() {
        return fillEmpty;
    }

    public boolean isCancelClicks() {
        return cancelClicks;
    }

    public int getRows() {
        return rows;
    }

    public int getSize() {
        return size;
    }

    public Inventory getInventory() {
        return inventory;
    }

    public void setInventory(Inventory inventory) {
        this.inventory = inventory;
    }

    public void open(Player p) {
        if(fillEmpty) p.openInventory(Menu.fillInventory(inventory));
        else p.openInventory(inventory);
    }

    public ArrayList<MenuItem> getMenuItems() {
        return menuItems;
    }

    public MenuItem getMenuItem(MenuItem item) {
        for(MenuItem menuItem : getMenuItems()) {
            if(menuItem.equals(item)) {
                return menuItem;
            }
        }
        return null;
    }

    public void setMenuItems(ArrayList<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}

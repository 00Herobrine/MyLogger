package x00Hero.MyLogger.GUI.API;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import x00Hero.MyLogger.Chat.ChatController;
import x00Hero.MyLogger.Events.GUI.MenuController;

import java.util.ArrayList;
import java.util.HashMap;

public class Menu {
    private int size, rows, pages, selPage;
    private HashMap<Integer, MenuPage> menuPages = new HashMap<>();

    public static ItemBuilder nothing = new ItemBuilder(Material.GRAY_STAINED_GLASS_PANE, 1, " ");
    private int maxSlot = 53;

    private boolean isSecureContainer = false;

    public boolean isSecureContainer() {
        return isSecureContainer;
    }

    public void setSecureContainer(boolean secureContainer) {
        isSecureContainer = secureContainer;
    }


    public void setPage(int page, MenuPage menuPage) {
        menuPages.put(page, menuPage);
    }

    public static int getAdjustedAmount(Integer slots) {
        return (int) (Math.ceil((double) slots / 9)) * 9;
    }

    public Menu(String title, ItemStack[] contents) {
        int slots = Menu.getAdjustedAmount(contents.length);
        MenuPage menuPage = new MenuPage(title, slots, false, false);
        Inventory tempInv = Bukkit.createInventory(null, slots, title);
        tempInv.setContents(contents);
        int curSlot = 0;
        for(ItemStack item : tempInv.getContents()) {
            if(item != null && item.getType() != Material.AIR) {
                int size = tempInv.getSize();
                if(curSlot < size) {
                    MenuItem menuItem = new MenuItem(item, curSlot);
                    menuItem.setCancelClick(false);
                    menuPage.addItem(menuItem);
                }
            }
            curSlot++;
        }
        setPage(1, menuPage);
    }

    public Menu(ArrayList<ItemStack> items, String title, boolean fillEmpty, boolean click) {
        selPage = 1;
        int curSlot = 0;
        int curPage = 1;
        size = items.size();
        rows = (int) Math.ceil((double) size / 9);
        pages = (int) Math.ceil((double) size / 54);
        if(pages > 1) {
            // account for the next item and the previous item on first and last page + next/prev for each page between
            int addition = (2 + (Math.abs(2 - pages) * 2));
            size += addition;
            rows = (int) Math.ceil((double) size / 9);
            pages = (int) Math.ceil((double) rows / 6);
        }
        int subtract = 1;
        MenuPage menuPage = new MenuPage(title, size, fillEmpty, click);
        int prevSlot = menuPage.prevPage.getSlot();
        for(ItemStack item : items) {
            if(menuPages.containsKey(curPage)) menuPage = menuPages.get(curPage);
            if(curSlot > maxSlot - subtract && pages != 1) {
                if(curPage != pages) menuPage.addItem(menuPage.nextPage);
                setPage(curPage, menuPage);
                curPage++;
                curSlot = 0;
                menuPage = new MenuPage(title + " Pg " + curPage, size, fillEmpty, click);
                menuPage.setItem(item, curSlot);
            } else {
                if(curSlot == prevSlot && curPage != 1) {
                    menuPage.addItem(menuPage.prevPage);
                    curSlot++;
                }
                menuPage.setItem(item, curSlot);
            }
            curSlot++;
        }
        if(curPage == pages && curSlot < prevSlot && curPage != 1) menuPage.addItem(menuPage.prevPage);
        setPage(curPage, menuPage);
    }

    public Menu(String title, ArrayList<MenuItem> menuItems, int slots, boolean fillEmpty, boolean cancelClicks) {
        selPage = 1;
        int curPage = 1;
        int subtract = 1;
        int prevSlot = MenuPage.prevSlot;
        for(MenuItem menuItem : menuItems) {
            int itemPageInt = menuItem.getPage();
            int itemSlot = menuItem.getSlot();
            MenuPage itemPage = getMenuPage(itemPageInt);
            if(itemPage != null) { // page exists already
                if(itemSlot == -1) { // not specified, add anywhere
                    int emptySlot = itemPage.getInv().firstEmpty(); // if there even is an empty check
                    menuItem.setSlot(emptySlot);
                    itemPage.addItem(menuItem);
                } else if(itemPage.getInv().getItem(itemSlot) != null) { // item already in slot
                    itemSlot++; // need to make if > maxSlot add to next page
                    menuItem.setSlot(itemSlot);
                    itemPage.addItem(menuItem);
                } else { // no item there place
                    itemPage.addItem(menuItem);
                }
            } else { // Page doesn't exist make it and add item
                if(itemPageInt != 1) title += " Pg " + itemPageInt;
                itemPage = new MenuPage(title, getAdjustedAmount(slots), fillEmpty, cancelClicks);
                if(itemSlot == -1) menuItem.setSlot(itemPage.getInv().firstEmpty()); // no slot specified add anywhere
                itemPage.addItem(menuItem);
            }
            if(menuItem.getSlot() > itemPage.getLastSlot()) itemPage.setLastSlot(menuItem.getSlot());
            setPage(itemPageInt, itemPage);
        }

/*            if(curSlot > maxSlot - subtract && pages != 1) { // if it's just one page, don't need to do paging stuff
                setPage(curPage, menuPage);
                curSlot = 0;
                curPage++;
                menuPage = new MenuPage(title + " Pg " + curPage, size, fillEmpty, cancelClicks);
                menuPage.addItem(menuItem);
            } else { // add stuff to page
                if(curSlot == prevSlot && curPage != 1) {
                    menuPage.addItem(menuPage.prevPage);
                    curSlot++;
                }
                menuItem.setSlot(curSlot);
                menuPage.addItem(menuItem);
            }
            setPage(itemPageInt, itemPage);
            curSlot++;
            itemPage.setCurSlot(curSlot);
        }*/
/*        int largestSlot = 0;
        int largestPage = 1;
        selPage = 1;
        for(MenuItem menuItem : menuItems) {
            int currentSlot = menuItem.getSlot();
            int currentPage = menuItem.getPage();
            if(largestSlot < currentSlot) largestSlot = currentSlot;
            if(largestPage < currentPage) largestPage = currentPage;
            MenuPage menuPage = menuPages.get(currentPage);
            if(menuPage != null) {
                menuPage.addItem(menuItem);
            } else {
                menuPage = new MenuPage(title, largestSlot, fillEmpty, cancelClicks);
                menuPage.addItem(menuItem);
            }
            setPage(currentPage, menuPage);
        }*/
    }

    public void openMenu(Player player) {
        openMenu(player, 1);
    }

    public void openMenu(Player player, int page) {
        if(!menuPages.containsKey(page)) {
            ChatController.sendMessage(player, 4);
            return;
        }
        selPage = page;
        MenuPage menuPage = menuPages.get(page);
        menuPage.open(player);
        MenuController.setMenu(player, this);
    }

    public void nextPage(Player player) {
        selPage += 1;
        if(selPage > pages) selPage = 1;
        openMenu(player, selPage);
    }

    public void prevPage(Player player) {
        selPage -= 1;
        if(selPage <= 0) selPage = pages;
        openMenu(player, selPage);
    }

    public static Inventory fillInventory(Inventory i) {
        for(int f = 0; f < i.getSize(); f++) { // turn this into a function
            if(i.firstEmpty() != -1) {
                i.setItem(i.firstEmpty(), nothing.getItemStack());
            } else {
                f = i.getSize();
            }
        }
        return i;
    }

    public MenuPage getMenuPage(int page) {
        return menuPages.get(page);
    }

    public int getSize() {
        return size;
    }

    public int getRows() {
        return rows;
    }

    public int getPages() {
        return pages;
    }

    public int getSelPage() {
        return selPage;
    }

    public int getNextPage() {
        return selPage + 1;
    }

    public int getPrevPage() {
        return selPage - 1;
    }

    public Inventory getCurrentInventory() {
        return getCurrentPage().getInventory();
    }

    public ItemStack getNextItem() {
        return getCurrentPage().nextPage.getItemStack();
    }

    public ItemStack getPrevItem() {
        return getCurrentPage().prevPage.getItemStack();
    }

    public MenuPage getCurrentPage() {
        return getMenuPage(selPage);
    }

    public void set(ItemStack itemStack, Integer slot) {
        MenuItem menuItem = new MenuItem(itemStack, slot);
    }
}

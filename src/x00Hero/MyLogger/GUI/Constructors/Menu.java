package x00Hero.MyLogger.GUI.Constructors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import x00Hero.MyLogger.Chat.ChatController;
import x00Hero.MyLogger.GUI.Events.MenuController;

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

    public Menu(ArrayList<ItemStack> items, String title, String ID, boolean fillEmpty, boolean click) {
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
        int prevSlot = MenuPage.prevPage.getSlot();
        for(ItemStack item : items) {
            if(menuPages.containsKey(curPage)) menuPage = menuPages.get(curPage);
            if(curSlot > maxSlot - subtract && pages != 1) {
                if(curPage != pages) menuPage.addItem(MenuPage.nextPage);
                setPage(curPage, menuPage);
                curPage++;
                curSlot = 0;
                MenuItem menuItem = new MenuItem(item, curSlot, ID);
                menuPage = new MenuPage(title + " Pg " + curPage, size, fillEmpty, click);
                menuItem.setMenuPage(menuPage);
                menuPage.addItem(menuItem);
            } else {
                if(curSlot == prevSlot && curPage != 1) {
                    menuPage.addItem(MenuPage.prevPage);
                    curSlot++;
                }
                MenuItem menuItem = new MenuItem(item, curSlot, ID);
                menuItem.setMenuPage(menuPage);
                menuPage.addItem(menuItem);
            }
            curSlot++;
        }
        if(curPage == pages && curSlot < prevSlot && curPage != 1) menuPage.addItem(MenuPage.prevPage);
        setPage(curPage, menuPage);
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
        int prevSlot = MenuPage.prevPage.getSlot();
        for(ItemStack item : items) {
            if(menuPages.containsKey(curPage)) menuPage = menuPages.get(curPage);
            if(curSlot > maxSlot - subtract && pages != 1) {
                if(curPage != pages) menuPage.addItem(MenuPage.nextPage);
                setPage(curPage, menuPage);
                curPage++;
                curSlot = 0;
                MenuItem menuItem = new MenuItem(item, curSlot, "default");
                menuPage = new MenuPage(title + " Pg " + curPage, size, fillEmpty, click);
                menuItem.setMenuPage(menuPage);
                menuPage.addItem(menuItem);
            } else {
                if(curSlot == prevSlot && curPage != 1) {
                    menuPage.addItem(MenuPage.prevPage);
                    curSlot++;
                }
                MenuItem menuItem = new MenuItem(item, curSlot, "lol");
                menuItem.setMenuPage(menuPage);
                menuPage.addItem(menuItem);
            }
            curSlot++;
        }
        if(curPage == pages && curSlot < prevSlot && curPage != 1) menuPage.addItem(MenuPage.prevPage);
        setPage(curPage, menuPage);
    }

    public Menu(String title, ArrayList<MenuItem> menuItems, boolean fillEmpty, boolean cancelClicks) {
        int lastSlot = 0;
        for(MenuItem item : menuItems) {
            int itemSlot = item.getSlot();
            if(itemSlot > lastSlot) lastSlot = itemSlot;
        }
        MenuPage menuPage = new MenuPage(title, getAdjustedAmount(lastSlot), fillEmpty, cancelClicks);
        for(MenuItem menuItem : menuItems) {
            int itemSlot = menuItem.getSlot();
            if(itemSlot > 53) continue;
            if(itemSlot == -1) {
                menuItem.setSlot(menuPage.getInventory().firstEmpty());
            }
            menuPage.addItem(menuItem);
        }
    }

    public void addPage(Integer pageNum, MenuPage menuPage) {
        menuPages.put(pageNum, menuPage);

    }

    /*public Menu(String title, ArrayList<MenuItem> menuItems, int slots, boolean fillEmpty, boolean cancelClicks) {
            HashMap<Integer, MenuPage> updatedPages = new HashMap<>();
        MenuPage tempPage = new MenuPage("temp", 54, fillEmpty, cancelClicks);
        for(MenuItem menuItem2 : menuItems) {
            int itemPageInt = menuItem2.getPage();
            int itemSlot = menuItem2.getSlot();
            if(itemSlot > 53) continue; // lmao bruh why are you making it bigger than the gui limit
            MenuPage itemPage = getMenuPage(itemPageInt);
            if(itemPage != null) { // page exists already
                if(itemSlot == -1) { // not specified, add anywhere
                    int firstEmpty = itemPage.getInventory().firstEmpty();
                    if(firstEmpty != -1) { // isn't full, set slot
                        menuItem2.setSlot(firstEmpty);
                        tempPage.addItem(menuItem2);
                    } else { // is full, move to next page and set slot
                        boolean found = false;
                        int sPage = itemPageInt;
                        MenuPage emptyPage = getMenuPage(itemPageInt + 1);
                        while(!found) {
                            emptyPage = getMenuPage(sPage);
                            if(emptyPage == null) emptyPage = new MenuPage("temp", 54, fillEmpty, cancelClicks); // doesn't exist, make it
                            if(emptyPage.getInventory().firstEmpty() != -1)  { // has an empty, found
                                found = true;
                            }
                            updatedPages.put(sPage, emptyPage);
                            sPage++;
                        }
                        emptyPage.addItem(menuItem2);
                    }
                } else {

                }
            }
        }
        for(Integer page : updatedPages.keySet()) {
            setPage(page, updatedPages.get(page));
            getMenuPage(1).set;
        }
        // bro idek anymore ^^
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
                    int emptySlot = itemPage.getInventory().firstEmpty(); // if there even is an empty check
                    menuItem.setSlot(emptySlot);
                    itemPage.addItem(menuItem);
                } else if(itemPage.getInventory().getItem(itemSlot) != null) { // item already in slot
                    itemSlot++; // need to make if > maxSlot add to next page
                    menuItem.setSlot(itemSlot);
                    itemPage.addItem(menuItem);
                } else { // no item there place
                    itemPage.addItem(menuItem);
                }
            } else { // Page doesn't exist make it and add item
                if(itemPageInt != 1) title += " Pg " + itemPageInt;
                int slotamt = slots;
                if(slotamt > 5) slotamt = getAdjustedAmount(slots);
                itemPage = new MenuPage(title, slotamt, fillEmpty, cancelClicks);
                if(itemSlot == -1) menuItem.setSlot(itemPage.getInventory().firstEmpty()); // no slot specified add anywhere
                itemPage.addItem(menuItem);
            }
            if(menuItem.getSlot() > itemPage.getLastSlot()) itemPage.setLastSlot(menuItem.getSlot());
            setPage(itemPageInt, itemPage);
        }
    }*/

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

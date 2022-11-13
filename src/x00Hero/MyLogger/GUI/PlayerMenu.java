package x00Hero.MyLogger.GUI;

import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import x00Hero.MyLogger.Events.GUI.MenuController;
import x00Hero.MyLogger.File.PlayerFile;
import x00Hero.MyLogger.GUI.API.ItemBuilder;
import x00Hero.MyLogger.GUI.API.Menu;
import x00Hero.MyLogger.GUI.API.MenuItem;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;


public class PlayerMenu {
    // player = person viewing, UUID = target
    public static void yearMenu(Player player, UUID uuid) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        File playerFile = PlayerFile.getPlayerFolder(uuid);
        for(File year : playerFile.listFiles()) {
            if(!year.isDirectory()) continue;
            String yearString = year.getName();
            Material material = Material.PAPER;
            if(MenuController.hasCustomItem(yearString, "year")) {
                material = MenuController.getCustomItem(yearString, "year");
            }
            ItemBuilder itemBuilder = new ItemBuilder(material, yearString, "Open Months of " + yearString);
            itemBuilder.storeString("year", yearString);
            itemBuilder.storeString("target", uuid.toString());
            MenuItem menuItem = new MenuItem(itemBuilder.getItemStack(), "menu-year");
            menuItems.add(menuItem);
        }
        Menu yMenu = new Menu("&6Years for Player", menuItems, playerFile.listFiles().length, true, true); // get target name if possible
        yMenu.openMenu(player);
    }

    public static void monthMenu(Player player, UUID uuid, String year) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        File yearFolder = PlayerFile.getYearFolder(uuid, year); // only does current year as of now
        for(File month : yearFolder.listFiles()) {
            if(!month.isDirectory()) continue;
            String monthString = month.getName();
            Material material = Material.PAPER;
            if(MenuController.hasCustomItem(monthString, "month")) material = MenuController.getCustomItem(monthString, "month");
            ItemBuilder itemBuilder = new ItemBuilder(material, monthString, "Open Days of " + monthString);
            itemBuilder.storeString("year", year);
            itemBuilder.storeString("month", monthString);
            itemBuilder.storeString("target", uuid.toString());
            MenuItem menuItem = new MenuItem(itemBuilder.getItemStack(), "menu-month");
            menuItems.add(menuItem);
        }
        Menu yMenu = new Menu("&6Months of " + year, menuItems, yearFolder.listFiles().length, true, true); // get target name if possible
        yMenu.openMenu(player);
    }

    public static void dayMenu(Player player, UUID uuid, String year, String month) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        File monthFolder = PlayerFile.getMonthFolder(uuid, year, month); // only does current year as of now
        for(File day : monthFolder.listFiles()) {
            String dayString = day.getName().replace(".yml", "");
            Material material = Material.PAPER;
            if(MenuController.hasCustomItem(dayString, "day")) material = MenuController.getCustomItem(dayString, "day");
            ItemBuilder itemBuilder = new ItemBuilder(material, dayString, "Open logs for " + month + "/" + dayString + "/" + year);
            itemBuilder.storeString("year", year);
            itemBuilder.storeString("month", month);
            itemBuilder.storeString("day", dayString);
            itemBuilder.storeString("target", uuid.toString());
            MenuItem menuItem = new MenuItem(itemBuilder.getItemStack(), "menu-day");
            menuItems.add(menuItem);
        }
        Menu yMenu = new Menu("&6Days of " + month + "-" + year, menuItems, monthFolder.listFiles().length, true, true); // get target name if possible
        yMenu.openMenu(player);
    }

    public static void veinMenu(Player player, UUID uuid, String year, String month, String day) {
        // list all veins for the day
        ArrayList<ItemStack> veins = new ArrayList<>();
        File mineLogFile = PlayerFile.getFileForDay(uuid, year, month, day);
        YamlConfiguration log = YamlConfiguration.loadConfiguration(mineLogFile);
        for(String veinID : log.getKeys(false)) {
            String matString = log.getString(veinID + ".material");
            ConfigurationSection sec = log.getConfigurationSection(veinID + ".blocks");
            int amt = sec.getKeys(false).size();
            if(amt > 64) amt = 64;
            Material material = Material.valueOf(matString);
            ItemBuilder vein = new ItemBuilder(material, amt, veinID, "&8View vein info");
            vein.storeString("year", year);
            vein.storeString("month", month);
            vein.storeString("day", day);
            vein.storeString("target", uuid.toString());
            veins.add(vein.getItemStack());
        }
        String date = month + "/" + day + "/" + year;
        Menu veinMenu = new Menu(veins, "Veins for " + date, "menu-vein", true, true);
        veinMenu.openMenu(player);
    }

    public static void blocksMenu(Player player, File logFile, String veinID) {
        ArrayList<ItemStack> blocks = new ArrayList<>();
        YamlConfiguration log = YamlConfiguration.loadConfiguration(logFile);
        ConfigurationSection blockSection = log.getConfigurationSection(veinID + ".blocks");
        Material material = Material.PAPER;
        String matString = log.getString(veinID + ".material");
        if(matString != null) material = Material.valueOf(matString);
        for(String blockID : blockSection.getKeys(false)) {
            String location = blockSection.getString(blockID + ".location");
            String light = blockSection.getString(blockID + ".light");
            String time = blockSection.getString(blockID + ".time");
            ItemBuilder block = new ItemBuilder(material, "&6" + location, "&7Light: " + light + "\n&7" + time);
            blocks.add(block.getItemStack());
        }
        Menu blockMenu = new Menu(blocks, "Vein viewer", true, true);
        blockMenu.openMenu(player);
    }

}

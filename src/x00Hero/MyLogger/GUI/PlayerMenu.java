package x00Hero.MyLogger.GUI;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import x00Hero.MyLogger.Events.GUI.MenuController;
import x00Hero.MyLogger.File.PlayerFile;
import x00Hero.MyLogger.GUI.API.ItemBuilder;
import x00Hero.MyLogger.GUI.API.Menu;

import java.io.File;
import java.util.ArrayList;
import java.util.UUID;


public class PlayerMenu {
    // player = person viewing, UUID = target
    public static void yearMenu(Player player, UUID uuid) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        File playerFile = PlayerFile.getPlayerFolder(uuid);
        for(File year : playerFile.listFiles()) {
            if(!year.isDirectory()) continue;
            String yearString = year.getName();
            Material material = Material.PAPER;
            if(MenuController.hasCustomItem(yearString, "year")) material = MenuController.getCustomItem(yearString, "year");
            ItemBuilder itemBuilder = new ItemBuilder(material, yearString, "Open Months of " + yearString);
            itemStacks.add(itemBuilder.getItemStack());
        }
        Menu yMenu = new Menu(itemStacks, "Years for " + uuid, true, true); // get target name if possible
        yMenu.openMenu(player);
    }

    public static void monthMenu(Player player, UUID uuid, String year) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        File yearFolder = PlayerFile.getYearFolder(uuid, year); // only does current year as of now
        for(File month : yearFolder.listFiles()) {
            if(!month.isDirectory()) continue;
            String monthString = month.getName();
            Material material = Material.PAPER;
            if(MenuController.hasCustomItem(monthString, "month")) material = MenuController.getCustomItem(monthString, "month");
            ItemBuilder itemBuilder = new ItemBuilder(material, monthString, "Open Days of " + monthString);
            itemStacks.add(itemBuilder.getItemStack());
        }
        Menu yMenu = new Menu(itemStacks, "Months of " + year + " for " + uuid, true, true); // get target name if possible
        yMenu.openMenu(player);
    }

    public static void dayMenu(Player player, UUID uuid, String year, String month) {
        ArrayList<ItemStack> itemStacks = new ArrayList<>();
        File monthFolder = PlayerFile.getMonthFolder(uuid, year, month); // only does current year as of now
        for(File day : monthFolder.listFiles()) {
            String dayString = day.getName();
            Material material = Material.PAPER;
            if(MenuController.hasCustomItem(dayString, "day")) material = MenuController.getCustomItem(dayString, "day");
            ItemBuilder itemBuilder = new ItemBuilder(material, dayString, "Open Days of " + dayString);
            itemStacks.add(itemBuilder.getItemStack());
        }
        Menu yMenu = new Menu(itemStacks, "Days of " + month + "-" + year + " for " + uuid, true, true); // get target name if possible
        yMenu.openMenu(player);
    }

    public void veinMenu(Player player, UUID uuid, String year, String month, String day) {

    }

}

package x00Hero.MyLogger.GUI;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import x00Hero.MyLogger.Constructors.ReplacementString;
import x00Hero.MyLogger.GUI.Events.MenuController;
import x00Hero.MyLogger.File.PlayerFile;
import x00Hero.MyLogger.GUI.Constructors.ItemBuilder;
import x00Hero.MyLogger.GUI.Constructors.Menu;
import x00Hero.MyLogger.GUI.Constructors.MenuItem;
import x00Hero.MyLogger.Main;

import java.io.File;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class PlayerMenu {
    public static void reloadConfigs() {
        autoSkip = Main.getConfigFile().getBoolean("UI.autoSkip");
    }
    private static boolean autoSkip = true;
    private static DateFormatSymbols dfs = new DateFormatSymbols();

    // player = person viewing, UUID = target
    public static void yearMenu(Player player, UUID uuid) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        File playerFile = PlayerFile.getPlayerFolder(uuid);
        ReplacementString rs = new ReplacementString();
        if((playerFile.listFiles().length - 1) == 1 && autoSkip) { // - 1 for player Config
            File[] file = playerFile.listFiles();
            monthMenu(player, uuid, file[0].getName());
        } else {
            Player target = Bukkit.getPlayer(uuid);
            String targetName = uuid.toString();
            if(target != null) targetName = target.getName();
            rs.addReplacement("{target}", targetName);
            rs.addReplacement("{player}", player.getName());
            for(File year : playerFile.listFiles()) {
                if(!year.isDirectory()) continue;
                String yearString = year.getName();
                Material material = Material.PAPER;
                if(MenuController.hasCustomItem(yearString, "year")) {
                    material = MenuController.getCustomItem(yearString, "year");
                }
                ItemBuilder itemBuilder = new ItemBuilder(material, yearString, "View Months of " + yearString);
                itemBuilder.storeString("year", yearString);
                itemBuilder.storeString("target", uuid.toString());
                MenuItem menuItem = new MenuItem(itemBuilder.getItemStack(), "menu-year");
                menuItems.add(menuItem);
            }
            String title = MenuController.getYearTitleFormat();
            Menu yMenu = new Menu(rs.format(title), menuItems, playerFile.listFiles().length, true, true); // get target name if possible
            yMenu.openMenu(player);
        }
    }

    public static void monthMenu(Player player, UUID uuid, String year) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        File yearFolder = PlayerFile.getYearFolder(uuid, year); // only does current year as of now
        ReplacementString rs = new ReplacementString();
        if((yearFolder.listFiles().length) == 1 && autoSkip) {
            File[] file = yearFolder.listFiles();
            dayMenu(player, uuid, year, file[0].getName());
        } else {
            Player target = Bukkit.getPlayer(uuid);
            String targetName = uuid.toString();
            if(target != null) targetName = target.getName();
            rs.addReplacement("{target}", targetName);
            rs.addReplacement("{player}", player.getName());
            rs.addReplacement("{year}", year);
            String lore = MenuController.getMonthLoreFormat();
            String name = MenuController.getMonthNameFormat();
            for(File month : yearFolder.listFiles()) {
                ReplacementString rs2 = new ReplacementString();
                if(!month.isDirectory()) continue;
                String monthString = month.getName();
                int monthInt = Integer.parseInt(monthString);
                rs2.addReplacement("{month}", dfs.getMonths()[monthInt - 1]);
                rs2.addReplacement("{monthNum}", monthString);
                Material material = Material.PAPER;
                if(MenuController.hasCustomItem(monthString, "month")) material = MenuController.getCustomItem(monthString, "month");
                ItemBuilder itemBuilder = new ItemBuilder(material, rs2.format(name), rs2.format(lore));
                itemBuilder.storeString("year", year);
                itemBuilder.storeString("month", monthString);
                itemBuilder.storeString("target", uuid.toString());
                MenuItem menuItem = new MenuItem(itemBuilder.getItemStack(), "menu-month");
                menuItems.add(menuItem);
            }
        }
        String title = MenuController.getMonthTitleFormat();
        Menu yMenu = new Menu(rs.format(title), menuItems, yearFolder.listFiles().length, true, true); // get target name if possible
        yMenu.openMenu(player);
    }

    public static void dayMenu(Player player, UUID uuid, String year, String month) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();
        File monthFolder = PlayerFile.getMonthFolder(uuid, year, month);
        ReplacementString rs = new ReplacementString();
        if((monthFolder.listFiles().length) == 1 && autoSkip) {
            File[] file = monthFolder.listFiles();
            String dayString = file[0].getName().replace(".yml", "");
            veinMenu(player, uuid, year, month, dayString);
        } else {
            Player target = Bukkit.getPlayer(uuid);
            String targetName = uuid.toString();
            int monthInt = Integer.parseInt(month);
            if(target != null) targetName = target.getName();
            rs.addReplacement("{target}", targetName);
            rs.addReplacement("{player}", player.getName());
            rs.addReplacement("{year}", year);
            rs.addReplacement("{month}", dfs.getMonths()[monthInt - 1]);
            rs.addReplacement("{monthShort}", dfs.getShortMonths()[monthInt - 1]);
            String lore = MenuController.getDayLoreFormat();
            String name = MenuController.getDayNameFormat();
            name = rs.format(name);
            lore = rs.format(lore);
            for(File day : monthFolder.listFiles()) {
                String dayString = day.getName().replace(".yml", "");
                rs.addReplacement("{day}", dayString);
                Material material = Material.PAPER;
                if(MenuController.hasCustomItem(dayString, "day")) material = MenuController.getCustomItem(dayString, "day");
                ItemBuilder itemBuilder = new ItemBuilder(material, rs.format(name), rs.format(lore));
                itemBuilder.storeString("year", year);
                itemBuilder.storeString("month", month);
                itemBuilder.storeString("day", dayString);
                itemBuilder.storeString("target", uuid.toString());
                MenuItem menuItem = new MenuItem(itemBuilder.getItemStack(), "menu-day");
                menuItems.add(menuItem);
            }
            String title = MenuController.getDayTitleFormat();
            Menu yMenu = new Menu(rs.format(title), menuItems, monthFolder.listFiles().length, true, true); // get target name if possible
            yMenu.openMenu(player);
        }
    }

    public static void veinMenu(Player player, UUID uuid, String year, String month, String day) {
        // list all veins for the day
        ArrayList<ItemStack> veins = new ArrayList<>();
        File mineLogFile = PlayerFile.getFileForDay(uuid, year, month, day);
        YamlConfiguration log = YamlConfiguration.loadConfiguration(mineLogFile);
        int veinNum = 1;
        Player target = Bukkit.getPlayer(uuid);
        String targetName = uuid.toString();
        if(target != null) targetName = target.getName();
        String date = month + "/" + day + "/" + year;
        ReplacementString rs = new ReplacementString();
        int monthInt = Integer.parseInt(month);
        rs.addReplacement("{target}", targetName);
        rs.addReplacement("{player}", player.getName());
        rs.addReplacement("{year}", year);
        rs.addReplacement("{month}", month);
        rs.addReplacement("{monthShort}", dfs.getShortMonths()[monthInt - 1]);
        rs.addReplacement("{day}", day);
        rs.addReplacement("{date}", date);
        for(String veinID : log.getKeys(false)) {
            ConfigurationSection sec = log.getConfigurationSection(veinID + ".blocks");
            int amt = sec.getKeys(false).size();
            if(amt > 64) amt = 64;
            Material material = Material.PAPER;
            String matString = log.getString(veinID + ".material");
            if(matString != null) material = Material.valueOf(matString);
            String name = MenuController.getVeinNameFormat().replace("{veinID}", veinID).replace("{num}", veinNum + "");
            String lore = MenuController.getVeinLoreFormat().replace("{veinID}", veinID).replace("{num}", veinNum + "");
            ItemBuilder vein = new ItemBuilder(material, amt, rs.format(name), rs.format(lore));
            vein.storeString("year", year);
            vein.storeString("month", month);
            vein.storeString("day", day);
            vein.storeString("target", uuid.toString());
            vein.storeString("ID", veinID);
            veins.add(vein.getItemStack());
            veinNum++;
        }
        String title = MenuController.getVeinTitleFormat();
        Menu veinMenu = new Menu(veins, rs.format(title), "menu-vein", true, true);
        veinMenu.openMenu(player);
    }

    public static void blocksMenu(Player player, UUID uuid, String year, String month, String day, String veinID) {
        ArrayList<ItemStack> blocks = new ArrayList<>();
        File file = PlayerFile.getFileForDay(uuid, year, month, day);
        YamlConfiguration log = YamlConfiguration.loadConfiguration(file);
        ConfigurationSection blockSection = log.getConfigurationSection(veinID + ".blocks");
        Material material = Material.PAPER;
        String matString = log.getString(veinID + ".material");
        if(matString != null) material = Material.valueOf(matString);
        Player target = Bukkit.getPlayer(uuid);
        String targetName = uuid.toString();
        if(target != null) targetName = target.getName();
        int monthInt = Integer.parseInt(month);
        ReplacementString rs = new ReplacementString();
        rs.addReplacement("{veinID}", veinID);
        rs.addReplacement("{target}", targetName);
        rs.addReplacement("{player}", player.getName());
        rs.addReplacement("{year}", year);
        rs.addReplacement("{month}", month);
        rs.addReplacement("{monthShort}", dfs.getShortMonths()[monthInt - 1]);
        rs.addReplacement("{day}", day);
        String title = MenuController.getBlocksTitleFormat();
        String lore = MenuController.getBlocksLoreFormat();
        for(String blockID : blockSection.getKeys(false)) {
            if(blockID == null) continue;
            ReplacementString rs2 = new ReplacementString();
            String light = blockSection.getString(blockID + ".light");
            String time = blockSection.getString(blockID + ".time");
            String location = blockSection.getString(blockID + ".location");
            String[] locations = location.split("@");
            String world = locations[0];
            String[] coords = locations[1].split(",");
            String[] lights = light.split("");
            rs2.addReplacement("{blockLight}", lights[0]);
            rs2.addReplacement("{skyLight}", lights[1].replace("(", "").replace(")", ""));
            rs2.addReplacement("{time}", time);
            rs2.addReplacement("{world}", world);
            rs2.addReplacement("{x}", coords[0]);
            rs2.addReplacement("{y}", coords[1]);
            rs2.addReplacement("{z}", coords[2]);
            ItemBuilder block = new ItemBuilder(material, "&6" + location, rs.format(rs2.format(lore)));
            blocks.add(block.getItemStack());
        }
        Menu blockMenu = new Menu(blocks, rs.format(title), true, true);
        blockMenu.openMenu(player);
    }

    public static String replaceString(String message, HashMap<String, Object> replacements) {
        for(String key : replacements.keySet()) {
            String replacement = replacements.get(key) + "";
            message = message.replace(key, replacement);
        }
        return message;
    }

}

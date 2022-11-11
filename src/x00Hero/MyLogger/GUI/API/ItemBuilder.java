package x00Hero.MyLogger.GUI.API;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ItemBuilder {

    ItemStack item;
    Material material;
    String name;
    ArrayList lore;
    int amount;

    public ItemBuilder(Material MATERIAL, int AMOUNT, String NAME) {
        ItemStack ITEM = new ItemStack(MATERIAL, AMOUNT);
        ItemMeta meta = ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', NAME));
        ITEM.setItemMeta(meta);

        item = ITEM;
        material = MATERIAL;
        name = NAME;
        amount = AMOUNT;
        lore = null;
        /*if(lore.size() > 0) */
    }

    public ItemBuilder(Material MATERIAL, int AMOUNT, String NAME, String LORE) {
        ItemStack ITEM = new ItemStack(MATERIAL, AMOUNT);
        ItemMeta meta = ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', NAME));
        String[] split = LORE.split("\n");
        ArrayList<String> lore2 = new ArrayList<>();
        for(String line : split) {
            lore2.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        if(lore2.size() > 0) meta.setLore(lore2);
        ITEM.setItemMeta(meta);

        item = ITEM;
        material = MATERIAL;
        name = NAME;
        amount = AMOUNT;
        lore = lore2;
        /*if(lore.size() > 0) */
    }

    public ItemBuilder(Material MATERIAL, String NAME, String LORE) {
        ItemStack ITEM = new ItemStack(MATERIAL);
        ItemMeta meta = ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', NAME));
        String[] split = LORE.split("\n");
        ArrayList<String> lore2 = new ArrayList<>();
        for(String line : split) {
            lore2.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        if(lore2.size() > 0 && !LORE.equals("")) meta.setLore(lore2);
        ITEM.setItemMeta(meta);

        item = ITEM;
        material = MATERIAL;
        name = NAME;
        amount = 1;
        lore = lore2;
    }

    public ItemBuilder(Material MATERIAL, String NAME, ArrayList<String> LORE) {
        ItemStack ITEM = new ItemStack(MATERIAL);
        ItemMeta meta = ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', NAME));
        ArrayList<String> lore2 = new ArrayList<>();
        for(String line : LORE) {
            lore2.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        if(lore2.size() > 0) meta.setLore(lore2);
        ITEM.setItemMeta(meta);

        item = ITEM;
        material = MATERIAL;
        name = NAME;
        amount = 1;
        lore = lore2;
    }

    public ItemBuilder(Material MATERIAL, Integer AMOUNT, String NAME, ArrayList<String> LORE) {
        ItemStack ITEM = new ItemStack(MATERIAL, AMOUNT);
        ItemMeta meta = ITEM.getItemMeta();
        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', NAME));
        ArrayList<String> lore2 = new ArrayList<>();
        for(String line : LORE) {
            lore2.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        if(lore2.size() > 0) meta.setLore(lore2);
        ITEM.setItemMeta(meta);

        item = ITEM;
        material = MATERIAL;
        name = NAME;
        amount = AMOUNT;
        lore = lore2;
    }

    public ItemBuilder(ItemStack ITEM) {
        item = ITEM;
        material = ITEM.getType();
        name = ITEM.getItemMeta().getDisplayName();
        amount = ITEM.getAmount();
    }

    public void setItem(ItemStack item) {
        this.item = item;
    }

    public ItemStack getItemStack() {
        return item;
    }

    public String getName() {
        return name;
    }

    public ArrayList<String> getLore() {
        return lore;
    }

    public int getAmount() {
        return amount;
    }

    public Material getMaterial() {
        return material;
    }
}

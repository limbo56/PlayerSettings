package me.limbo56.settings.utils;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lim_bo56
 * On Aug 7, 2016
 * At 2:24:15 AM
 */
public class ItemFactory {

    /**
     * Method used to create an itemstack
     *
     * @param displayName Name of the item
     * @param lore        The lore of the item
     * @param useLore     Boolean for lore
     * @param material    The Material
     * @param amount      Amount of items
     * @param data        Data byte
     * @return ItemStack
     */
    public static ItemStack createItem(String displayName, boolean useLore, Material material, int amount, int data, String... lore) {
        ItemStack item = new ItemStack(material, amount, (byte) data);

        ItemMeta meta = item.getItemMeta();

        meta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES, ItemFlag.HIDE_POTION_EFFECTS, ItemFlag.HIDE_ENCHANTS);
        meta.setDisplayName(displayName);

        if (useLore) {
            List<String> finalLore = meta.hasLore() ? meta.getLore() : new ArrayList<String>();

            for (String s : lore)
                if (s != null)
                    finalLore.add(s.replace("&", "§"));

            meta.setLore(finalLore);
        }

        item.setItemMeta(meta);

        return item;
    }

    public static ItemStack addGlow(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();

        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

        itemStack.setItemMeta(meta);
        return itemStack;
    }
}

package me.limbo56.playersettings.utils;

import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemUtils {

    public static void addGlow(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
    }

    public static void removeGlow(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.removeEnchant(Enchantment.LURE);
        itemStack.setItemMeta(meta);
    }
}

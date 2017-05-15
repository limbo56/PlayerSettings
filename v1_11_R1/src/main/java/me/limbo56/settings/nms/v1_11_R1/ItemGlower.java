package me.limbo56.settings.nms.v1_11_R1;

import me.limbo56.settings.version.IItemGlower;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Created by lim_bo56
 * On 11/22/2016
 * At 8:40 PM
 */
public class ItemGlower implements IItemGlower {

    @Override
    public ItemStack glow(ItemStack itemStack) {
        ItemMeta meta = itemStack.getItemMeta();
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        itemStack.setItemMeta(meta);
        return itemStack;
    }

}

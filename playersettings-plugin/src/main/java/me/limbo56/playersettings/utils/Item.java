package me.limbo56.playersettings.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Builder
@AllArgsConstructor
public class Item {
    private Material material;
    private int amount;
    private String name;
    @Singular(value = "lore")
    private List<String> lore;
    private byte data;

    public static class ItemBuilder {
        public ItemStack build() {
            ItemStack itemStack = new ItemStack(material, amount);
            ItemMeta itemMeta = itemStack.getItemMeta();

            if (amount < 1) amount = 1;
            if (data != -1 && data >= 0)
                itemStack = new ItemStack(material, amount, data);
            if (name != null)
                itemMeta.setDisplayName(ColorUtils.translateString(name));
            if (lore != null)
                itemMeta.setLore(ColorUtils.translateStringList(lore));

            itemStack.setItemMeta(itemMeta);
            return itemStack;
        }
    }
}

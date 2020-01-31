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
    private ItemStack item;
    private Material material;
    private int amount;
    private String name;
    @Singular(value = "lore")
    private List<String> lore;
    @Builder.Default()
    private byte data = -1;

    public static class ItemBuilder {
        public ItemStack build() {
            if (item == null) {
                if (amount < 1) amount = 1;

                if (data != -1 && data >= 0) {
                    item = new ItemStack(material, amount, data);
                } else {
                    item = new ItemStack(material, amount);
                }
            }

            ItemMeta itemMeta = item.getItemMeta();

            if (name != null)
                itemMeta.setDisplayName(ColorUtils.translateString(name));

            if (lore != null)
                itemMeta.setLore(ColorUtils.translateStringList(lore));

            item.setItemMeta(itemMeta);
            return item;
        }
    }
}

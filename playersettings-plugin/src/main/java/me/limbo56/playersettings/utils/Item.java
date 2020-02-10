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
    private byte data;

    public static class ItemBuilder {
        private byte data = -1;

        public ItemStack build() {
            if (this.item == null) {
                if (this.amount < 1) this.amount = 1;

                if (this.data != -1 && this.data >= 0) {
                    this.item = new ItemStack(this.material, this.amount, this.data);
                } else {
                    this.item = new ItemStack(this.material, this.amount);
                }
            }

            ItemMeta itemMeta = this.item.getItemMeta();

            if (this.name != null)
                itemMeta.setDisplayName(ColorUtils.translateString(this.name));

            if (this.lore != null)
                itemMeta.setLore(ColorUtils.translateStringList(this.lore));

            this.item.setItemMeta(itemMeta);
            return this.item;
        }
    }
}

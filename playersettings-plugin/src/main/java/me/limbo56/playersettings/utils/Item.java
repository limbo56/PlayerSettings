package me.limbo56.playersettings.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.Damageable;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

@Builder
@AllArgsConstructor
public class Item {

    private final ItemStack item;
    private final Material material;
    private final int amount;
    private final String name;
    @Singular(value = "lore")
    private final List<String> lore;
    private final byte data;

    public static class ItemBuilder {

        private byte data = -1;

        public ItemStack build() {
            if (this.item == null) {
                if (this.amount < 1) {
                    this.amount = 1;
                }

                this.item = new ItemStack(this.material, this.amount);
            }

            ItemMeta itemMeta = this.item.getItemMeta();

            if (this.name != null) {
                itemMeta.setDisplayName(ColorUtils.translateString(this.name));
            }

            if (this.data != -1 && this.data >= 0) {
                ((Damageable) itemMeta).setDamage(this.data);
            }

            if (this.lore != null) {
                itemMeta.setLore(ColorUtils.translateStringList(this.lore));
            }

            this.item.setItemMeta(itemMeta);
            return this.item;
        }
    }
}

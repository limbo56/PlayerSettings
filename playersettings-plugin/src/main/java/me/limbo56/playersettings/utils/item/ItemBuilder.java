package me.limbo56.playersettings.utils.item;

import com.google.common.base.Preconditions;
import me.limbo56.playersettings.utils.ColorUtils;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ItemBuilder {
    private Material material;
    private int amount;
    private String name;
    private List<String> lore;
    private byte data;

    public ItemBuilder(Material material, int amount) {
        this.material = material;
        this.amount = amount;
        this.data = -1;
    }

    public ItemBuilder(Material material) {
        this(material, 1);
    }

    public ItemBuilder() {
        this(Material.AIR);
    }

    public ItemStack build() {
        ItemStack itemStack = new ItemStack(Material.AIR);
        ItemMeta itemMeta = itemStack.getItemMeta();

        Preconditions.checkNotNull(material);
        itemStack.setType(material);
        itemStack.setAmount(amount);

        if (data != -1 && data >= 0)
            itemStack.getData().setData(data);
        if (name != null)
            itemMeta.setDisplayName(ColorUtils.translateString(name));
        if (lore != null)
            itemMeta.setLore(ColorUtils.translateStringList(lore));

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }

    public ItemBuilder material(Material material) {
        this.material = material;
        return this;
    }

    public ItemBuilder amount(int amount) {
        this.amount = amount;
        return this;
    }

    public ItemBuilder name(String name) {
        this.name = name;
        return this;
    }

    public ItemBuilder lore(String lore) {
        this.lore = Collections.singletonList(lore);
        return this;
    }


    public ItemBuilder lore(String[] lore) {
        this.lore = Arrays.asList(lore);
        return this;
    }

    public ItemBuilder lore(List<String> lore) {
        this.lore = lore;
        return this;
    }

    public ItemBuilder data(byte data) {
        this.data = data;
        return this;
    }
}

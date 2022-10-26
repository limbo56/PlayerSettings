package me.limbo56.playersettings.util;

import com.cryptomorin.xseries.SkullUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class ItemBuilder {
  private ItemStack item;
  private String name;
  private Material material;
  private Integer modelData;
  private String textures;
  private Integer amount;
  private Byte data = -1;
  private List<String> lore = new ArrayList<>();

  public static ItemBuilder builder() {
    return new ItemBuilder();
  }

  public ItemBuilder item(@NotNull ItemStack item) {
    this.item = item;
    return this;
  }

  public ItemBuilder name(@NotNull String name) {
    this.name = name;
    return this;
  }

  public ItemBuilder material(@NotNull Material material) {
    this.material = material;
    return this;
  }

  public ItemBuilder modelData(int modelData) {
    this.modelData = modelData;
    return this;
  }

  public ItemBuilder textures(@NotNull String textures) {
    this.textures = textures;
    return this;
  }

  public ItemBuilder amount(int amount) {
    this.amount = amount;
    return this;
  }

  public ItemBuilder data(byte data) {
    this.data = data;
    return this;
  }

  public ItemBuilder lore(@NotNull String lore) {
    this.lore.add(lore);
    return this;
  }

  public ItemBuilder lore(@NotNull List<String> lore) {
    this.lore = lore;
    return this;
  }

  public ItemStack build() {
    if (this.item == null) {
      this.amount = Math.max(Optional.ofNullable(this.amount).orElse(1), 1);
      if (this.data != null) {
        this.item = new ItemStack(this.material, this.amount, this.data);
      } else {
        this.item = new ItemStack(this.material, this.amount);
      }
    }

    ItemMeta itemMeta = this.item.getItemMeta();
    if (itemMeta != null) {
      if (this.name != null) {
        itemMeta.setDisplayName(this.name);
      }
      if (this.lore != null) {
        itemMeta.setLore(this.lore);
      }
      if (this.textures != null) {
        SkullUtils.applySkin(itemMeta, this.textures);
      }
      if (this.modelData != null) {
        itemMeta.setCustomModelData(this.modelData);
      }
      this.item.setItemMeta(itemMeta);
    }
    return this.item;
  }
}

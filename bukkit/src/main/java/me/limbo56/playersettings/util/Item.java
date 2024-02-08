package me.limbo56.playersettings.util;

import com.cryptomorin.xseries.SkullUtils;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import me.limbo56.playersettings.util.text.TextMessage;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Item {
  public static Item.Builder builder() {
    return new Builder();
  }

  @SafeVarargs
  public static void format(ItemStack original, Function<String, String>... modifiers) {
    format(original, original.getItemMeta(), modifiers);
  }

  @SafeVarargs
  public static void format(
      ItemStack original, ItemMeta templateMeta, Function<String, String>... modifiers) {
    ItemMeta targetMeta = original.getItemMeta();
    if (targetMeta == null || templateMeta == null) {
      return;
    }

    TextMessage.Builder builder = TextMessage.builder().addModifiers(modifiers);
    String displayName = templateMeta.getDisplayName();
    List<String> lore = templateMeta.getLore();

    targetMeta.setDisplayName(builder.from(displayName).getFirstTextLine());
    if (lore != null) {
      targetMeta.setLore(builder.from(lore).getTextLines());
    }

    original.setItemMeta(targetMeta);
  }

  public static class Builder {
    private ItemStack item;
    private String name;
    private Material material;
    private Integer modelData;
    private String textures;
    private Integer amount;
    private Byte data;
    private List<String> lore = new ArrayList<>();

    public Item.Builder item(@NotNull ItemStack item) {
      this.item = item;
      return this;
    }

    public Item.Builder name(@NotNull String name) {
      this.name = name;
      return this;
    }

    public Item.Builder material(@NotNull Material material) {
      this.material = material;
      return this;
    }

    public Item.Builder modelData(int modelData) {
      this.modelData = modelData;
      return this;
    }

    public Item.Builder textures(@NotNull String textures) {
      this.textures = textures;
      return this;
    }

    public Item.Builder amount(int amount) {
      this.amount = amount;
      return this;
    }

    public Item.Builder data(byte data) {
      this.data = data;
      return this;
    }

    public Item.Builder lore(@NotNull String lore) {
      this.lore.add(lore);
      return this;
    }

    public Item.Builder lore(@NotNull List<String> lore) {
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
}

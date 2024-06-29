package me.limbo56.playersettings.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import com.cryptomorin.xseries.profiles.builder.XSkull;
import com.cryptomorin.xseries.profiles.objects.Profileable;
import me.limbo56.playersettings.message.text.Text;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class Item {
  public static Item.Builder builder() {
    return new Builder();
  }

  @SafeVarargs
  public static void format(ItemStack item, Function<String, String>... modifiers) {
    format(item, item.getItemMeta(), modifiers);
  }

  @SafeVarargs
  public static void format(
      ItemStack original, ItemMeta templateMeta, Function<String, String>... modifiers) {
    ItemMeta originalMeta = original.getItemMeta();
    if (originalMeta == null || templateMeta == null) {
      return;
    }

    List<String> lore = templateMeta.getLore();
    originalMeta.setDisplayName(
        Text.create(templateMeta.getDisplayName(), modifiers).getFirstTextLine());
    if (lore != null && !lore.isEmpty()) {
      originalMeta.setLore(Text.create(lore, modifiers).getTextLines());
    } else {
      originalMeta.setLore(null);
    }

    original.setItemMeta(originalMeta);
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
          XSkull.of(itemMeta).profile(Profileable.detect(this.textures)).apply();
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

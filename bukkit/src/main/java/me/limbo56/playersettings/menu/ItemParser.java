package me.limbo56.playersettings.menu;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.base.Preconditions;
import java.util.List;
import me.limbo56.playersettings.util.ColorUtil;
import me.limbo56.playersettings.util.ItemBuilder;
import me.limbo56.playersettings.util.data.Parser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class ItemParser implements Parser<ConfigurationSection, ItemStack> {
  public static final ItemParser ITEM_PARSER = new ItemParser();

  @Override
  public ItemStack parse(ConfigurationSection section) {
    String itemName = section.getName();
    String displayName =
        Preconditions.checkNotNull(
            section.getString("name", null), "Name cannot be null for item: " + itemName);
    String material =
        Preconditions.checkNotNull(
            section.getString("material", null), "Material cannot be null for item: " + itemName);
    ItemStack item =
        Preconditions.checkNotNull(
            XMaterial.matchXMaterial(material).orElse(XMaterial.BEDROCK).parseItem(),
            "Unknown material '" + material + "' for item '" + itemName + "'");
    int amount = section.getInt("amount", 1);
    List<String> lore = section.getStringList("lore");
    return ItemBuilder.builder()
        .item(item)
        .name(ColorUtil.translateColorCodes(displayName))
        .amount(amount)
        .lore(ColorUtil.translateColorCodes(lore))
        .build();
  }
}
package me.limbo56.playersettings.api;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.immutables.value.Value;

/**
 * A {@link ConfigurationSerializable} that contains the display properties of an item which will be
 * displayed in the settings menu.
 */
@Value.Immutable
@Value.Style(defaults = @Value.Immutable(copy = false))
public interface MenuItem extends ConfigurationSerializable {
  /**
   * Gets the {@link ItemStack} defined that will be used to display the setting
   *
   * @return {@link ItemStack} that displays the setting
   */
  @Value.Parameter
  ItemStack getItemStack();

  /**
   * Gets the inventory page number where the setting's {@link ItemStack} is rendered
   *
   * @return inventory page number
   */
  @Value.Parameter
  int getPage();

  /**
   * Gets the inventory slot number where the setting's {@link ItemStack} is rendered
   *
   * @return inventory slot number
   */
  @Value.Parameter
  int getSlot();

  @Override
  default Map<String, Object> serialize() {
    ItemStack itemStack = getItemStack();
    String data = itemStack.getDurability() != 0 ? ":" + itemStack.getDurability() : "";
    ItemMeta itemMeta = itemStack.getItemMeta();
    List<String> lore = itemMeta.getLore();
    String displayName = itemMeta.getDisplayName().replaceAll("§", "&");
    lore.replaceAll(text -> text.replaceAll("§", "&"));

    Map<String, Object> mappedObject = new LinkedHashMap<>();
    mappedObject.put("page", getPage());
    mappedObject.put("slot", getSlot());
    mappedObject.put("name", displayName);
    mappedObject.put("material", itemStack.getType() + data);
    mappedObject.put("amount", itemStack.getAmount());
    mappedObject.put("lore", lore);
    return mappedObject;
  }
}

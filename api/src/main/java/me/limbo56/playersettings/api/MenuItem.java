package me.limbo56.playersettings.api;

import com.cryptomorin.xseries.XItemStack;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an item displayed in a paginated menu. This interface provides methods to retrieve
 * properties of the item.
 */
@Value.Immutable
public interface MenuItem extends ConfigurationSerializable {
  /**
   * Retrieves the {@link ItemStack} used to display this menu item.
   *
   * @return The {@link ItemStack} used to display the item.
   */
  @Value.Parameter
  ItemStack getItemStack();

  /**
   * Retrieves the page number where this item is rendered in the menu inventory.
   *
   * @return The page number of the menu inventory.
   */
  @Value.Parameter
  int getPage();

  /**
   * Retrieves the slot number where this item is rendered in the menu inventory.
   *
   * @return The slot number of the menu inventory.
   */
  @Value.Parameter
  int getSlot();

  /**
   * Serializes the menu item into a map for storage or transmission. The serialized map contains
   * the page number, slot number, and item stack details.
   *
   * @return A map representing the serialized state of the menu item.
   */
  @Override
  default @NotNull Map<String, Object> serialize() {
    Map<String, Object> mappedMenuItem = new LinkedHashMap<>();
    mappedMenuItem.put("page", getPage());
    mappedMenuItem.put("slot", getSlot());
    mappedMenuItem.putAll(XItemStack.serialize(getItemStack()));
    return mappedMenuItem;
  }
}

package me.limbo56.playersettings.api;

import com.cryptomorin.xseries.XItemStack;
import java.util.LinkedHashMap;
import java.util.Map;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

/**
 * A {@link ConfigurationSerializable} that contains the display properties of an item which will be
 * displayed in the settings menu.
 */
@Value.Immutable
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
  default @NotNull Map<String, Object> serialize() {
    Map<String, Object> mappedItemStack = XItemStack.serialize(getItemStack());
    Map<String, Object> mappedMenuItem = new LinkedHashMap<>(mappedItemStack);
    mappedMenuItem.put("page", getPage());
    mappedMenuItem.put("slot", getSlot());
    return mappedMenuItem;
  }

  static MenuItem deserialize(ConfigurationSection section) {
    int page = section.getInt("page", 1);
    int slot = section.getInt("slot");
    return ImmutableMenuItem.builder()
        .itemStack(XItemStack.deserialize(section))
        .page(page)
        .slot(slot)
        .build();
  }
}

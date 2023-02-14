package me.limbo56.playersettings.api;

import com.cryptomorin.xseries.XItemStack;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link ConfigurationSerializable} that contains the display properties of an item which will be
 * displayed in a paginated menu.
 */
@Value.Immutable
public interface MenuItem extends ConfigurationSerializable {
  static MenuItem deserialize(Map<String, Object> menuItem) {
    int page = (int) menuItem.getOrDefault("page", 1);
    int slot = (int) menuItem.getOrDefault("slot", 0);
    return ImmutableMenuItem.builder()
        .itemStack(XItemStack.deserialize(menuItem))
        .page(page)
        .slot(slot)
        .build();
  }

  static MenuItem deserialize(ConfigurationSection configurationSection) {
    Map<String, Object> menuItem =
        configurationSection.getKeys(true).stream()
            .collect(Collectors.toMap(key -> key, configurationSection::get));
    return deserialize(menuItem);
  }

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
    Map<String, Object> mappedMenuItem = new LinkedHashMap<>(XItemStack.serialize(getItemStack()));
    mappedMenuItem.put("slot", getSlot());
    mappedMenuItem.put("page", getPage());
    return mappedMenuItem;
  }
}

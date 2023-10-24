package me.limbo56.playersettings.menu.item;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.actions.MenuItemAction;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SettingsMenuItem implements MenuItem {
  private final MenuItem menuItem;
  private final MenuItemAction clickAction;

  public SettingsMenuItem(MenuItem menuItem, MenuItemAction clickAction) {
    this.menuItem = menuItem;
    this.clickAction = clickAction;
  }

  public static MenuItem deserialize(
      @NotNull ConfigurationSection configurationSection, int value) {
    Map<String, Object> menuItem = new HashMap<>();
    String name = configurationSection.getString("name", null);

    for (String key : configurationSection.getKeys(false)) {
      if (configurationSection.contains(key)
          && menuItem.put(key, configurationSection.get(key)) != null) {
        throw new IllegalStateException(
            "Duplicate key found '" + key + "' deserializing '" + name + "'");
      }
    }

    ConfigurationSection material = configurationSection.getConfigurationSection("material");
    ConfigurationSection customModelData =
        configurationSection.getConfigurationSection("custom-model-data");
    if (material != null) applyValueBasedModification(menuItem, material, value);
    if (customModelData != null) applyValueBasedModification(menuItem, customModelData, value);

    return MenuItem.deserialize(menuItem);
  }

  private static void applyValueBasedModification(
      Map<String, Object> menuItem, @NotNull ConfigurationSection section, int value) {
    Set<String> sections = section.getKeys(false);
    if (sections.isEmpty()) {
      return;
    }

    int valueInteger = sections.contains(String.valueOf(value)) ? value : 0;
    String valueKey = String.valueOf(valueInteger);
    menuItem.put(section.getName(), section.get(valueKey));
  }

  public void executeClickAction(ClickType type, SettingUser user) {
    clickAction.execute(this, type, user);
  }

  @Override
  public ItemStack getItemStack() {
    return menuItem.getItemStack();
  }

  @Override
  public int getPage() {
    return menuItem.getPage();
  }

  @Override
  public int getSlot() {
    return menuItem.getSlot();
  }
}

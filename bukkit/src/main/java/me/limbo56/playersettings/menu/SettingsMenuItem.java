package me.limbo56.playersettings.menu;

import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.actions.MenuItemAction;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class SettingsMenuItem implements MenuItem {
  private final MenuItem menuItem;
  private final MenuItemAction clickAction;

  public SettingsMenuItem(MenuItem menuItem, MenuItemAction clickAction) {
    this.menuItem = menuItem;
    this.clickAction = clickAction;
  }

  public static MenuItem deserialize(ConfigurationSection configurationSection, int settingValue) {
    Map<String, Object> menuItem =
        configurationSection.getKeys(false).stream()
            .collect(Collectors.toMap(key -> key, configurationSection::get));

    ConfigurationSection material = configurationSection.getConfigurationSection("material");
    ConfigurationSection customModelData =
        configurationSection.getConfigurationSection("custom-model-data");
    if (material != null) applyValueBasedModification(menuItem, material, settingValue);
    if (customModelData != null)
      applyValueBasedModification(menuItem, customModelData, settingValue);

    return MenuItem.deserialize(menuItem);
  }

  private static void applyValueBasedModification(
      Map<String, Object> menuItem, ConfigurationSection section, int value) {
    Set<String> sections = section.getKeys(false);
    if (sections.size() > 0) {
      String valueKey = String.valueOf(sections.contains(String.valueOf(value)) ? value : 0);
      menuItem.put(section.getName(), section.getString(valueKey));
    }
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

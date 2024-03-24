package me.limbo56.playersettings.configuration.parser;

import com.cryptomorin.xseries.XItemStack;
import java.util.Map;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.util.Configurations;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuItemParser implements ConfigurationSectionParser<MenuItem> {
  @Override
  public MenuItem parse(ConfigurationSection section) {
    try {
      return parse(Configurations.mapSection(section));
    } catch (Exception exception) {
      throw new ConfigurationSectionParseException(
          "An error occurred while parsing item `" + section.getName() + "` from `items.yml`",
          section,
          exception);
    }
  }

  public MenuItem parse(@NotNull Map<String, Object> map) {
    int page = (int) map.getOrDefault("page", 1);
    int slot = (int) map.getOrDefault("slot", 0);
    ItemStack itemStack = XItemStack.deserialize(map);
    return ImmutableMenuItem.builder().itemStack(itemStack).page(page).slot(slot).build();
  }
}

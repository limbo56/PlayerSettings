package me.limbo56.playersettings.configuration.parser;

import com.cryptomorin.xseries.XItemStack;
import java.util.Map;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.util.Configurations;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class MenuItemParser implements ConfigurationSectionParser<MenuItem> {
  @Override
  public MenuItem parse(ConfigurationSection section) {
    return parse(Configurations.mapSection(section));
  }

  public MenuItem parse(@NotNull Map<String, Object> map) {
    int page = (int) map.getOrDefault("page", 1);
    int slot = (int) map.getOrDefault("slot", 0);
    return ImmutableMenuItem.builder()
        .itemStack(XItemStack.deserialize(map))
        .page(page)
        .slot(slot)
        .build();
  }
}

package me.limbo56.playersettings.configuration.parser;

import java.util.Map;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.util.Configurations;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class SettingItemParser extends MenuItemParser {
  @Override
  public MenuItem parse(@NotNull Map<String, Object> map) {
    return parse(map, 0);
  }

  public MenuItem parse(@NotNull ConfigurationSection section, int value) {
    return parse(Configurations.mapSection(section), value);
  }

  public MenuItem parse(@NotNull Map<String, Object> map, int value) {
    applyValueBasedModification(map, "material", value);
    applyValueBasedModification(map, "custom-model-data", value);
    return super.parse(map);
  }

  private void applyValueBasedModification(Map<String, Object> map, String property, int value) {
    Object modification = map.get(property);
    if (map.isEmpty() || modification == null) {
      return;
    }

    if (modification instanceof Map<?, ?>) {
      Map<String, Object> modificationMap = (Map<String, Object>) modification;
      String key =
          modificationMap.containsKey(String.valueOf(value))
              ? String.valueOf(value)
              : String.valueOf(0);
      map.put(property, modificationMap.get(key));
    }
  }
}

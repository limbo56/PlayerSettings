package me.limbo56.playersettings.configuration;

import com.google.common.collect.ListMultimap;
import me.limbo56.playersettings.api.setting.Setting;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class PluginConfiguration extends BaseConfiguration {
  @Override
  @NotNull
  String getFileName() {
    return "config.yml";
  }

  public boolean hasMetricsEnabled() {
    return getFile().getBoolean("general.metrics");
  }

  public boolean hasDebugEnabled() {
    return getFile().getBoolean("general.debug");
  }

  public boolean hasUpdateAlertsEnabled() {
    return getFile().getBoolean("general.update-alert");
  }

  public boolean isAllowedWorld(String name) {
    List<String> worldList = getFile().getStringList("general.worlds");
    return worldList.contains(name) || worldList.contains("*");
  }

  public boolean isToggleButtonEnabled() {
    return getFile().getBoolean("menu.toggle-button");
  }

  public String getToggleOnSound() {
    return getFile().getString("menu.sounds.setting-toggle-on");
  }

  @Nullable
  public ListMultimap<String, Integer> getValueAliases() {
    ConfigurationSection generalSection = getFile().getConfigurationSection("general");
    return generalSection == null ? null : Setting.deserializeValueAliases(generalSection);
  }

  public String getToggleOffSound() {
    return getFile().getString("menu.sounds.setting-toggle-off");
  }
}

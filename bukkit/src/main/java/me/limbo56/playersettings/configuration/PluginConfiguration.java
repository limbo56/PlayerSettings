package me.limbo56.playersettings.configuration;

import com.google.common.collect.ImmutableListMultimap;
import java.util.List;
import java.util.Optional;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.database.SettingsDatabase;
import me.limbo56.playersettings.setting.Settings;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class PluginConfiguration extends BaseConfiguration {
  public PluginConfiguration(PlayerSettings plugin) {
    super(plugin);
  }

  @Override
  @NotNull
  String getFileName() {
    return "config.yml";
  }

  public boolean isAllowedWorld(String name) {
    List<String> worldList = configuration.getStringList("general.worlds");
    return worldList.contains(name) || worldList.contains("*");
  }

  public boolean isToggleButtonEnabled() {
    return configuration.getBoolean("menu.toggle-button", true);
  }

  public boolean isCommandEnabled(String command) {
    return configuration.getBoolean("commands." + command + ".enabled", true);
  }

  public boolean hasMetricsEnabled() {
    return configuration.getBoolean("general.metrics", true);
  }

  public boolean hasDebugEnabled() {
    return configuration.getBoolean("general.debug", false);
  }

  public boolean hasOfflineWarningEnabled() {
    return configuration.getBoolean("general.offline-warning", true);
  }

  public boolean hasUpdateAlertsEnabled() {
    return configuration.getBoolean("general.update-alert", true);
  }

  public long getSettingsSaveDelay() {
    return configuration.getLong("general.settings-save-delay", 500);
  }

  public long getFlightStateSaveDelay() {
    return configuration.getLong("general.flight-state-save-delay", 600);
  }

  public List<String> getNPCMetadata() {
    return configuration.getStringList("general.npc-metadata");
  }

  public String getToggleOnSound() {
    return configuration.getString("menu.sounds.setting-toggle-on");
  }

  public String getToggleOffSound() {
    return configuration.getString("menu.sounds.setting-toggle-off");
  }

  public ImmutableListMultimap<String, Integer> getDefaultValueAliases() {
    return Optional.ofNullable(
            Parsers.VALUE_ALIASES_PARSER.parse(
                configuration.getConfigurationSection("general.value-aliases")))
        .orElse(Settings.Constants.DEFAULT_VALUE_ALIASES);
  }

  public String getDefaultCommand() {
    return configuration.getString("commands.default", "OPEN");
  }

  public String getChatEnableCommand() {
    return configuration.getString("commands.chat-enable", "playersettings:settings");
  }

  public SettingsDatabase<?> getSettingsDatabase() {
    ConfigurationSection storageSection = configuration.getConfigurationSection("storage");
    if (storageSection == null) {
      throw new NullPointerException(
          "Empty or missing properties in the 'storage' section inside 'config.yml'");
    }
    return Parsers.SETTING_DATABASE_PARSER.parse(storageSection);
  }
}

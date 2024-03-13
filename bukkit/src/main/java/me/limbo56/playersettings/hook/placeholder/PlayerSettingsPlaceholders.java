package me.limbo56.playersettings.hook.placeholder;

import java.util.Optional;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.setting.InternalSetting;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerSettingsPlaceholders extends PlaceholderExpansion {
  private static final String IDENTIFIER = "playersettings";
  private static final String AUTHOR = "lim_bo56";

  private final PlayerSettings plugin;

  public PlayerSettingsPlaceholders(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  @Override
  public @NotNull String getAuthor() {
    return AUTHOR;
  }

  @Override
  public @NotNull String getIdentifier() {
    return IDENTIFIER;
  }

  @Override
  public @NotNull String getVersion() {
    return plugin.getDescription().getVersion();
  }

  @Override
  public boolean persist() {
    return true; // This is required or else PlaceholderAPI will unregister the Expansion on reload
  }

  @Override
  public String onRequest(OfflinePlayer player, @NotNull String identifier) {
    return inferOfflinePlaceholders(identifier).orElse(super.onRequest(player, identifier));
  }

  @Override
  public @Nullable String onPlaceholderRequest(Player player, @NotNull String identifier) {
    // Placeholder is unknown by the Expansion
    if (player == null) {
      return null;
    }

    return inferOnlinePlaceholders(player, identifier).orElse(null);
  }

  private Optional<String> inferOfflinePlaceholders(String identifier) {
    Setting setting = findTargetSetting(identifier).orElse(null);

    if (setting != null) {
      Optional<@NotNull String> settingName = Optional.of(setting.getDisplayName());

      if (identifier.endsWith("_name")) {
        return settingName;
      }

      if (identifier.endsWith("_name_plain")) {
        return settingName.map(ChatColor::stripColor);
      }
    }

    return Optional.empty();
  }

  private Optional<String> inferOnlinePlaceholders(Player player, @NotNull String params) {
    if (params.endsWith("_in_allowed_world")) {
      return Optional.of(
          String.valueOf(plugin.getConfiguration().isAllowedWorld(player.getWorld().getName())));
    }

    InternalSetting setting = findTargetSetting(params).orElse(null);
    if (setting != null) {
      int value = getSettingValue(player, setting);
      if (params.endsWith("_value")) {
        return Optional.of(String.valueOf(value));
      }

      int toggleValue = value < 1 ? 1 : -value;
      if (params.endsWith("_toggle")) {
        return Optional.of(String.valueOf(toggleValue));
      }

      if (params.endsWith("_value_formatted")) {
        return Optional.of(setting.getValueAlias(value));
      }

      if (params.endsWith("_toggle_formatted")) {
        return Optional.of(setting.getValueAlias(toggleValue));
      }
    }

    return Optional.empty();
  }

  @NotNull
  private Optional<InternalSetting> findTargetSetting(@NotNull String identifier) {
    for (InternalSetting setting : plugin.getSettingsManager().getSettings()) {
      if (identifier.startsWith(setting.getName())) {
        return Optional.of(setting);
      }
    }
    return Optional.empty();
  }

  private int getSettingValue(Player player, Setting setting) {
    return plugin
        .getUserManager()
        .getSettingWatcher(player.getUniqueId())
        .getValue(setting.getName());
  }
}

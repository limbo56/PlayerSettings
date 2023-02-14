package me.limbo56.playersettings;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

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
  public String onRequest(OfflinePlayer player, @NotNull String params) {
    return inferGlobalPlaceholders(params).orElse(super.onRequest(player, params));
  }

  @Override
  public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
    if (player == null) return null; // Placeholder is unknown by the Expansion
    return inferPlayerPlaceholders(player, params)
        .orElse(inferPlayerSettingValuePlaceholders(player, params).orElse(null));
  }

  private Optional<String> inferGlobalPlaceholders(String params) {
    Optional<String> targetSetting =
        plugin.getSettingsManager().getSettingNames().stream().filter(params::startsWith).findAny();
    if (!targetSetting.isPresent()) {
      return Optional.empty();
    }

    Setting setting = plugin.getSettingsManager().getSetting(targetSetting.get());
    Optional<ItemMeta> itemMeta =
        Optional.ofNullable(setting.getItem().getItemStack().getItemMeta());
    if (params.endsWith("_name")) {
      return Optional.ofNullable(itemMeta.map(ItemMeta::getDisplayName).orElse(setting.getName()));
    }

    if (params.endsWith("_name_plain")) {
      return Optional.ofNullable(
          itemMeta
              .map(meta -> ChatColor.stripColor(meta.getDisplayName()))
              .orElse(setting.getName()));
    }

    return Optional.empty();
  }

  private @NotNull Optional<String> inferPlayerPlaceholders(Player player, @NotNull String params) {
    if (params.endsWith("_in_allowed_world")) {
      String worldName = player.getWorld().getName();
      boolean inAllowedWorld = plugin.getPluginConfiguration().isAllowedWorld(worldName);
      return Optional.of(String.valueOf(inAllowedWorld));
    }
    return Optional.empty();
  }

  private @NotNull Optional<String> inferPlayerSettingValuePlaceholders(
      Player player, @NotNull String params) {
    Optional<String> targetSetting =
        plugin.getSettingsManager().getSettingNames().stream().filter(params::startsWith).findAny();
    if (!targetSetting.isPresent()) {
      return Optional.empty();
    }

    SettingWatcher settingWatcher =
        plugin.getUserManager().getUser(player.getUniqueId()).getSettingWatcher();
    Setting setting = plugin.getSettingsManager().getSetting(targetSetting.get());
    int settingValue = settingWatcher.getValue(setting.getName());
    int toggleValue = settingValue < 1 ? 1 : -settingValue;
    if (params.endsWith("_toggle")) {
      return Optional.of(String.valueOf(toggleValue));
    }
    if (params.endsWith("_toggle_formatted")) {
      return Optional.of(
          plugin.getSettingsConfiguration().formatSettingValue(setting, toggleValue));
    }

    if (params.endsWith("_value")) {
      return Optional.of(String.valueOf(settingValue));
    }
    if (params.endsWith("_value_formatted")) {
      return Optional.of(
          plugin.getSettingsConfiguration().formatSettingValue(setting, settingValue));
    }

    return Optional.empty();
  }
}

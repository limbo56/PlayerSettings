package me.limbo56.playersettings;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
  public String onRequest(OfflinePlayer player, String params) {
    List<String> settingNames = getSettingNameList();
    Optional<String> targetSetting = settingNames.stream().filter(params::startsWith).findAny();
    if (targetSetting.isPresent()) {
      Setting setting = plugin.getSettingsManager().getSetting(targetSetting.get());

      if (params.endsWith("_name")) {
        return Optional.ofNullable(setting.getItem().getItemStack().getItemMeta())
            .map(ItemMeta::getDisplayName)
            .orElse(setting.getName());
      }

      if (params.endsWith("_name_plain")) {
        return Optional.ofNullable(setting.getItem().getItemStack().getItemMeta())
            .map(meta -> ChatColor.stripColor(meta.getDisplayName()))
            .orElse(setting.getName());
      }
    }

    return super.onRequest(player, params);
  }

  @Override
  public @Nullable String onPlaceholderRequest(Player player, @NotNull String params) {
    if (player == null) {
      return null; // Placeholder is unknown by the Expansion
    }

    if (params.endsWith("_in_allowed_world")) {
      String worldName = player.getWorld().getName();
      return String.valueOf(PlayerSettingsProvider.isAllowedWorld(worldName));
    }

    List<String> settingNames = getSettingNameList();
    Optional<String> targetSetting = settingNames.stream().filter(params::startsWith).findAny();
    if (targetSetting.isPresent()) {
      Setting setting = plugin.getSettingsManager().getSetting(targetSetting.get());
      SettingUser user = plugin.getUserManager().getUser(player.getUniqueId());
      SettingWatcher settingWatcher = user.getSettingWatcher();

      if (params.endsWith("_toggle")) {
        return String.valueOf(user.hasSettingEnabled(setting.getName()));
      }

      if (params.endsWith("_value")) {
        return String.valueOf(settingWatcher.getValue(setting.getName()));
      }

      if (params.endsWith("_value_formatted")) {
        return PlayerSettingsProvider.formatSettingValue(
            settingWatcher.getValue(setting.getName()));
      }
    }

    return null; // Placeholder is unknown by the Expansion
  }

  @NotNull
  private List<String> getSettingNameList() {
    return plugin.getSettingsManager().getSettingMap().values().stream()
        .map(Setting::getName)
        .collect(Collectors.toList());
  }
}

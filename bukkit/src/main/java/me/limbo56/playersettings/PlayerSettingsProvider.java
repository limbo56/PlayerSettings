package me.limbo56.playersettings;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.util.Permissions;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

public final class PlayerSettingsProvider {
  private static PlayerSettings plugin;

  public static PlayerSettings getPlugin() {
    return plugin;
  }

  public static void setPlugin(PlayerSettings plugin) {
    PlayerSettingsProvider.plugin = plugin;
  }

  public static Integer parseSettingValue(String value) {
    if ("on".equalsIgnoreCase(value)) {
      return 1;
    } else if ("off".equalsIgnoreCase(value)) {
      return 0;
    }
    try {
      return Integer.parseInt(value);
    } catch (NumberFormatException e) {
      return null;
    }
  }

  public static String formatSettingValue(int value) {
    return value == 1 ? "on" : value < 1 ? "off" : String.valueOf(value);
  }

  public static void configureCustomSetting(Setting setting) {
    String name = setting.getName();
    SettingsConfiguration settingsConfiguration = plugin.getSettingsConfiguration();
    ItemsConfiguration itemsConfiguration = plugin.getItemsConfiguration();
    if (settingsConfiguration.isSettingConfigured(name)
        && itemsConfiguration.isItemConfigured(name)) {
      return;
    }

    if (!settingsConfiguration.isSettingConfigured(name)) {
      settingsConfiguration.configureSetting(setting);
    }

    if (!itemsConfiguration.isItemConfigured(name)) {
      itemsConfiguration.configureItem(name, setting.getItem());
    }
  }

  public static boolean isAllowedWorld(String name) {
    List<String> worldList = plugin.getPluginConfiguration().getStringList("general.worlds");
    return worldList.contains(name) || worldList.contains("*");
  }

  public static boolean hasMetricsEnabled() {
    return getPlugin().getPluginConfiguration().getBoolean("general.metrics");
  }

  public static boolean isUpdateMessageEnabled() {
    return getPlugin().getPluginConfiguration().getBoolean("general.update-alert");
  }

  public static String getMessagePrefix() {
    return plugin.getMessagesConfiguration().getString("prefix", "");
  }

  public static boolean isToggleButtonEnabled() {
    return plugin.getPluginConfiguration().getBoolean("menu.toggle-button");
  }

  public static String getToggleOnSound() {
    return plugin.getPluginConfiguration().getString("menu.sounds.setting-toggle-on");
  }

  public static String getToggleOffSound() {
    return plugin.getPluginConfiguration().getString("menu.sounds.setting-toggle-off");
  }

  public static int getSettingPermissionLevel(CommandSender sender, Setting setting) {
    String permission = "playersettings." + setting.getName().toLowerCase();
    int defaultValue = sender.hasPermission(permission) ? 1 : setting.getDefaultValue();
    return Permissions.getPermissionLevel(sender, permission, defaultValue);
  }

  @NotNull
  public static Set<String> getAllowedSettings(UUID uuid) {
    return getPlugin().getSettingsManager().getSettingMap().keySet().stream()
        .filter(getPlugin().getUserManager().getUser(uuid)::hasSettingPermissions)
        .collect(Collectors.toSet());
  }

  @NotNull
  public static Set<String> getAllowedLevels(Setting setting, int level) {
    return IntStream.range(0, setting.getMaxValue() + 1)
        .filter(value -> value <= level)
        .mapToObj(PlayerSettingsProvider::formatSettingValue)
        .collect(Collectors.toSet());
  }
}

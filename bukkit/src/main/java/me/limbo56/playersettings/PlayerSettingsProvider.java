package me.limbo56.playersettings;

import static me.limbo56.playersettings.settings.SettingValue.SETTING_VALUE;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.util.PermissionUtil;
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

  public static boolean isAllowedWorld(String name) {
    List<String> worldList = plugin.getPluginConfiguration().getStringList("general.worlds");
    return worldList.contains(name) || worldList.contains("*");
  }

  public static boolean isSettingConfigured(String settingName) {
    return plugin.getSettingsConfiguration().hasSetting(settingName)
        && plugin.getItemsConfiguration().contains(settingName);
  }

  public static String getMessagePrefix() {
    return plugin.getMessagesConfiguration().getString("prefix", "");
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
    return PermissionUtil.getPermissionLevel(sender, permission, defaultValue);
  }

  @NotNull
  public static Set<String> getAllowedSettings(UUID uuid) {
    return getPlugin().getSettingsContainer().getSettingMap().keySet().stream()
        .filter(getPlugin().getUserManager().getUser(uuid)::hasSettingPermissions)
        .collect(Collectors.toSet());
  }

  @NotNull
  public static Set<String> getAllowedLevels(Setting setting, int level) {
    return IntStream.range(0, setting.getMaxValue() + 1)
        .filter(value -> value <= level)
        .mapToObj(SETTING_VALUE::format)
        .collect(Collectors.toSet());
  }

  public static boolean isUpdateMessageEnabled() {
    return getPlugin().getPluginConfiguration().getBoolean("general.version-alert");
  }
}

package me.limbo56.playersettings.user;

import com.cryptomorin.xseries.XSound;
import java.util.Objects;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.message.Messenger;
import me.limbo56.playersettings.message.text.ReplaceModifier;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.util.Permissions;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class UserSettingsModifier {
  private final SettingsManager settingsManager;
  private final Messenger messenger;
  private final PluginConfiguration pluginConfiguration;
  private final SettingsConfiguration settingsConfiguration;
  private final MessagesConfiguration messagesConfiguration;
  private final SettingUser user;

  public UserSettingsModifier(SettingUser user) {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.settingsManager = plugin.getSettingsManager();
    this.messenger = plugin.getMessenger();
    this.pluginConfiguration = plugin.getConfiguration();
    this.settingsConfiguration =
        plugin.getConfigurationManager().getConfiguration(SettingsConfiguration.class);
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
    this.user = user;
  }

  public void setValue(@NotNull String settingName, int value, boolean silent) {
    Player player = user.getPlayer();
    SettingWatcher settingWatcher = user.getSettingWatcher();
    if (!hasSettingPermissions(settingName)) {
      return;
    }

    InternalSetting setting = settingsManager.getSetting(settingName);
    int currentValue = settingWatcher.getValue(settingName);
    if (isSettingUnchanged(player, setting, currentValue, value)) {
      return;
    }

    String disablePermission = setting.getDisablePermission();
    if (value <= 0 && disablePermission != null && !player.hasPermission(disablePermission)) {
      messenger.sendMessage(player, messagesConfiguration.getMessage("settings.no-disable-access"));
      return;
    }

    int playerMaxValue = Permissions.getSettingPermissionLevel(player, setting);
    if (!isWithinAbsoluteRange(value, playerMaxValue)) {
      sendLowAccessLevelMessage(player, setting, playerMaxValue);
      return;
    }

    int settingMaxValue = setting.getMaxValue();
    if (!isWithinAbsoluteRange(value, settingMaxValue)) {
      sendInvalidLevelMessage(player, setting);
      return;
    }

    settingWatcher.setValue(settingName, value, silent);
    if (!silent) {
      sendSettingChangedMessage(player, setting, value);
      playSettingToggleSound(player, setting, currentValue, value);
    }
  }

  private boolean hasSettingPermissions(String settingName) {
    if (!user.hasSettingPermissions(settingName)) {
      messenger.sendMessage(
          user.getPlayer(), messagesConfiguration.getMessage("settings.no-access"));
      return false;
    }
    return true;
  }

  private boolean isSettingUnchanged(
      Player player, InternalSetting setting, int currentValue, int newValue) {
    if (newValue == currentValue) {
      sendSettingUnchangedMessage(player, setting, currentValue);
      return true;
    }
    return false;
  }

  private void sendSettingUnchangedMessage(
      Player player, InternalSetting setting, int currentValue) {
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("commands.setting-unchanged"),
        ReplaceModifier.of("%setting%", setting.getDisplayName()),
        ReplaceModifier.of("%value%", setting.getValueAlias(currentValue)));
  }

  private boolean isWithinAbsoluteRange(int current, int max) {
    return Math.abs(current) <= max;
  }

  private void sendLowAccessLevelMessage(
      Player player, InternalSetting setting, int playerMaxLevel) {
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("settings.low-access-level"),
        ReplaceModifier.of("%setting%", setting.getDisplayName()),
        ReplaceModifier.of("%max%", String.valueOf(playerMaxLevel)));
  }

  private void sendInvalidLevelMessage(Player player, InternalSetting setting) {
    messenger.sendMessage(
        player, messenger.getMessageProvider().getInvalidSettingLevelMessage(player, setting));
  }

  private void sendSettingChangedMessage(Player player, InternalSetting setting, int value) {
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("commands.setting-changed"),
        ReplaceModifier.of("%setting%", setting.getDisplayName()),
        ReplaceModifier.of("%value%", setting.getValueAlias(value)));
  }

  private void playSettingToggleSound(
      Player player, Setting setting, int previousValue, int newValue) {
    String toggleSound = getSettingToggleSound(setting, newValue > previousValue);
    Objects.requireNonNull(
            XSound.parse(toggleSound),
            "Invalid sound '" + toggleSound + "' for setting '" + setting.getName() + "'")
        .soundPlayer()
        .forPlayers(player)
        .play();
  }

  @NotNull
  private String getSettingToggleSound(Setting setting, boolean enabled) {
    ConfigurationSection overrides =
        settingsConfiguration.getSettingOverridesSection(setting.getName());
    String defaultToggleSound = getDefaultToggleSound(enabled);

    if (overrides == null) {
      return defaultToggleSound;
    } else {
      String soundPath = enabled ? "setting-toggle-on" : "setting-toggle-off";
      String overrideSound = overrides.getString("sounds." + soundPath);
      return overrideSound == null ? defaultToggleSound : overrideSound;
    }
  }

  private String getDefaultToggleSound(boolean enabled) {
    return enabled
        ? pluginConfiguration.getToggleOnSound()
        : pluginConfiguration.getToggleOffSound();
  }
}

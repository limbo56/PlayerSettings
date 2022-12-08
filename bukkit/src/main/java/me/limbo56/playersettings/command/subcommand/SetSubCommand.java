package me.limbo56.playersettings.command.subcommand;

import com.cryptomorin.xseries.XSound;
import com.google.common.collect.Range;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class SetSubCommand extends SubCommand {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public SetSubCommand() {
    super("set", "Sets the value of a setting", "<setting> <value>", 3, null);
  }

  @Override
  protected void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    // Check if the setting provided is valid
    String settingName = args[1];
    Player player = (Player) sender;
    Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
    if (setting == null) {
      Text.fromMessages("commands.setting-not-found")
          .placeholder("%setting%", settingName)
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    // Check if the value provided is valid
    String value = args[2];
    Integer parsedValue = PlayerSettingsProvider.parseSettingValue(value);
    if (parsedValue == null) {
      Text.fromMessages("commands.setting-invalid-value")
          .placeholder("%max%", String.valueOf(setting.getMaxValue()))
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    // Check if user is not loading
    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (user.isLoading()) {
      Text.fromMessages("settings.wait-loading")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    // Check if user can change the setting
    SettingWatcher watcher = user.getSettingWatcher();
    if (!this.canChangeSettingToLevel(user, watcher, setting, parsedValue)) {
      return;
    }

    // Change setting value and play sound
    int previousValue = watcher.getValue(settingName);
    watcher.setValue(settingName, parsedValue, false);
    playSettingToggleSound(setting, player, previousValue, parsedValue);

    // Send setting change message
    String placeholderName =
        ChatColor.stripColor(setting.getItem().getItemStack().getItemMeta().getDisplayName());
    String placeholderValue = PlayerSettingsProvider.formatSettingValue(parsedValue);
    Text.fromMessages("commands.setting-changed")
        .placeholder("%setting%", placeholderName)
        .placeholder("%value%", placeholderValue)
        .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    final List<String> completions = new ArrayList<>();
    // Show setting names in completions
    if (args.length < 3) {
      UUID uuid = ((Player) sender).getUniqueId();
      Set<String> settingNames = PlayerSettingsProvider.getAllowedSettings(uuid);
      StringUtil.copyPartialMatches(args[1], settingNames, completions);
    }

    // Show allowed values in completions
    if (args.length > 2 && PLUGIN.getSettingsManager().isSettingLoaded(args[1])) {
      Setting setting = PLUGIN.getSettingsManager().getSetting(args[1]);
      int permissionLevel = PlayerSettingsProvider.getSettingPermissionLevel(sender, setting);
      Set<String> settingValues = PlayerSettingsProvider.getAllowedLevels(setting, permissionLevel);
      StringUtil.copyPartialMatches(args[2], settingValues, completions);
    }

    Collections.sort(completions);
    return completions;
  }

  private boolean canChangeSettingToLevel(
      SettingUser user, SettingWatcher watcher, Setting setting, int level) {
    Player player = user.getPlayer();

    // Check if the value was changed
    String settingName = setting.getName();
    if (level == watcher.getValue(settingName)) {
      Text.fromMessages("commands.setting-unchanged")
          .placeholder("%setting%", settingName)
          .placeholder("%value%", PlayerSettingsProvider.formatSettingValue(level))
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    // Allow change to default setting value
    if (level == setting.getDefaultValue()) {
      return true;
    }

    // Ignore players who don't have permission to change the setting
    if (!user.hasSettingPermissions(settingName)) {
      Text.fromMessages("settings.no-access")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    // Ignore values higher than the player's max permission level
    int playerMaxLevel = PlayerSettingsProvider.getSettingPermissionLevel(player, setting);
    if (!Range.closed(-playerMaxLevel, playerMaxLevel).contains(level)) {
      Text.fromMessages("settings.low-access-level")
          .placeholder("%setting%", settingName)
          .placeholder("%max%", String.valueOf(playerMaxLevel))
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    // Ignore levels out of the setting's value range
    int settingMaxLevel = setting.getMaxValue();
    if (!Range.closed(-settingMaxLevel, settingMaxLevel).contains(level)) {
      Text.fromMessages("commands.setting-invalid-level")
          .placeholder("%max%", String.valueOf(settingMaxLevel))
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return false;
    }

    return true;
  }

  private void playSettingToggleSound(
      Setting setting, Player player, int previousValue, int newValue) {
    boolean isToggleOn = newValue > previousValue;
    String toggleSound = getSettingToggleSound(setting, isToggleOn);
    Objects.requireNonNull(
            XSound.parse(toggleSound),
            "Invalid sound '" + toggleSound + "' for setting '" + setting.getName() + "'")
        .forPlayer(player)
        .play();
  }

  @NotNull
  private String getSettingToggleSound(Setting setting, boolean enabled) {
    ConfigurationSection overrideSection =
        PLUGIN.getSettingsConfiguration().getSettingOverridesSection(setting.getName());
    if (overrideSection == null) {
      return getDefaultToggleSound(enabled);
    }

    String soundPath = enabled ? "setting-toggle-on" : "setting-toggle-off";
    String overrideSound = overrideSection.getString("sounds." + soundPath);
    return overrideSound == null ? getDefaultToggleSound(enabled) : overrideSound;
  }

  private String getDefaultToggleSound(boolean enabled) {
    return enabled
        ? PlayerSettingsProvider.getToggleOnSound()
        : PlayerSettingsProvider.getToggleOffSound();
  }
}

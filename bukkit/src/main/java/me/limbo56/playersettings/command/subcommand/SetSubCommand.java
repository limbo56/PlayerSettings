package me.limbo56.playersettings.command.subcommand;

import com.cryptomorin.xseries.XSound;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Range;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Permissions;
import me.limbo56.playersettings.util.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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
          .usePlaceholder("%setting%", settingName)
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    // Check if the value provided is valid
    String value = args[2];
    Integer parsedValue = PLUGIN.getSettingsConfiguration().parseSettingValue(setting, value);
    if (parsedValue == null) {
      String acceptedValues = getAcceptedValues(sender, setting);
      Text.fromMessages("commands.setting-invalid-level")
          .usePlaceholder("%values%", acceptedValues)
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    // Check if user is not loading
    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (user.isLoading()) {
      Text.fromMessages("settings.wait-loading")
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
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
    Text.fromMessages("commands.setting-changed")
        .usePlaceholder("%setting%", setting.getDisplayName())
        .usePlaceholder(
            "%value%", PLUGIN.getSettingsConfiguration().formatSettingValue(setting, parsedValue))
        .usePlaceholderApi(player)
        .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    List<String> completions = new ArrayList<>();

    // Show setting names in completions
    if (args.length < 3) {
      UUID uuid = ((Player) sender).getUniqueId();
      Collection<String> settingNames = PLUGIN.getSettingsManager().getAllowedSettings(uuid);
      StringUtil.copyPartialMatches(args[1], settingNames, completions);
    }

    // Show allowed values in completions
    if (args.length > 2 && PLUGIN.getSettingsManager().isSettingRegistered(args[1])) {
      Setting setting = PLUGIN.getSettingsManager().getSetting(args[1]);
      int permissionLevel = Permissions.getSettingPermissionLevel(sender, setting);
      Collection<String> settingLevels = getAllowedValues(setting, permissionLevel);
      ImmutableListMultimap<Integer, String> valueAliases = setting.getValueAliases().inverse();
      settingLevels.addAll(getSettingLevelAliases(settingLevels, valueAliases));
      StringUtil.copyPartialMatches(args[2], settingLevels, completions);
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
          .usePlaceholder("%setting%", setting.getDisplayName())
          .usePlaceholder(
              "%value%", PLUGIN.getSettingsConfiguration().formatSettingValue(setting, level))
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return false;
    }

    // Allow change to default setting value
    if (level == setting.getDefaultValue()) {
      return true;
    }

    // Ignore players who don't have permission to change the setting
    if (!user.hasSettingPermissions(settingName)) {
      Text.fromMessages("settings.no-access")
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return false;
    }

    // Ignore values higher than the player's max permission level
    int playerMaxLevel = Permissions.getSettingPermissionLevel(player, setting);
    if (!Range.closed(-playerMaxLevel, playerMaxLevel).contains(level)) {
      Text.fromMessages("settings.low-access-level")
          .usePlaceholder("%setting%", setting.getDisplayName())
          .usePlaceholder("%max%", String.valueOf(playerMaxLevel))
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return false;
    }

    // Ignore levels out of the setting's value range
    int settingMaxLevel = setting.getMaxValue();
    if (!Range.closed(-settingMaxLevel, settingMaxLevel).contains(level)) {
      String acceptedValues = getAcceptedValues(player, setting);
      Text.fromMessages("commands.setting-invalid-level")
          .usePlaceholder("%values%", acceptedValues)
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return false;
    }

    return true;
  }

  @NotNull
  private String getAcceptedValues(@NotNull CommandSender sender, Setting setting) {
    Collection<String> settingLevels =
        getAllowedValues(setting, Permissions.getSettingPermissionLevel(sender, setting));
    List<String> settingLevelAliases =
        getSettingLevelAliases(settingLevels, setting.getValueAliases().inverse());
    return Stream.concat(settingLevelAliases.stream(), settingLevels.stream())
        .collect(Collectors.joining(", "));
  }

  @NotNull
  private List<String> getSettingLevelAliases(
      Collection<String> settingLevels, ImmutableListMultimap<Integer, String> valueAliases) {
    return settingLevels.stream()
        .map(level -> valueAliases.get(Integer.parseInt(level)).stream().findFirst())
        .filter(Optional::isPresent)
        .map(Optional::get)
        .collect(Collectors.toList());
  }

  private Collection<String> getAllowedValues(Setting setting, int value) {
    return IntStream.range(0, setting.getMaxValue() + 1)
        .filter(current -> current <= value)
        .mapToObj(String::valueOf)
        .collect(Collectors.toList());
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
        ? PLUGIN.getPluginConfiguration().getToggleOnSound()
        : PLUGIN.getPluginConfiguration().getToggleOffSound();
  }
}

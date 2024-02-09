package me.limbo56.playersettings.command.subcommand;

import com.cryptomorin.xseries.XSound;
import com.google.common.collect.ListMultimap;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.message.Messenger;
import me.limbo56.playersettings.message.text.ReplaceModifier;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.Permissions;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class SetSubCommand extends SubCommand {
  private final SettingsConfiguration settingsConfiguration;
  private final SettingsManager settingsManager;
  private final UserManager userManager;
  private final Messenger messenger;
  private final MessagesConfiguration messagesConfiguration;

  public SetSubCommand(PlayerSettings plugin) {
    super(plugin, "set", "Sets the value of a setting", "<setting> <value>", 3, null);
    this.settingsConfiguration =
        plugin.getConfigurationManager().getConfiguration(SettingsConfiguration.class);
    this.settingsManager = plugin.getSettingsManager();
    this.userManager = plugin.getUserManager();
    this.messenger = plugin.getMessenger();
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
  }

  @Override
  protected void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    // Check if the setting provided is valid
    Player player = (Player) sender;
    String settingName = args[1];
    if (!settingsManager.isRegistered(settingName)) {
      sendSettingNotFoundMessage(player, settingName);
      return;
    }

    InternalSetting setting = settingsManager.getSetting(settingName);
    SettingUser user = userManager.getUser(player.getUniqueId());
    if (isLoading(user) || !hasSettingPermissions(user, settingName)) {
      return;
    }

    String value = args[2];
    Integer newValue = parseValue(setting, value);
    if (isInvalidValue(player, setting, newValue)) {
      return;
    }

    SettingWatcher settingWatcher = user.getSettingWatcher();
    int currentValue = settingWatcher.getValue(settingName);
    if (isSettingUnchanged(player, setting, currentValue, newValue)) {
      return;
    }

    int playerMaxLevel = Permissions.getSettingPermissionLevel(player, setting);
    if (!isWithinAbsoluteRange(playerMaxLevel, currentValue)) {
      sendLowAccessLevelMessage(player, setting, playerMaxLevel);
      return;
    }
    if (!isWithinAbsoluteRange(setting.getMaxValue(), currentValue)) {
      sendInvalidLevelMessage(player, setting);
      return;
    }

    settingWatcher.setValue(settingName, newValue, false);
    playSettingToggleSound(player, setting, currentValue, newValue);
    sendSettingChangedMessage(player, setting, newValue);
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    List<String> completions = new ArrayList<>();
    if (args.length < 3) {
      UUID uuid = ((Player) sender).getUniqueId();
      Collection<String> settingNames = userManager.getUser(uuid).getAllowedSettings();
      StringUtil.copyPartialMatches(args[1], settingNames, completions);
    }
    if (args.length > 2 && settingsManager.isRegistered(args[1])) {
      Setting setting = settingsManager.getSetting(args[1]);
      Collection<String> settingLevels = getAllowedLevels(sender, setting);
      settingLevels.addAll(getSettingLevelAliases(setting, settingLevels));
      StringUtil.copyPartialMatches(args[2], settingLevels, completions);
    }
    Collections.sort(completions);
    return completions;
  }

  private Integer parseValue(Setting setting, String value) {
    try {
      int settingValue =
          setting.getValueAliases().get(value).stream()
              .findFirst()
              .orElseGet(() -> Integer.parseInt(value));
      PluginLogger.debug(
          "Parsing value '"
              + value
              + "' for setting '"
              + setting.getName()
              + "', Parsed '"
              + settingValue
              + "'");
      return settingValue;
    } catch (NumberFormatException exception) {
      PluginLogger.debug("Unknown value '" + value + "' for setting '" + setting.getName() + "'");
      return 0;
    }
  }

  private boolean isLoading(SettingUser user) {
    if (user.isLoading()) {
      messenger.sendMessage(
          user.getPlayer(), messagesConfiguration.getMessage("settings.wait-loading"));
      return true;
    }
    return false;
  }

  private boolean hasSettingPermissions(SettingUser user, String settingName) {
    if (!user.hasSettingPermissions(settingName)) {
      messenger.sendMessage(
          user.getPlayer(), messagesConfiguration.getMessage("settings.no-access"));
      return false;
    }
    return true;
  }

  private boolean isInvalidValue(Player player, InternalSetting setting, Integer newValue) {
    if (newValue == null) {
      sendInvalidLevelMessage(player, setting);
      return true;
    }
    return false;
  }

  private boolean isSettingUnchanged(
      Player player, InternalSetting setting, int currentValue, int newValue) {
    if (newValue == currentValue) {
      sendSettingUnchangedMessage(player, setting, currentValue);
      return true;
    }
    return false;
  }

  private boolean isWithinAbsoluteRange(int max, int current) {
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

  private void sendSettingUnchangedMessage(
      Player player, InternalSetting setting, int currentValue) {
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("commands.setting-unchanged"),
        ReplaceModifier.of("%setting%", setting.getDisplayName()),
        ReplaceModifier.of("%value%", setting.getValueAlias(currentValue)));
  }

  private void sendSettingNotFoundMessage(Player player, String settingName) {
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("commands.setting-not-found"),
        ReplaceModifier.of("%setting%", settingName));
  }

  private void sendInvalidLevelMessage(Player player, Setting setting) {
    Collection<String> settingLevels = getAllowedLevels(player, setting);
    List<String> settingLevelAliases = getSettingLevelAliases(setting, settingLevels);
    String acceptedValues =
        Stream.concat(settingLevelAliases.stream(), settingLevels.stream())
            .collect(Collectors.joining(", "));
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("commands.setting-invalid-level"),
        ReplaceModifier.of("%values%", acceptedValues));
  }

  private void sendSettingChangedMessage(Player player, InternalSetting setting, int value) {
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("commands.setting-changed"),
        ReplaceModifier.of("%setting%", setting.getDisplayName()),
        ReplaceModifier.of("%value%", setting.getValueAlias(value)));
  }

  @NotNull
  private List<String> getSettingLevelAliases(Setting setting, Collection<String> settingLevels) {
    ListMultimap<String, Integer> valueAliases = setting.getValueAliases();
    List<String> list = new ArrayList<>();

    for (String level : settingLevels) {
      valueAliases.get(level).stream().findFirst().map(String::valueOf).ifPresent(list::add);
    }

    return list;
  }

  private Collection<String> getAllowedLevels(CommandSender sender, Setting setting) {
    List<String> allowedValues = new ArrayList<>();
    int maxLevel = Permissions.getSettingPermissionLevel(sender, setting);

    for (int level = 0; level <= setting.getMaxValue(); level++) {
      if (level <= maxLevel) {
        allowedValues.add(String.valueOf(level));
      }
    }

    return allowedValues;
  }

  private void playSettingToggleSound(
      Player player, Setting setting, int previousValue, int newValue) {
    String toggleSound = getSettingToggleSound(setting, newValue > previousValue);
    Objects.requireNonNull(
            XSound.parse(toggleSound),
            "Invalid sound '" + toggleSound + "' for setting '" + setting.getName() + "'")
        .forPlayer(player)
        .play();
  }

  @NotNull
  private String getSettingToggleSound(Setting setting, boolean on) {
    ConfigurationSection overrides =
        settingsConfiguration.getSettingOverridesSection(setting.getName());
    if (overrides == null) {
      return getDefaultToggleSound(on);
    }

    String soundPath = on ? "setting-toggle-on" : "setting-toggle-off";
    String overrideSound = overrides.getString("sounds." + soundPath);
    return overrideSound == null ? getDefaultToggleSound(on) : overrideSound;
  }

  private String getDefaultToggleSound(boolean enabled) {
    PluginConfiguration pluginConfiguration = plugin.getConfiguration();
    return enabled
        ? pluginConfiguration.getToggleOnSound()
        : pluginConfiguration.getToggleOffSound();
  }
}

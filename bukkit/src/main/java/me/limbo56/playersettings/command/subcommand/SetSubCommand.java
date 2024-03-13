package me.limbo56.playersettings.command.subcommand;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.message.Messenger;
import me.limbo56.playersettings.message.text.ReplaceModifier;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.Colors;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class SetSubCommand extends SubCommand {
  private final SettingsManager settingsManager;
  private final UserManager userManager;
  private final Messenger messenger;
  private final MessagesConfiguration messagesConfiguration;

  public SetSubCommand(PlayerSettings plugin) {
    super(plugin, "set", "Sets the value of a setting", "<setting> <value>", 3, null);
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
    if (isLoading(user)) {
      return;
    }

    String value = args[2];
    Integer newValue = parseValue(setting, value);
    if (isInvalidValue(player, setting, newValue)) {
      return;
    }

    user.changeSetting(settingName, newValue);
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
      InternalSetting setting = settingsManager.getSetting(args[1]);
      Collection<Integer> settingValues = setting.getAllowedValues(sender);
      Stream<String> aliases =
          setting.getAliases(settingValues).stream()
              .map(s -> ChatColor.stripColor(Colors.translateColorCodes(s)));
      Collection<String> availableValues =
          Stream.concat(aliases, settingValues.stream().map(String::valueOf))
              .collect(Collectors.toSet());
      StringUtil.copyPartialMatches(args[2], availableValues, completions);
    }
    Collections.sort(completions);
    return completions;
  }

  private boolean isLoading(SettingUser user) {
    if (user.isLoading()) {
      messenger.sendMessage(
          user.getPlayer(), messagesConfiguration.getMessage("settings.wait-loading"));
      return true;
    }
    return false;
  }

  private void sendSettingNotFoundMessage(Player player, String settingName) {
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("commands.setting-not-found"),
        ReplaceModifier.of("%setting%", settingName));
  }

  private Integer parseValue(InternalSetting setting, String value) {
    try {
      Optional<Integer> optionalValue = Optional.empty();
      for (Map.Entry<String, Collection<Integer>> entry :
          setting.getValueAliases().asMap().entrySet()) {
        String alias = ChatColor.stripColor(Colors.translateColorCodes(entry.getKey()));
        if (alias.equals(value)) {
          optionalValue = entry.getValue().stream().findFirst();
        }
      }

      int settingValue = optionalValue.orElseGet(() -> Integer.parseInt(value));
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
      return null;
    }
  }

  @Contract("_, _, null -> true")
  private boolean isInvalidValue(Player player, InternalSetting setting, Integer newValue) {
    if (newValue == null) {
      messenger.sendMessage(
          player, messenger.getMessageProvider().getInvalidSettingLevelMessage(player, setting));
      return true;
    }
    return false;
  }
}

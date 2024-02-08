package me.limbo56.playersettings.command.subcommand;

import com.google.common.collect.Sets;
import java.util.*;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Messenger;
import me.limbo56.playersettings.util.text.ReplaceModifier;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class GetSubCommand extends SubCommand {
  private final MessagesConfiguration messagesConfiguration;
  private final Messenger messenger;

  public GetSubCommand(PlayerSettings plugin) {
    super(plugin, "get", "Gets the value of a setting", "<setting>", 2, null);
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
    messenger = plugin.getMessenger();
  }

  @Override
  protected void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    SettingUser user = plugin.getUserManager().getUser(player.getUniqueId());
    if (user == null) {
      messenger.sendMessage(player, messagesConfiguration.getMessage("settings.no-settings-data"));
      return;
    }

    // Check if user is not loading
    if (user.isLoading()) {
      messenger.sendMessage(player, messagesConfiguration.getMessage("settings.wait-loading"));
      return;
    }

    String settingName = args[1];
    InternalSetting setting = plugin.getSettingsManager().getSetting(settingName);
    if (setting == null) {
      messenger.sendMessage(
          player,
          messagesConfiguration.getMessage("commands.setting-not-found"),
          ReplaceModifier.of("%setting%", settingName));
      return;
    }

    int value = user.getSettingWatcher().getValue(settingName);
    messenger.sendMessage(
        player,
        messagesConfiguration.getMessage("commands.setting-show"),
        ReplaceModifier.of("%setting%", settingName),
        ReplaceModifier.of("%value%", setting.getValueAlias(value)));
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    final List<String> completions = new ArrayList<>();
    Set<String> settingNames = getWatchedSettings((Player) sender);
    StringUtil.copyPartialMatches(args[1], settingNames, completions);
    Collections.sort(completions);
    return completions;
  }

  @NotNull
  private Set<String> getWatchedSettings(Player sender) {
    return Sets.newHashSet(
        plugin.getUserManager().getSettingWatcher(sender.getUniqueId()).getWatched());
  }
}

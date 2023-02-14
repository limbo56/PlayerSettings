package me.limbo56.playersettings.command.subcommand;

import com.google.common.collect.ImmutableSet;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class GetSubCommand extends SubCommand {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public GetSubCommand() {
    super("get", "Gets the value of a setting", "<setting>", 2, null);
  }

  @Override
  protected void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    SettingUser user = PLUGIN.getUserManager().getUser(player.getUniqueId());
    if (user == null) {
      Text.fromMessages("settings.no-settings-data")
          .usePlaceholderApi(player)
          .sendMessage(sender, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    // Check if user is not loading
    if (user.isLoading()) {
      Text.fromMessages("settings.wait-loading")
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    String settingName = args[1];
    Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
    if (setting == null) {
      Text.fromMessages("commands.setting-not-found")
          .usePlaceholder("%setting%", settingName)
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    int value = user.getSettingWatcher().getValue(settingName);
    Text.fromMessages("commands.setting-show")
        .usePlaceholder("%setting%", setting.getDisplayName())
        .usePlaceholder("%value%", PLUGIN.getSettingsConfiguration().formatSettingValue(setting, value))
        .usePlaceholderApi(player)
        .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    SettingUser user = PLUGIN.getUserManager().getUser(((Player) sender).getUniqueId());
    final List<String> completions = new ArrayList<>();
    Set<String> settingNames = ImmutableSet.copyOf(user.getSettingWatcher().getWatched());
    StringUtil.copyPartialMatches(args[1], settingNames, completions);
    Collections.sort(completions);
    return completions;
  }
}

package me.limbo56.playersettings.command.subcommand;

import static me.limbo56.playersettings.settings.SettingValue.SETTING_VALUE;

import com.google.common.collect.ImmutableSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class GetSubCommand extends SubCommand {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  public GetSubCommand() {
    super("get", "Gets the value of a setting", "<setting>", 2, null);
  }

  @Override
  protected void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    SettingUser user = plugin.getUserManager().getUser(player.getUniqueId());
    if (user == null) {
      Text.fromMessages("settings.no-settings-data")
          .sendMessage(sender, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    // Check if user is not loading
    if (user.isLoading()) {
      Text.fromMessages("settings.wait-loading")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    String settingName = args[1];
    Setting setting = plugin.getSettingsContainer().getSetting(settingName);
    if (setting == null) {
      Text.fromMessages("commands.setting-not-found")
          .placeholder("%setting%", settingName)
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    String placeholderName =
        ChatColor.stripColor(setting.getItem().getItemStack().getItemMeta().getDisplayName());
    int value = user.getSettingWatcher().getValue(settingName);
    String placeholderValue = SETTING_VALUE.format(value);
    Text.fromMessages("commands.setting-show")
        .placeholder("%setting%", placeholderName)
        .placeholder("%value%", placeholderValue)
        .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    SettingUser user = plugin.getUserManager().getUser(((Player) sender).getUniqueId());
    final List<String> completions = new ArrayList<>();
    Set<String> settingNames = ImmutableSet.copyOf(user.getSettingWatcher().getWatched());
    StringUtil.copyPartialMatches(args[1], settingNames, completions);
    Collections.sort(completions);
    return completions;
  }
}

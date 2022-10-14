package me.limbo56.playersettings.command.subcommand;

import static me.limbo56.playersettings.settings.SettingValue.SETTING_VALUE;

import com.cryptomorin.xseries.XSound;
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
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

public class SetSubCommand extends SubCommand {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  public SetSubCommand() {
    super("set", "Sets the value of a setting", "<setting> <value>", 3, null);
  }

  @Override
  protected void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    // Check if the setting provided is valid
    String settingName = args[1];
    Player player = (Player) sender;
    Setting setting = plugin.getSettingsContainer().getSetting(settingName);
    if (setting == null) {
      Text.fromMessages("commands.setting-not-found")
          .placeholder("%setting%", settingName)
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    // Check if the value provided is valid
    String value = args[2];
    Integer parsedValue = SETTING_VALUE.parse(value);
    if (parsedValue == null) {
      Text.fromMessages("commands.setting-invalid-value")
          .placeholder("%max%", String.valueOf(setting.getMaxValue()))
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    // Check if user is not loading
    SettingUser user = plugin.getUserManager().getUser(player.getUniqueId());
    if (user.isLoading()) {
      Text.fromMessages("settings.wait-loading")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    // Check if user can change the setting
    SettingWatcher watcher = user.getSettingWatcher();
    if (!user.canChangeSettingTo(setting, parsedValue)) {
      return;
    }

    // Change setting value and play sound
    int previousValue = watcher.getValue(settingName);
    watcher.setValue(settingName, parsedValue, false);
    playChangeSound(player, previousValue, parsedValue);

    // Send setting change message
    String placeholderName =
        ChatColor.stripColor(setting.getItem().getItemStack().getItemMeta().getDisplayName());
    String placeholderValue = SETTING_VALUE.format(parsedValue);
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
    if (args.length > 2 && plugin.getSettingsContainer().isSettingLoaded(args[1])) {
      Setting setting = plugin.getSettingsContainer().getSetting(args[1]);
      int permissionLevel = PlayerSettingsProvider.getSettingPermissionLevel(sender, setting);
      Set<String> settingValues = PlayerSettingsProvider.getAllowedLevels(setting, permissionLevel);
      StringUtil.copyPartialMatches(args[2], settingValues, completions);
    }

    Collections.sort(completions);
    return completions;
  }

  private void playChangeSound(Player player, int previousValue, int newValue) {
    if (newValue < previousValue) {
      Objects.requireNonNull(
              XSound.parse(PlayerSettingsProvider.getToggleOffSound()),
              "Could not parse 'setting-toggle-off' sound from 'config.yml'")
          .forPlayer(player)
          .play();
      return;
    }
    Objects.requireNonNull(
            XSound.parse(PlayerSettingsProvider.getToggleOnSound()),
            "Could not parse 'setting-toggle-on' sound from 'config.yml'")
        .forPlayer(player)
        .play();
  }
}

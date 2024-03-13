package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.message.Messenger;
import me.limbo56.playersettings.message.text.ReplaceModifier;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenSubCommand extends SubCommand {
  private final MessagesConfiguration messagesConfiguration;
  private final Messenger messenger;

  public OpenSubCommand(PlayerSettings plugin) {
    super(plugin, "open", "Opens the main menu", "", 1, null);
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
    this.messenger = plugin.getMessenger();
  }

  @Override
  public void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    if (!isValidWorld(player) || isLoading(player)) {
      return;
    }
    openSettingsMenu(player);
  }

  private void openSettingsMenu(Player player) {
    plugin.getSettingsMenuManager().open(player, 1);
  }

  private boolean isValidWorld(Player player) {
    String worldName = player.getWorld().getName();
    if (plugin.getConfiguration().isAllowedWorld(worldName)) {
      return true;
    }
    if (player.isOp()) {
      sendInvalidWorldMessage(player);
    }
    return false;
  }

  private boolean isLoading(Player player) {
    SettingUser user = plugin.getUserManager().getUser(player.getUniqueId());
    if (user == null || user.isLoading()) {
      sendWaitLoadingMessage(player);
      return true;
    }
    return false;
  }

  private void sendInvalidWorldMessage(Player player) {
    messenger.sendMessage(
        player,
        "The settings are not enabled for the current world '%worldName%'. Please add it to the 'general.worlds' section of the 'config.yml' file to enable the settings.",
        ReplaceModifier.of("%worldName%", player.getWorld().getName()));
  }

  private void sendWaitLoadingMessage(Player player) {
    messenger.sendMessage(player, messagesConfiguration.getMessage("settings.wait-loading"));
  }
}

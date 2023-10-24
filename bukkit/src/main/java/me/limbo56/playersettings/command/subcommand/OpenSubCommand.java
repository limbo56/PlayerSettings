package me.limbo56.playersettings.command.subcommand;

import java.util.ArrayList;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenSubCommand extends SubCommand {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public OpenSubCommand() {
    super("open", "Opens the main menu", "", 1, null);
  }

  @Override
  public void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    String worldName = player.getWorld().getName();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(worldName)) {
      if (player.isOp()) {
        Text.from(
                "In order to enable the menu on this world, you need to add the '"
                    + worldName
                    + "' world to the 'general.worlds' section of the 'config.yml' file.")
            .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      }
      return;
    }

    SettingUser user =
        PlayerSettingsProvider.getPlugin().getUserManager().getUser(player.getUniqueId());
    if (user.isLoading()) {
      Text.fromMessages("settings.wait-loading")
          .usePlaceholderApi(player)
          .sendMessage(player, PLUGIN.getMessagesConfiguration().getMessagePrefix());
      return;
    }

    SettingsMenu.open(user.getPlayer(), 1);
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    return new ArrayList<>();
  }
}

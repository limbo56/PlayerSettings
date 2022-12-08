package me.limbo56.playersettings.command.subcommand;

import java.util.ArrayList;
import java.util.List;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.command.SubCommand;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Text;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class OpenSubCommand extends SubCommand {
  public OpenSubCommand() {
    super("open", "Opens the main menu", "", 1, null);
  }

  @Override
  public void execute(@NotNull CommandSender sender, @NotNull String @NotNull [] args) {
    Player player = (Player) sender;
    String worldName = player.getWorld().getName();
    if (!PlayerSettingsProvider.isAllowedWorld(worldName)) {
      if (player.isOp()) {
        Text.from(
                "&cTo enable the menu on this world, "
                    + "add the '"
                    + worldName
                    + "' world to the 'general.worlds' "
                    + "section in the 'config.yml' file.")
            .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      }
      return;
    }

    SettingUser user =
        PlayerSettingsProvider.getPlugin().getUserManager().getUser(player.getUniqueId());
    if (user.isLoading()) {
      Text.fromMessages("settings.wait-loading")
          .sendMessage(player, PlayerSettingsProvider.getMessagePrefix());
      return;
    }

    PlayerSettingsProvider.getPlugin().getSettingsMenuManager().openMenu(user, 1);
  }

  @Override
  public List<String> onTabComplete(CommandSender sender, String[] args) {
    return new ArrayList<>();
  }
}

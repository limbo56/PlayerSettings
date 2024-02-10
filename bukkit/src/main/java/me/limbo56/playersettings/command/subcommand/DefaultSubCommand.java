package me.limbo56.playersettings.command.subcommand;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class DefaultSubCommand extends SubCommand {
  public DefaultSubCommand(PlayerSettings plugin) {
    super(plugin, "default", "", "", 1, null);
  }

  @Override
  protected void execute(@NotNull CommandSender sender, @NotNull String[] args) {
    plugin.getSettingsMenuManager().open((Player) sender, 1);
  }
}

package me.limbo56.playersettings.menu.action;

import java.util.List;
import me.clip.placeholderapi.PlaceholderAPI;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.jetbrains.annotations.NotNull;

public class ExecuteCommandsAction implements MenuItemAction {
  private static final String CLOSE_COMMAND = "playersettings close";
  private final List<String> commands;

  public ExecuteCommandsAction(List<String> commands) {
    this.commands = commands;
  }

  @Override
  public void execute(SettingUser user, SettingsMenuItem menuItem, ClickType clickType) {
    Player player = user.getPlayer();
    for (String command : commands) {
      if (command.equalsIgnoreCase(CLOSE_COMMAND)) {
        player.closeInventory();
        return;
      }

      player.performCommand(formatCommand(player, command));
    }
  }

  @NotNull
  private static String formatCommand(Player player, String command) {
    String formattedCommand = command.replace("%player%", player.getName());
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      formattedCommand = PlaceholderAPI.setPlaceholders(player, formattedCommand);
    }
    return formattedCommand;
  }
}

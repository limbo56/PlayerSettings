package me.limbo56.playersettings.menu.actions;

import me.clip.placeholderapi.PlaceholderAPI;
import me.limbo56.playersettings.menu.SettingsMenuItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.List;

public class DismissAction implements MenuItemAction {
  private static final String CLOSE_COMMAND = "playersettings close";
  private final List<String> commands;

  public DismissAction(List<String> commands) {
    this.commands = commands;
  }

  @Override
  public void execute(SettingsMenuItem menuItem, ClickType clickType, SettingUser user) {
    Player player = user.getPlayer();
    for (String command : commands) {
      if (command.equalsIgnoreCase(CLOSE_COMMAND)) {
        player.closeInventory();
        return;
      }

      String formattedCommand = command.replace("%player%", player.getName());
      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
        formattedCommand = PlaceholderAPI.setPlaceholders(player, formattedCommand);
      }

      player.performCommand(formattedCommand);
    }
  }
}

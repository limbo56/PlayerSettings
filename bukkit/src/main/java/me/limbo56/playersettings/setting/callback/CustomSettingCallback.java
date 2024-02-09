package me.limbo56.playersettings.setting.callback;

import java.util.List;
import java.util.Map;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.message.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.message.text.ReplaceModifier;
import me.limbo56.playersettings.message.text.Text;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CustomSettingCallback implements SettingCallback {
  private final CommandExecutor commandExecutor;

  public CustomSettingCallback(Map<String, List<String>> commandsMap) {
    this.commandExecutor = new CommandExecutor(commandsMap);
  }

  @Override
  public void notifyChange(Player player, Setting setting, int value) {
    String section = commandExecutor.getCommandsSection(value);
    if (section == null) {
      PluginLogger.warning(
          "No callback found for value '" + value + "' of setting '" + setting.getName() + "'");
      return;
    }
    commandExecutor.execute(player, section);
  }

  private static final class CommandExecutor {
    private static final String CONSOLE_PREFIX = "SUDO ";
    private static final String DEFAULT_SECTION = "default";

    private final Map<String, List<String>> commandsMap;

    private CommandExecutor(Map<String, List<String>> commandsMap) {
      this.commandsMap = commandsMap;
    }

    private void execute(Player player, String action) {
      for (String command : getActionCommands(player, action)) {
        if (!command.startsWith(CONSOLE_PREFIX)) {
          player.performCommand(command);
          return;
        }

        String formattedCommand = command.replace(CONSOLE_PREFIX, "");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
      }
    }

    @Nullable
    private String getCommandsSection(int value) {
      String stringValue = String.valueOf(Math.max(value, 0));
      if (!commandsMap.containsKey(stringValue)) {
        return commandsMap.containsKey(DEFAULT_SECTION) ? DEFAULT_SECTION : null;
      }
      return stringValue;
    }

    private List<String> getActionCommands(Player player, String action) {
      return Text.create(
              commandsMap.get(action),
              new PlaceholderAPIModifier(player),
              ReplaceModifier.of("%player%", player.getName()),
              ReplaceModifier.of("%value%", action))
          .getTextLines();
    }
  }
}

package me.limbo56.playersettings.settings;

import java.util.List;
import java.util.Map;
import me.clip.placeholderapi.PlaceholderAPI;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingCallback;
import me.limbo56.playersettings.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

public class CustomSettingCallback implements SettingCallback {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private static final String CONSOLE_PREFIX = "SUDO ";
  private static final String DEFAULT_SECTION = "default";
  private final Map<String, List<String>> commandsMap;

  public CustomSettingCallback(Map<String, List<String>> commandsMap) {
    this.commandsMap = commandsMap;
  }

  @Override
  public void notifyChange(Setting setting, Player player, int value) {
    String section = getSettingCommandSection(setting, value);
    if (section == null) {
      return;
    }

    List<String> commands = formatCommands(commandsMap.get(section), player, value);
    dispatchCommands(player, commands);
  }

  @Nullable
  private String getSettingCommandSection(Setting setting, int value) {
    String stringValue = String.valueOf(Math.max(value, 0));
    if (!commandsMap.containsKey(stringValue) && commandsMap.containsKey(DEFAULT_SECTION)) {
      return DEFAULT_SECTION;
    }

    if (!commandsMap.containsKey(stringValue)) {
      PLUGIN
          .getLogger()
          .warning("No action '" + stringValue + "' found for setting '" + setting.getName() + "'");
      return null;
    }

    return stringValue;
  }

  private List<String> formatCommands(List<String> commands, Player player, int value) {
    return Text.from(commands)
        .placeholder("%player%", player.getName())
        .placeholder("%value%", String.valueOf(value))
        .build();
  }

  private void dispatchCommands(Player player, List<String> commands) {
    for (String command : commands) {
      if (!command.startsWith(CONSOLE_PREFIX)) {
        player.performCommand(command);
        return;
      }

      String formattedCommand = command.replace(CONSOLE_PREFIX, "");
      if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
        formattedCommand = PlaceholderAPI.setPlaceholders(player, formattedCommand);
      }

      Bukkit.dispatchCommand(Bukkit.getConsoleSender(), formattedCommand);
    }
  }
}

package me.limbo56.playersettings.settings;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingCallback;
import me.limbo56.playersettings.util.Text;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CustomSettingCallback implements SettingCallback {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final CommandExecutor commandExecutor;

  public CustomSettingCallback(Map<String, List<String>> commandsMap) {
    this.commandExecutor = new CommandExecutor(commandsMap);
  }

  @Nullable
  public static SettingCallback deserialize(ConfigurationSection section) {
    Map<String, List<String>> commandMap = new HashMap<>();
    if (!section.contains("values")
        && !section.contains("onEnable")
        && !section.contains("onDisable")) {
      return null;
    }

    ConfigurationSection valuesSection = section.getConfigurationSection("values");
    if (valuesSection != null) {
      commandMap =
          valuesSection.getKeys(false).stream()
              .collect(Collectors.toMap(value -> value, valuesSection::getStringList));
    } else if (section.contains("onEnable") || section.contains("onDisable")) {
      commandMap.put("1", section.getStringList("onEnable"));
      commandMap.put("0", section.getStringList("onDisable"));
    }

    return new CustomSettingCallback(commandMap);
  }

  @Override
  public void notifyChange(Setting setting, Player player, int value) {
    String section = commandExecutor.getCommandsSection(value);
    if (section == null) {
      PLUGIN
          .getLogger()
          .warning(
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
      return Text.from(commandsMap.get(action))
          .usePlaceholder("%player%", player.getName())
          .usePlaceholder("%value%", action)
          .usePlaceholderApi(player)
          .build();
    }
  }
}

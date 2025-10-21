package me.limbo56.playersettings.configuration;

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.util.Configurations;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;

public class MessagesConfiguration extends BaseConfiguration {
  private String currentLocale;

  public MessagesConfiguration(PlayerSettings plugin) {
    super(plugin);
  }

  @Override
  @NotNull
  String getFileName() {
    // Get the locale from plugin configuration
    PluginConfiguration pluginConfig = plugin.getConfiguration();
    if (pluginConfig != null && pluginConfig.isLoaded()) {
      currentLocale = pluginConfig.getLocale();
    } else {
      currentLocale = "en_US";
    }

    // Return the locale-specific messages file
    return "locales/" + currentLocale + ".yml";
  }

  @Override
  public void load() {
    if (!isLoaded()) {
      // Create plugin data folder
      if (!plugin.getDataFolder().exists()) {
        plugin.getDataFolder().mkdirs();
      }

      // Create locales folder
      File localesFolder = new File(plugin.getDataFolder(), "locales");
      if (!localesFolder.exists()) {
        localesFolder.mkdirs();
      }

      // Get the locale from plugin configuration
      PluginConfiguration pluginConfig = plugin.getConfiguration();
      if (pluginConfig != null && pluginConfig.isLoaded()) {
        currentLocale = pluginConfig.getLocale();
      } else {
        currentLocale = "en_US";
      }

      // Load locale-specific configuration file
      String localeFileName = "locales/" + currentLocale + ".yml";
      File localeFile = new File(plugin.getDataFolder(), localeFileName);

      // If locale file doesn't exist, try to save it from resources
      if (!localeFile.exists()) {
        InputStream localeResource = plugin.getResource(localeFileName);
        if (localeResource != null) {
          plugin.saveResource(localeFileName, false);
        } else {
          // Fall back to en_US if requested locale doesn't exist
          plugin.getLogger().warning("Locale file '" + localeFileName + "' not found, falling back to en_US");
          currentLocale = "en_US";
          localeFileName = "locales/en_US.yml";
          localeFile = new File(plugin.getDataFolder(), localeFileName);
          if (!localeFile.exists()) {
            InputStream defaultResource = plugin.getResource(localeFileName);
            if (defaultResource != null) {
              plugin.saveResource(localeFileName, false);
            }
          }
        }
      }

      configuration = YamlConfiguration.loadConfiguration(localeFile);

      // Also load legacy messages.yml for backwards compatibility if it exists
      File legacyMessagesFile = Configurations.getPluginFile("messages.yml");
      if (legacyMessagesFile.exists()) {
        YamlConfiguration legacyConfig = YamlConfiguration.loadConfiguration(legacyMessagesFile);
        // Use legacy file as defaults for missing keys
        configuration.setDefaults(legacyConfig);
      }
    }
  }

  public String getMessage(String path) {
    return getMessage(path, "");
  }

  public List<String> getMessageList(String path) {
    return getMessageList(path, Collections.emptyList());
  }

  public String getMessage(String path, String defaultMessage) {
    return configuration.getString(path, defaultMessage);
  }

  public List<String> getMessageList(String path, List<String> defaultMessage) {
    if (hasMessage(path)) {
      return configuration.getStringList(path);
    } else {
      return defaultMessage;
    }
  }

  public boolean hasMessage(String path) {
    return configuration.contains(path);
  }

  public String getCurrentLocale() {
    return currentLocale;
  }
}

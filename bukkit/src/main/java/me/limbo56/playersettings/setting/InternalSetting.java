package me.limbo56.playersettings.setting;

import com.google.common.collect.ImmutableListMultimap;
import java.util.*;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.ImmutableSetting;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.ConfigurationManager;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.listener.ListenerManager;
import me.limbo56.playersettings.util.Permissions;
import me.limbo56.playersettings.util.PluginLogger;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.event.Listener;

public class InternalSetting extends ForwardingSetting {
  private final boolean custom;
  private final ListenerManager listenerManager;
  private final PluginConfiguration pluginConfiguration;
  private final SettingsConfiguration settingsConfiguration;
  private final ItemsConfiguration itemsConfiguration;

  private InternalSetting(Setting setting, boolean custom) {
    super(setting);
    this.custom = custom;

    PlayerSettings plugin = PlayerSettings.getInstance();
    ConfigurationManager configurationManager = plugin.getConfigurationManager();
    this.listenerManager = plugin.getListenerManager();
    this.pluginConfiguration = configurationManager.getConfiguration(PluginConfiguration.class);
    this.settingsConfiguration = configurationManager.getConfiguration(SettingsConfiguration.class);
    this.itemsConfiguration = configurationManager.getConfiguration(ItemsConfiguration.class);

    configure();
    if (custom) {
      mergeWithConfiguration();
    }
  }

  private void configure() {
    String settingName = setting.getName();
    if (!settingsConfiguration.isSettingConfigured(settingName)) {
      settingsConfiguration.writeSetting(setting);
    }
    if (!itemsConfiguration.isItemConfigured(settingName)) {
      itemsConfiguration.writeMenuItem(settingName, setting.getItem());
    }
  }

  private void mergeWithConfiguration() {
    ConfigurationSection configuration =
        settingsConfiguration.getFile().getConfigurationSection(setting.getName());
    if (configuration == null) {
      PluginLogger.warning("Missing configuration for setting `" + setting.getName() + "`");
      return;
    }

    this.setting =
        ImmutableSetting.copyOf(Parsers.SETTING_PARSER.parse(configuration))
            .withCallbacks(setting.getCallbacks())
            .withListeners(setting.getListeners());
  }

  public static InternalSetting from(Setting setting, boolean custom) {
    return new InternalSetting(setting, custom);
  }

  public static InternalSetting from(Setting setting) {
    return from(setting, false);
  }

  public static InternalSetting fromInternal(InternalSetting internalSetting) {
    return from(internalSetting.setting, internalSetting.custom);
  }

  public void register() {
    for (Listener listener : setting.getListeners()) {
      listenerManager.registerListener(listener);
    }
  }

  public void unregister() {
    for (Listener listener : setting.getListeners()) {
      listenerManager.unregisterListener(listener);
    }
  }

  public boolean isCustom() {
    return custom;
  }

  @Override
  public ImmutableListMultimap<String, Integer> getValueAliases() {
    return Optional.of(super.getValueAliases())
        .filter(map -> !map.isEmpty())
        .orElse(pluginConfiguration.getDefaultValueAliases());
  }

  public Collection<Integer> getAllowedValues(CommandSender sender) {
    List<Integer> allowedValues = new ArrayList<>();
    int maxLevel = Permissions.getSettingPermissionLevel(sender, setting);
    for (int level = 0; level <= setting.getMaxValue(); level++) {
      if (level <= maxLevel) {
        allowedValues.add(level);
      }
    }
    return Collections.unmodifiableCollection(allowedValues);
  }

  public Collection<String> getAliases(Collection<Integer> values) {
    List<String> aliases = new ArrayList<>();
    for (int level : values) {
      aliases.add(getValueAlias(level));
    }
    return Collections.unmodifiableCollection(aliases);
  }

  public String getValueAlias(int level) {
    ImmutableListMultimap<Integer, String> aliases = getValueAliases().inverse();
    int levelRange = level < 1 ? 0 : level;
    String levelString = String.valueOf(levelRange);
    if (aliases.containsKey(levelRange)) {
      return aliases.get(levelRange).stream().findFirst().orElse(levelString);
    }
    return levelString;
  }
}

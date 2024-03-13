package me.limbo56.playersettings.menu.renderer;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.menu.item.LevelSettingItem;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.menu.item.ToggleSettingItem;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.util.PluginLogger;
import org.jetbrains.annotations.NotNull;

public class SettingsRenderer implements MenuItemRenderer {
  private final PluginConfiguration pluginConfiguration;
  private final SettingsManager settingsManager;

  public SettingsRenderer() {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.settingsManager = plugin.getSettingsManager();
    this.pluginConfiguration = plugin.getConfiguration();
  }

  @Override
  public void render(@NotNull SettingsMenu settingsMenu, int page) {
    for (InternalSetting setting : settingsManager.getSettings()) {
      if (setting.isEnabled() && setting.getItem().getPage() == page) {
        renderSetting(settingsMenu, setting);
      }
    }
  }

  public void renderSetting(@NotNull SettingsMenu settingsMenu, @NotNull InternalSetting setting) {
    if (!isSettingWithingMenuBounds(settingsMenu, setting)) {
      PluginLogger.warning(
          "Setting '"
              + setting.getName()
              + "' is not between the bounds of the menu (0-"
              + settingsMenu.getSize()
              + ")");
      return;
    }

    SettingsMenuItem.Context context = new SettingsMenuItem.Context(settingsMenu, this);
    settingsMenu.renderItem(new LevelSettingItem(context, setting));
    if (pluginConfiguration.isToggleButtonEnabled()) {
      settingsMenu.renderItem(new ToggleSettingItem(context, setting));
    }
  }

  private boolean isSettingWithingMenuBounds(SettingsMenu settingsMenu, Setting setting) {
    int menuSize = settingsMenu.getSize();
    int toggleButtonOffset = pluginConfiguration.isToggleButtonEnabled() ? 9 : 0;
    int settingSlot = setting.getItem().getSlot();
    return settingSlot + toggleButtonOffset < menuSize;
  }
}

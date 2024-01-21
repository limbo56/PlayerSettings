package me.limbo56.playersettings.menu.renderers;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.item.SettingLevelItem;
import me.limbo56.playersettings.menu.item.SettingToggleItem;
import me.limbo56.playersettings.util.PluginLogger;
import org.jetbrains.annotations.NotNull;

public class SettingsRenderer implements MenuItemRenderer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @Override
  public void render(@NotNull MenuHolder menuHolder, int page) {
    for (Setting setting : PLUGIN.getSettingsManager().getSettings()) {
      if (setting.getItem().getPage() == page) {
        renderSetting(menuHolder, setting);
      }
    }
  }

  public void renderSetting(@NotNull MenuHolder menuHolder, @NotNull Setting setting) {
    String settingName = setting.getName();
    if (!isSettingWithingMenuBounds(menuHolder, setting)) {
      PluginLogger.warning(
          "Setting '"
              + settingName
              + "' is not between the bounds of the menu (0-"
              + menuHolder.getSize()
              + ")");
      return;
    }

    menuHolder.renderItem(new SettingLevelItem(this, setting, menuHolder));
    if (PLUGIN.getPluginConfiguration().isToggleButtonEnabled()) {
      menuHolder.renderItem(new SettingToggleItem(this, setting, menuHolder));
    }
  }

  private boolean isSettingWithingMenuBounds(MenuHolder menuHolder, Setting setting) {
    int menuSize = menuHolder.getSize();
    int toggleButtonOffset = PLUGIN.getPluginConfiguration().isToggleButtonEnabled() ? 9 : 0;
    int settingSlot = setting.getItem().getSlot();

    return settingSlot + toggleButtonOffset < menuSize;
  }
}

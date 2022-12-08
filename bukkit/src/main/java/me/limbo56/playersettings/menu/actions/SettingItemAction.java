package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.SettingsMenuHolder;
import me.limbo56.playersettings.menu.SettingsMenuItem;
import me.limbo56.playersettings.menu.renderers.MenuSettingsRenderer;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public class SettingItemAction implements MenuItemAction {
  protected final MenuSettingsRenderer renderer;
  protected final SettingsMenuHolder inventory;
  protected final Setting setting;

  protected SettingItemAction(
      MenuSettingsRenderer renderer, SettingsMenuHolder inventory, Setting setting) {
    this.renderer = renderer;
    this.inventory = inventory;
    this.setting = setting;
  }

  @Override
  public void execute(SettingsMenuItem menuItem, ClickType clickType, SettingUser user) {
    renderer.renderSetting(inventory, setting);
  }
}

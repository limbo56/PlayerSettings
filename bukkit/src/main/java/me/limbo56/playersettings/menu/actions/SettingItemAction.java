package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.menu.renderers.SettingsRenderer;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public class SettingItemAction implements MenuItemAction {
  protected final SettingsRenderer renderer;
  protected final MenuHolder inventory;
  protected final Setting setting;

  protected SettingItemAction(SettingsRenderer renderer, MenuHolder inventory, Setting setting) {
    this.renderer = renderer;
    this.inventory = inventory;
    this.setting = setting;
  }

  @Override
  public void execute(SettingsMenuItem menuItem, ClickType clickType, SettingUser user) {
    renderer.renderSetting(inventory, setting);
  }
}

package me.limbo56.playersettings.menu.action;

import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public abstract class SettingItemAction implements MenuItemAction {
  protected final SettingsMenuItem.Context context;
  protected final InternalSetting setting;

  protected SettingItemAction(SettingsMenuItem.Context context, InternalSetting setting) {
    this.context = context;
    this.setting = setting;
  }

  abstract void onExecute(SettingUser user, ClickType clickType);

  @Override
  public void execute(SettingUser user, SettingsMenuItem menuItem, ClickType clickType) {
    onExecute(user, clickType);
    context.getRenderer().renderSetting(context.getMenu(), setting);
  }
}

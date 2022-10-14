package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.SettingsInventory;
import me.limbo56.playersettings.menu.renderers.SettingRenderer;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public class SettingItemAction implements InventoryItemAction {
  protected final SettingRenderer renderer;
  protected final SettingsInventory inventory;
  protected final Setting setting;

  protected SettingItemAction(
      SettingRenderer renderer, SettingsInventory inventory, Setting setting) {
    this.renderer = renderer;
    this.inventory = inventory;
    this.setting = setting;
  }

  @Override
  public void execute(ClickType clickType, SettingUser user) {
    renderer.renderSetting(inventory, setting);
  }
}

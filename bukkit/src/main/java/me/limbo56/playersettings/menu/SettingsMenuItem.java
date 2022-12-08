package me.limbo56.playersettings.menu;

import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.actions.MenuItemAction;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

public class SettingsMenuItem implements MenuItem {
  private final MenuItem menuItem;
  private final MenuItemAction clickAction;

  public SettingsMenuItem(MenuItem menuItem, MenuItemAction clickAction) {
    this.menuItem = menuItem;
    this.clickAction = clickAction;
  }

  public void executeClickAction(ClickType type, SettingUser user) {
    clickAction.execute(this, type, user);
  }

  @Override
  public ItemStack getItemStack() {
    return menuItem.getItemStack();
  }

  @Override
  public int getPage() {
    return menuItem.getPage();
  }

  @Override
  public int getSlot() {
    return menuItem.getSlot();
  }
}

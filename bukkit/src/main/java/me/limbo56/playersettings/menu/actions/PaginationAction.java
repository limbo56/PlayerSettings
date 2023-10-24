package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.menu.renderers.PaginationRenderer.PaginationItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public class PaginationAction implements MenuItemAction {
  private final PaginationItem paginationItem;

  public PaginationAction(PaginationItem paginationItem) {
    this.paginationItem = paginationItem;
  }

  @Override
  public void execute(SettingsMenuItem menuItem, ClickType clickType, SettingUser user) {
    SettingsMenu.open(user.getPlayer(), paginationItem.calculatePage(menuItem.getPage()));
  }
}

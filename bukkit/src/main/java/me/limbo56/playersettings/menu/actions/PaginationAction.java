package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.menu.SettingsMenuItem;
import me.limbo56.playersettings.menu.SettingsMenuManager;
import me.limbo56.playersettings.menu.renderers.MenuPaginationRenderer.PaginationItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public class PaginationAction implements MenuItemAction {
  private static final SettingsMenuManager SETTINGS_MENU_MANAGER =
      PlayerSettingsProvider.getPlugin().getSettingsMenuManager();
  private final PaginationItem direction;

  public PaginationAction(PaginationItem direction) {
    this.direction = direction;
  }

  @Override
  public void execute(SettingsMenuItem menuItem, ClickType clickType, SettingUser user) {
    int nextPage = direction.calculatePage(menuItem.getPage());
    SETTINGS_MENU_MANAGER.openMenu(user, nextPage);
  }
}

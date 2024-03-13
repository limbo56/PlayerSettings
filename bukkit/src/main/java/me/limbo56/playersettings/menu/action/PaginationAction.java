package me.limbo56.playersettings.menu.action;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.menu.SettingsMenuManager;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.menu.renderer.PaginationRenderer.PaginationItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public class PaginationAction implements MenuItemAction {
  private final PaginationItem paginationItem;
  private final SettingsMenuManager settingsMenuManager;

  public PaginationAction(PaginationItem paginationItem) {
    this.paginationItem = paginationItem;
    this.settingsMenuManager = PlayerSettings.getInstance().getSettingsMenuManager();
  }

  @Override
  public void execute(SettingUser user, SettingsMenuItem menuItem, ClickType clickType) {
    int newPage = paginationItem.calculatePage(menuItem.getPage());
    settingsMenuManager.open(user.getPlayer(), newPage);
  }
}

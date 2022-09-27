package me.limbo56.playersettings.menu.actions;

import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.menu.renderers.PaginationRenderer.PaginationItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.event.inventory.ClickType;

public class PaginationAction implements InventoryItemAction {
  private final PaginationItem direction;
  private final int page;

  public PaginationAction(PaginationItem direction, int page) {
    this.direction = direction;
    this.page = page;
  }

  @Override
  public void execute(ClickType clickType, SettingUser user) {
    SettingsMenu.openMenu(user, direction.calculatePage(page));
  }
}

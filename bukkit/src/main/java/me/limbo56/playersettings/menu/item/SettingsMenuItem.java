package me.limbo56.playersettings.menu.item;

import java.util.function.Supplier;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.menu.action.MenuItemAction;
import me.limbo56.playersettings.menu.renderer.SettingsRenderer;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.Item;
import me.limbo56.playersettings.util.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.util.text.ReplaceModifier;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class SettingsMenuItem implements MenuItem {
  private final MenuItem menuItem;
  private final MenuItemAction clickAction;

  public SettingsMenuItem(MenuItem menuItem, MenuItemAction clickAction) {
    this.menuItem = menuItem;
    this.clickAction = clickAction;
  }

  public SettingsMenuItem(Supplier<MenuItem> menuItem, MenuItemAction clickAction) {
    this.menuItem = menuItem.get();
    this.clickAction = clickAction;
  }

  public void executeClickAction(ClickType type, SettingUser user) {
    clickAction.execute(user, this, type);
  }

  protected static void format(
      Player player, int value, InternalSetting setting, MenuItem menuItem) {
    ItemStack original = menuItem.getItemStack();
    ItemMeta templateMeta = setting.getItem().getItemStack().getItemMeta();
    format(player, value, setting, original, templateMeta);
  }

  protected static void format(
      Player player,
      int value,
      InternalSetting setting,
      ItemStack original,
      ItemMeta templateMeta) {
    int maxValue = setting.getMaxValue();
    Item.format(
        original,
        templateMeta,
        new PlaceholderAPIModifier(player),
        ReplaceModifier.of("%current%", setting.getValueAlias(value)),
        ReplaceModifier.of("%max%", setting.getValueAlias(maxValue)),
        ReplaceModifier.of("%next%", setting.getValueAlias(getNextValue(maxValue, value))),
        ReplaceModifier.of("%previous%", setting.getValueAlias(getPreviousValue(maxValue, value))));
  }

  private static int getNextValue(int value, int maxValue) {
    boolean isToggledValue = value < 0;
    int nextValue = isToggledValue ? -value : value + 1;
    return (nextValue > maxValue) ? 0 : nextValue;
  }

  private static int getPreviousValue(int value, int maxValue) {
    boolean isToggledValue = value < 0;
    int previousValue = isToggledValue ? -value : value - 1;
    return (previousValue < 0) ? maxValue : previousValue;
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

  public static class Context {
    private final SettingsMenu menu;
    private final SettingsRenderer renderer;

    public Context(SettingsMenu menu, SettingsRenderer renderer) {
      this.menu = menu;
      this.renderer = renderer;
    }

    public SettingsMenu getMenu() {
      return menu;
    }

    public SettingsRenderer getRenderer() {
      return renderer;
    }
  }
}

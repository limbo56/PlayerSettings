package me.limbo56.playersettings.menu.renderers;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.actions.ExecuteCommandsAction;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.util.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DismissRenderer implements MenuItemRenderer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @Override
  public void render(@NotNull MenuHolder menuHolder, int page) {
    ConfigurationSection dismissItemSection =
        PLUGIN.getItemsConfiguration().getFile().getConfigurationSection("dismiss");
    if (dismissItemSection == null) {
      return;
    }

    menuHolder.renderItem(buildMenuItem(menuHolder, dismissItemSection));
  }

  private SettingsMenuItem buildMenuItem(MenuHolder holder, ConfigurationSection configuration) {
    MenuItem item = MenuItem.deserialize(configuration);
    ItemStack translatedItemStack =
        ItemBuilder.translateItemStack(item.getItemStack(), holder.getOwner().getPlayer());
    ImmutableMenuItem menuItem = ImmutableMenuItem.copyOf(item).withItemStack(translatedItemStack);
    ExecuteCommandsAction onPressAction =
        new ExecuteCommandsAction(configuration.getStringList("onPress"));
    return new SettingsMenuItem(menuItem, onPressAction);
  }
}

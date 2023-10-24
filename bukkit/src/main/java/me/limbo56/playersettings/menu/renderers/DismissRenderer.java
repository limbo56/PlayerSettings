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
import org.bukkit.entity.Player;
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

    ImmutableMenuItem menuItem = getDismissItem(menuHolder, dismissItemSection);
    ExecuteCommandsAction onPressAction = getOnPressAction(dismissItemSection);

    menuHolder.renderItem(new SettingsMenuItem(menuItem, onPressAction));
  }

  @NotNull
  private static ImmutableMenuItem getDismissItem(
      MenuHolder holder, ConfigurationSection itemSection) {
    MenuItem item = MenuItem.deserialize(itemSection);
    Player player = holder.getOwner().getPlayer();
    ItemStack translatedItemStack = ItemBuilder.translateItemStack(item.getItemStack(), player);

    return ImmutableMenuItem.copyOf(item).withItemStack(translatedItemStack);
  }

  @NotNull
  private static ExecuteCommandsAction getOnPressAction(ConfigurationSection dismissItemSection) {
    return new ExecuteCommandsAction(dismissItemSection.getStringList("onPress"));
  }
}

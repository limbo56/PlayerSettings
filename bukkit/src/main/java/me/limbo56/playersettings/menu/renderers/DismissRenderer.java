package me.limbo56.playersettings.menu.renderers;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.SettingsMenuHolder;
import me.limbo56.playersettings.menu.SettingsMenuItem;
import me.limbo56.playersettings.menu.actions.DismissAction;
import me.limbo56.playersettings.util.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class DismissRenderer implements SettingsMenuItemRenderer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @Override
  public void render(@NotNull SettingsMenuHolder menuHolder, int page) {
    ConfigurationSection dismissItemSection =
        PLUGIN.getItemsConfiguration().getFile().getConfigurationSection("dismiss");
    if (dismissItemSection == null) {
      return;
    }

    MenuItem item = MenuItem.deserialize(dismissItemSection);
    ItemStack translateItemStack =
        ItemBuilder.translateItemStack(item.getItemStack(), menuHolder.getOwner().getPlayer());
    List<String> commands = dismissItemSection.getStringList("onPress");
    menuHolder.renderItem(
        new SettingsMenuItem(
            ImmutableMenuItem.copyOf(item).withItemStack(translateItemStack),
            new DismissAction(commands)));
  }
}

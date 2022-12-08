package me.limbo56.playersettings.menu.renderers;

import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.SettingsMenuHolder;
import me.limbo56.playersettings.menu.SettingsMenuItem;
import me.limbo56.playersettings.menu.actions.DismissAction;
import me.limbo56.playersettings.util.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.jetbrains.annotations.NotNull;

public class DismissRenderer implements SettingsMenuItemRenderer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @Override
  public void render(@NotNull SettingsMenuHolder menuHolder, int page) {
    ConfigurationSection dismissItemSection =
        PLUGIN.getItemsConfiguration().getFile().getConfigurationSection("dismiss");
    if (dismissItemSection == null) {
      return;
    }

    List<String> commands = dismissItemSection.getStringList("onPress");
    MenuItem item = MenuItem.deserialize(dismissItemSection);
    MenuItem translatedItem =
        ImmutableMenuItem.copyOf(item)
            .withItemStack(ItemBuilder.translateItemStack(item.getItemStack()));
    menuHolder.renderItem(new SettingsMenuItem(translatedItem, new DismissAction(commands)));
  }
}

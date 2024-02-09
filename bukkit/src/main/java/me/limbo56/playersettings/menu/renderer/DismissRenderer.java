package me.limbo56.playersettings.menu.renderer;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.menu.action.ExecuteCommandsAction;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.message.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.util.Item;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class DismissRenderer implements MenuItemRenderer {
  private final ItemsConfiguration itemsConfiguration;

  public DismissRenderer() {
    this.itemsConfiguration =
        PlayerSettings.getInstance()
            .getConfigurationManager()
            .getConfiguration(ItemsConfiguration.class);
  }

  @Override
  public void render(@NotNull SettingsMenu menu, int page) {
    ConfigurationSection section = itemsConfiguration.getFile().getConfigurationSection("dismiss");
    if (section == null) {
      return;
    }

    ImmutableMenuItem menuItem = getDismissItem(menu.getOwner().getPlayer(), section);
    ExecuteCommandsAction onPressAction = getOnPressAction(section);
    menu.renderItem(new SettingsMenuItem(menuItem, onPressAction));
  }

  private static ImmutableMenuItem getDismissItem(Player player, ConfigurationSection section) {
    MenuItem item = Parsers.MENU_ITEM_PARSER.parse(section);
    ItemStack itemStack = item.getItemStack();
    Item.format(itemStack, new PlaceholderAPIModifier(player));
    return ImmutableMenuItem.copyOf(item).withItemStack(itemStack);
  }

  @NotNull
  private static ExecuteCommandsAction getOnPressAction(ConfigurationSection section) {
    return new ExecuteCommandsAction(section.getStringList("onPress"));
  }
}

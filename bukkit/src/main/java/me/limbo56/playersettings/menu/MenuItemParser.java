package me.limbo56.playersettings.menu;

import static me.limbo56.playersettings.menu.ItemParser.ITEM_PARSER;

import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.util.data.Parser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

public class MenuItemParser implements Parser<ConfigurationSection, MenuItem> {
  public static final MenuItemParser MENU_ITEM_PARSER = new MenuItemParser();

  @Override
  public MenuItem parse(ConfigurationSection section) {
    ItemStack parsedItem = ITEM_PARSER.parse(section);
    int page = section.getInt("page", 0);
    int slot = section.getInt("slot", 0);
    return ImmutableMenuItem.of(parsedItem, page, slot);
  }
}

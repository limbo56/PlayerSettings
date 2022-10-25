package me.limbo56.playersettings.menu;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.menu.renderers.ItemRenderer;
import me.limbo56.playersettings.menu.renderers.PaginationRenderer;
import me.limbo56.playersettings.menu.renderers.SettingRenderer;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.ColorUtil;
import org.bukkit.configuration.ConfigurationSection;

public class SettingsMenu {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private static final ItemRenderer PAGINATION_RENDERER = new PaginationRenderer();
  private static final ItemRenderer SETTING_RENDERER = new SettingRenderer();

  public static void openMenu(SettingUser player, int page) {
    // Create menu
    ConfigurationSection menuConfiguration =
        plugin.getPluginConfiguration().getConfigurationSection("menu");
    String menuName = ColorUtil.translateColorCodes(menuConfiguration.getString("name"));
    int menuSize = menuConfiguration.getInt("size");
    SettingsInventory menu = new SettingsInventory(player.getUniqueId(), menuName, menuSize, page);
    PAGINATION_RENDERER.render(menu, page);
    SETTING_RENDERER.render(menu, page);

    // Open menu
    player.getPlayer().openInventory(menu.getInventory());
  }
}

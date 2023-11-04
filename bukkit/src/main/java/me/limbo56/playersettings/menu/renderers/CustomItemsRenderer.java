package me.limbo56.playersettings.menu.renderers;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.actions.ExecuteCommandsAction;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.util.ItemBuilder;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomItemsRenderer implements MenuItemRenderer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private static final List<String> IGNORE_LIST =
      Arrays.asList("enabled", "disabled", "dismiss", "next-page", "previous-page");

  @Override
  public void render(@NotNull MenuHolder menuHolder, int page) {
    YamlConfiguration itemsConfiguration = PLUGIN.getItemsConfiguration().getFile();
    List<String> ignoreList = getIgnoreList();

    for (String key : itemsConfiguration.getKeys(false)) {
      if (ignoreList.contains(key)) {
        continue;
      }

      ConfigurationSection customItemSection = itemsConfiguration.getConfigurationSection(key);
      if (customItemSection == null) {
        continue;
      }

      ImmutableMenuItem menuItem = getCustomItem(menuHolder, customItemSection);
      if (menuItem.getPage() != 0 && menuItem.getPage() != page) {
        continue;
      }

      ExecuteCommandsAction onPressAction = getOnPressAction(customItemSection);
      menuHolder.renderItem(new SettingsMenuItem(menuItem, onPressAction));
    }
  }

  @NotNull
  private static ImmutableMenuItem getCustomItem(
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

  private List<String> getIgnoreList() {
    List<String> ignored = new ArrayList<>(IGNORE_LIST);
    ignored.addAll(PLUGIN.getSettingsManager().getSettingMap().keySet());

    return ignored;
  }
}

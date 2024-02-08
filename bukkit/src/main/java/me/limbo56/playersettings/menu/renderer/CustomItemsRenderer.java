package me.limbo56.playersettings.menu.renderer;

import com.google.common.collect.Sets;
import java.util.*;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.configuration.ConfigurationManager;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.menu.action.ExecuteCommandsAction;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.util.Item;
import me.limbo56.playersettings.util.text.PlaceholderAPIModifier;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class CustomItemsRenderer implements MenuItemRenderer {
  private static final Set<String> IGNORE_LIST =
      Sets.newHashSet("enabled", "disabled", "dismiss", "next-page", "previous-page");
  private final ItemsConfiguration itemsConfiguration;
  private final SettingsConfiguration settingsConfiguration;

  public CustomItemsRenderer() {
    ConfigurationManager configurationManager =
        PlayerSettings.getInstance().getConfigurationManager();
    this.itemsConfiguration = configurationManager.getConfiguration(ItemsConfiguration.class);
    this.settingsConfiguration = configurationManager.getConfiguration(SettingsConfiguration.class);
  }

  @Override
  public void render(@NotNull SettingsMenu settingsMenu, int page) {
    Player player = settingsMenu.getOwner().getPlayer();
    getCustomItems(page, player).forEach(settingsMenu::renderItem);
  }

  private List<SettingsMenuItem> getCustomItems(int page, Player player) {
    Set<String> ignoreList = getIgnoreList();
    List<SettingsMenuItem> customItems = new ArrayList<>();

    for (String key : itemsConfiguration.getFile().getKeys(false)) {
      if (ignoreList.contains(key)) {
        continue;
      }

      ConfigurationSection itemSection = itemsConfiguration.getFile().getConfigurationSection(key);
      if (itemSection == null) {
        continue;
      }

      ImmutableMenuItem menuItem = getCustomItem(itemSection, player);
      if (menuItem.getPage() != 0 && menuItem.getPage() != page) {
        continue;
      }

      ExecuteCommandsAction onPressAction = getOnPressAction(itemSection);
      customItems.add(new SettingsMenuItem(menuItem, onPressAction));
    }

    return customItems;
  }

  private Set<String> getIgnoreList() {
    Set<String> ignored = new HashSet<>(IGNORE_LIST);
    ignored.addAll(settingsConfiguration.getSettingNames());
    return ignored;
  }

  @NotNull
  private ImmutableMenuItem getCustomItem(ConfigurationSection section, Player player) {
    MenuItem item = Parsers.MENU_ITEM_PARSER.parse(section);
    ItemStack itemStack = item.getItemStack();
    Item.format(itemStack, new PlaceholderAPIModifier(player));
    return ImmutableMenuItem.copyOf(item).withItemStack(itemStack);
  }

  @NotNull
  private ExecuteCommandsAction getOnPressAction(ConfigurationSection dismissItemSection) {
    return new ExecuteCommandsAction(dismissItemSection.getStringList("onPress"));
  }
}

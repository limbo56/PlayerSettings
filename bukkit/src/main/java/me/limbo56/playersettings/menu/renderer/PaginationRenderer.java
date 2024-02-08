package me.limbo56.playersettings.menu.renderer;

import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.menu.action.PaginationAction;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.setting.SettingsManager;
import me.limbo56.playersettings.util.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.util.text.TextMessage;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class PaginationRenderer implements MenuItemRenderer {
  private final SettingsManager settingsManager;
  private final ItemsConfiguration itemsConfiguration;

  public PaginationRenderer() {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.settingsManager = plugin.getSettingsManager();
    this.itemsConfiguration =
        plugin.getConfigurationManager().getConfiguration(ItemsConfiguration.class);
  }

  @Override
  public void render(@NotNull SettingsMenu settingsMenu, int page) {
    if (page > 1) {
      renderNavigationItem(PaginationItem.PREVIOUS, settingsMenu, page);
    }

    int highestPage = getHighestPage();
    if (page != highestPage && highestPage > 1) {
      renderNavigationItem(PaginationItem.NEXT, settingsMenu, page);
    }
  }

  private void renderNavigationItem(
      PaginationItem paginationItem, SettingsMenu settingsMenu, int page) {
    Player player = settingsMenu.getOwner().getPlayer();
    MenuItem menuItem = getNavigationItem(paginationItem, player, page);
    settingsMenu.renderItem(new SettingsMenuItem(menuItem, new PaginationAction(paginationItem)));
  }

  @NotNull
  private MenuItem getNavigationItem(PaginationItem paginationItem, Player player, int page) {
    MenuItem menuItem = paginationItem.getItem(itemsConfiguration.getFile());
    ItemStack itemStack = menuItem.getItemStack();
    formatNavigationItem(itemStack, player, page);
    return ImmutableMenuItem.copyOf(menuItem).withItemStack(itemStack);
  }

  private void formatNavigationItem(ItemStack itemStack, Player player, int page) {
    ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) {
      return;
    }

    TextMessage.Builder builder =
        TextMessage.builder()
            .replacePlaceholder("%current%", String.valueOf(page))
            .replacePlaceholder("%max%", String.valueOf(getHighestPage()))
            .addModifier(new PlaceholderAPIModifier(player));
    List<String> lore = meta.getLore();

    meta.setDisplayName(builder.from(meta.getDisplayName()).getFirstTextLine());
    if (lore != null) {
      meta.setLore(builder.from(lore).getTextLines());
    }

    itemStack.setItemMeta(meta);
  }

  private int getHighestPage() {
    boolean seen = false;
    int best = 0;
    for (Setting setting : settingsManager.getSettings()) {
      int page = setting.getItem().getPage();
      if (!seen || page > best) {
        seen = true;
        best = page;
      }
    }
    return seen ? best : 1;
  }

  public enum PaginationItem {
    NEXT("next-page", page -> page + 1),
    PREVIOUS("previous-page", page -> page - 1);

    private final String name;
    private final Function<Integer, Integer> pageCalculator;

    PaginationItem(String name, Function<Integer, Integer> pageCalculator) {
      this.name = name;
      this.pageCalculator = pageCalculator;
    }

    public MenuItem getItem(ConfigurationSection section) {
      return Parsers.MENU_ITEM_PARSER.parse(
          Objects.requireNonNull(
              section.getConfigurationSection(this.name),
              "Navigation item '" + this.name + "' not found"));
    }

    public int calculatePage(int page) {
      return pageCalculator.apply(page);
    }
  }
}

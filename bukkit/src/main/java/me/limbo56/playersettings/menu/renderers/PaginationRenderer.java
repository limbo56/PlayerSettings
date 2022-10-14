package me.limbo56.playersettings.menu.renderers;

import static me.limbo56.playersettings.menu.MenuItemParser.MENU_ITEM_PARSER;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.function.Function;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.menu.InventoryItem;
import me.limbo56.playersettings.menu.SettingsInventory;
import me.limbo56.playersettings.menu.actions.PaginationAction;
import me.limbo56.playersettings.util.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class PaginationRenderer implements ItemRenderer {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @Override
  public void render(@NotNull SettingsInventory inventory, int page) {
    if (page != 1) renderNavigationItem(inventory, PaginationItem.PREVIOUS);
    if (page != getHighestPage() && getHighestPage() > 1)
      renderNavigationItem(inventory, PaginationItem.NEXT);
  }

  private void renderNavigationItem(SettingsInventory menu, PaginationItem paginationItem) {
    ItemStack item = paginationItem.getItem().getItemStack();
    int slot = paginationItem.getItem().getSlot();
    int page = menu.getPage();
    ItemMeta meta = item.getItemMeta();

    // Format display name
    String formattedDisplayName =
        formatPaginationText(Text.from(meta.getDisplayName()), page).first();
    meta.setDisplayName(formattedDisplayName);

    // Format lore if present
    List<String> lore = meta.getLore();
    if (lore != null) {
      List<String> formattedLore = formatPaginationText(Text.from(lore), page).build();
      meta.setLore(formattedLore);
    }

    // Update meta and render item
    item.setItemMeta(meta);
    menu.renderItem(new InventoryItem(slot, item, new PaginationAction(paginationItem, page)));
  }

  private Text formatPaginationText(Text text, int page) {
    return text.placeholder("%current%", String.valueOf(page))
        .placeholder("%max%", String.valueOf(getHighestPage()));
  }

  private int getHighestPage() {
    return plugin.getSettingsContainer().getSettingMap().values().stream()
        .mapToInt(setting -> setting.getItem().getPage())
        .max()
        .orElse(1);
  }

  public enum PaginationItem {
    NEXT("next-page", page -> page + 1),
    PREVIOUS("previous-page", page -> page - 1);

    private final String section;
    private final Function<Integer, Integer> pageCalculator;

    PaginationItem(String section, Function<Integer, Integer> pageCalculator) {
      this.section = section;
      this.pageCalculator = pageCalculator;
    }

    public MenuItem getItem() {
      ConfigurationSection navigationItemSection =
          plugin.getItemsConfiguration().getConfigurationSection(this.section);
      Preconditions.checkNotNull(
          navigationItemSection, "Navigation item '" + this.section + "' not found");
      return MENU_ITEM_PARSER.parse(navigationItemSection);
    }

    public int calculatePage(int page) {
      return pageCalculator.apply(page);
    }
  }
}

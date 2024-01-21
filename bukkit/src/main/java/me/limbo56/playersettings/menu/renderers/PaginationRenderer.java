package me.limbo56.playersettings.menu.renderers;

import com.google.common.base.Preconditions;
import java.util.List;
import java.util.function.Function;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.actions.PaginationAction;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.util.ItemBuilder;
import me.limbo56.playersettings.util.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class PaginationRenderer implements MenuItemRenderer {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @Override
  public void render(@NotNull MenuHolder menuHolder, int page) {
    if (page > 1) {
      renderNavigationItem(PaginationItem.PREVIOUS, menuHolder, page);
    }

    int highestPage = getHighestPage();
    if (page != highestPage && highestPage > 1) {
      renderNavigationItem(PaginationItem.NEXT, menuHolder, page);
    }
  }

  private void renderNavigationItem(
      PaginationItem paginationItem, MenuHolder menuHolder, int page) {
    MenuItem defaultPaginationItem =
        ImmutableMenuItem.copyOf(paginationItem.getItem()).withPage(page);
    Player player = menuHolder.getOwner().getPlayer();
    ImmutableMenuItem formattedPaginationItem =
        ImmutableMenuItem.builder()
            .from(defaultPaginationItem)
            .itemStack(formatNavigationItemstack(defaultPaginationItem, player))
            .build();
    menuHolder.renderItem(
        new SettingsMenuItem(formattedPaginationItem, new PaginationAction(paginationItem)));
  }

  @NotNull
  private ItemStack formatNavigationItemstack(MenuItem paginationItem, Player player) {
    ItemStack item = ItemBuilder.translateItemStack(paginationItem.getItemStack(), player);
    ItemMeta meta = item.getItemMeta();
    int page = paginationItem.getPage();

    // Format display name
    String formattedDisplayName =
        fillPaginationPlaceholders(Text.from(meta.getDisplayName()), page).first();
    meta.setDisplayName(formattedDisplayName);

    // Format lore
    List<String> lore = meta.getLore();
    if (lore != null) {
      List<String> formattedLore = fillPaginationPlaceholders(Text.from(lore), page).build();
      meta.setLore(formattedLore);
    }

    item.setItemMeta(meta);
    return item;
  }

  private Text fillPaginationPlaceholders(Text text, int page) {
    return text.usePlaceholder("%current%", String.valueOf(page))
        .usePlaceholder("%max%", String.valueOf(getHighestPage()));
  }

  private int getHighestPage() {
    boolean seen = false;
    int best = 0;
    for (Setting setting : PLUGIN.getSettingsManager().getSettings()) {
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

    private final String section;
    private final Function<Integer, Integer> pageCalculator;

    PaginationItem(String section, Function<Integer, Integer> pageCalculator) {
      this.section = section;
      this.pageCalculator = pageCalculator;
    }

    public MenuItem getItem() {
      ConfigurationSection navigationItemSection =
          PLUGIN.getItemsConfiguration().getFile().getConfigurationSection(this.section);
      Preconditions.checkNotNull(
          navigationItemSection, "Navigation item '" + this.section + "' not found");
      return MenuItem.deserialize(navigationItemSection);
    }

    public int calculatePage(int page) {
      return pageCalculator.apply(page);
    }
  }
}

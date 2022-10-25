package me.limbo56.playersettings.menu.renderers;

import static me.limbo56.playersettings.menu.ItemParser.ITEM_PARSER;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.menu.InventoryItem;
import me.limbo56.playersettings.menu.SettingsInventory;
import me.limbo56.playersettings.menu.actions.AugmentSettingAction;
import me.limbo56.playersettings.menu.actions.ToggleSettingAction;
import me.limbo56.playersettings.util.Text;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public class SettingRenderer implements ItemRenderer {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();

  @Override
  public void render(@NotNull SettingsInventory inventory, int page) {
    Collection<Setting> settings = plugin.getSettingsContainer().getSettingMap().values();
    settings.stream()
        .filter(setting -> setting.getItem().getPage() == page)
        .forEach(setting -> renderSetting(inventory, setting));
  }

  public void renderSetting(@NotNull SettingsInventory inventory, @NotNull Setting setting) {
    String settingName = setting.getName();
    int menuSize = inventory.getSize();
    int slot = setting.getItem().getSlot();
    if (!isWithinBounds(menuSize, slot)) {
      String settingWarning =
          "Setting '"
              + settingName
              + "' is not between the bounds of the menu (0-"
              + menuSize
              + ")";
      plugin.getLogger().warning(settingWarning);
      return;
    }

    // Render setting item
    int value = inventory.getUser().getSettingWatcher().getValue(settingName);
    ItemStack settingItem = buildSettingItem(inventory, setting, value);
    inventory.renderItem(
        new InventoryItem(slot, settingItem, new AugmentSettingAction(this, inventory, setting)));

    // Render toggle item
    ConfigurationSection toggleItemSection =
        plugin.getItemsConfiguration().getConfigurationSection(value > 0 ? "enabled" : "disabled");
    Preconditions.checkNotNull(toggleItemSection, "Toggle item section is null");
    ItemStack toggleItem = ITEM_PARSER.parse(toggleItemSection);
    inventory.renderItem(
        new InventoryItem(slot + 9, toggleItem, new ToggleSettingAction(this, inventory, setting)));
  }

  private ItemStack buildSettingItem(SettingsInventory inventory, Setting setting, int value) {
    ItemStack itemStack = inventory.getInventory().getItem(setting.getItem().getSlot());
    if (itemStack == null) {
      itemStack = setting.getItem().getItemStack().clone();
    }

    // Display setting value as item amount
    itemStack.setAmount(Math.max(value, 1));

    ItemMeta meta = itemStack.getItemMeta();
    if (meta != null) {
      // Format lore
      List<String> lore = meta.getLore();
      if (lore != null) {
        meta.setLore(formatSettingText(Text.from(lore), setting, value).build());
      }

      // Add or remove glow
      if (value > 0) {
        meta.addEnchant(Enchantment.LURE, 1, false);
        meta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
      } else {
        meta.removeEnchant(Enchantment.LURE);
      }

      itemStack.setItemMeta(meta);
    }
    return itemStack;
  }

  private Text formatSettingText(Text text, Setting setting, int value) {
    int maxValue = setting.getMaxValue();
    int nextValue = value + 1 > maxValue ? 0 : value + 1;
    int previousValue = value - 1 < 0 ? maxValue : value - 1;
    return text.placeholder("%current%", String.valueOf(value))
        .placeholder("%max%", String.valueOf(maxValue))
        .placeholder("%next%", String.valueOf(nextValue))
        .placeholder("%previous%", String.valueOf(previousValue));
  }

  private boolean isWithinBounds(int size, int slot) {
    return slot < size && (slot + 9) <= size;
  }
}

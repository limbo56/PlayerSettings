package me.limbo56.playersettings.menu.renderers;

import static me.limbo56.playersettings.menu.ItemParser.ITEM_PARSER;
import static me.limbo56.playersettings.settings.SettingValue.SETTING_VALUE;

import com.google.common.base.Preconditions;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
    if (PlayerSettingsProvider.isToggleButtonEnabled()) {
      ConfigurationSection toggleItemSection =
          Preconditions.checkNotNull(
              plugin
                  .getItemsConfiguration()
                  .getConfigurationSection(value > 0 ? "enabled" : "disabled"),
              "Toggle item section is null");
      ItemStack toggleItem = ITEM_PARSER.parse(toggleItemSection);
      inventory.renderItem(
          new InventoryItem(
              slot + 9, toggleItem, new ToggleSettingAction(this, inventory, setting)));
    }
  }

  private ItemStack buildSettingItem(SettingsInventory inventory, Setting setting, int value) {
    ItemStack itemStack = inventory.getInventory().getItem(setting.getItem().getSlot());
    if (itemStack == null) {
      itemStack = setting.getItem().getItemStack().clone();
    }

    // Display setting value as item amount
    itemStack.setAmount(Math.max(value, 1));

    ItemMeta templateMeta = setting.getItem().getItemStack().getItemMeta();
    ItemMeta meta = itemStack.getItemMeta();
    if (meta != null) {
      // Format lore
      List<String> lore =
          Optional.ofNullable(templateMeta).map(ItemMeta::getLore).orElse(new ArrayList<>());
      meta.setLore(formatSettingLore(Text.from(lore), value, setting.getMaxValue()));

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

  private List<String> formatSettingLore(Text text, int value, int maxValue) {
    int nextValue = getNextValue(maxValue, value);
    int previousValue = getPreviousValue(maxValue, value);
    return text.placeholder("%current%", SETTING_VALUE.format(value))
        .placeholder("%max%", SETTING_VALUE.format(maxValue))
        .placeholder("%next%", SETTING_VALUE.format(nextValue))
        .placeholder("%previous%", SETTING_VALUE.format(previousValue))
        .build();
  }

  private boolean isWithinBounds(int size, int slot) {
    int offset = PlayerSettingsProvider.isToggleButtonEnabled() ? 9 : 0;
    return slot < size && (slot + offset) < size;
  }

  private int getPreviousValue(int maxValue, int value) {
    int previousValue = value - 1;
    if (value < 0) {
      previousValue = -value;
    } else if (previousValue < 0) {
      previousValue = maxValue;
    }
    return previousValue;
  }

  private int getNextValue(int maxValue, int value) {
    int nextValue = value + 1;
    if (value < 0) {
      nextValue = -value;
    } else if (nextValue > maxValue) {
      nextValue = 0;
    }
    return nextValue;
  }
}

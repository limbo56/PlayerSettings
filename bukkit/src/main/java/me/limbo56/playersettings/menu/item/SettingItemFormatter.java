package me.limbo56.playersettings.menu.item;

import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.util.Text;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public final class SettingItemFormatter {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  public static void formatSettingItem(
      ItemStack itemStack, ItemMeta templateMeta, Player player, Setting setting, int value) {
    ItemMeta meta = itemStack.getItemMeta();
    if (meta == null) {
      return;
    }

    formatSettingItemMeta(meta, templateMeta, player, setting, value);
    itemStack.setItemMeta(meta);
  }

  public static void formatSettingItemMeta(
      ItemMeta meta, ItemMeta templateMeta, Player player, Setting setting, int value) {
    String displayName =
        applySettingPlaceholders(Text.from(templateMeta.getDisplayName()), player, setting, value)
            .first();
    List<String> lore =
        applySettingPlaceholders(Text.from(templateMeta.getLore()), player, setting, value).build();

    meta.setDisplayName(displayName);
    meta.setLore(lore);
  }

  @NotNull
  public static Text applySettingPlaceholders(
      Text text, Player player, Setting setting, int value) {
    SettingsConfiguration settingsConfiguration = PLUGIN.getSettingsConfiguration();

    int maxValueInteger = setting.getMaxValue();
    String currentValue = settingsConfiguration.formatSettingValue(setting, value);
    String maxValue = settingsConfiguration.formatSettingValue(setting, maxValueInteger);
    String nextValue =
        settingsConfiguration.formatSettingValue(setting, getNextValue(maxValueInteger, value));
    String previousValue =
        settingsConfiguration.formatSettingValue(setting, getPreviousValue(maxValueInteger, value));

    return text.usePlaceholder("%current%", currentValue)
        .usePlaceholder("%max%", maxValue)
        .usePlaceholder("%next%", nextValue)
        .usePlaceholder("%previous%", previousValue)
        .usePlaceholderApi(player);
  }

  private static int getNextValue(int value, int maxValue) {
    boolean isToggledValue = value < 0;
    int nextValue = isToggledValue ? -value : value + 1;

    return (nextValue > maxValue) ? 0 : nextValue;
  }

  private static int getPreviousValue(int value, int maxValue) {
    boolean isToggledValue = value < 0;
    int previousValue = isToggledValue ? -value : value - 1;

    return (previousValue < 0) ? maxValue : previousValue;
  }
}

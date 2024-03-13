package me.limbo56.playersettings.menu.item;

import com.cryptomorin.xseries.XItemStack;
import com.google.common.base.Preconditions;
import java.util.function.Function;
import java.util.function.Supplier;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.configuration.ConfigurationManager;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import me.limbo56.playersettings.configuration.SettingsConfiguration;
import me.limbo56.playersettings.configuration.parser.Parsers;
import me.limbo56.playersettings.menu.action.ToggleSettingAction;
import me.limbo56.playersettings.setting.InternalSetting;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

public final class ToggleSettingItem extends SettingsMenuItem {
  public ToggleSettingItem(SettingsMenuItem.Context context, InternalSetting setting) {
    super(
        new ToggleSettingItemSupplier(context.getMenu().getOwner(), setting),
        new ToggleSettingAction(context, setting));
  }

  private static class ToggleSettingItemSupplier implements Supplier<MenuItem> {
    private final SettingUser user;
    private final InternalSetting setting;
    private final SettingsConfiguration settingsConfiguration;
    private final ItemsConfiguration itemsConfiguration;

    private ToggleSettingItemSupplier(SettingUser user, InternalSetting setting) {
      this.user = user;
      this.setting = setting;

      ConfigurationManager configurationManager =
          PlayerSettings.getInstance().getConfigurationManager();
      this.settingsConfiguration =
          configurationManager.getConfiguration(SettingsConfiguration.class);
      this.itemsConfiguration = configurationManager.getConfiguration(ItemsConfiguration.class);
    }

    @Override
    public MenuItem get() {
      int value = user.getSettingWatcher().getValue(setting.getName());
      String togglePath = value > 0 ? "enabled" : "disabled";
      ItemStack itemStack = getToggleItem(value);
      ItemMeta templateMeta = itemStack.getItemMeta();
      int page = setting.getItem().getPage();
      int slot = setting.getItem().getSlot() + 9;

      // Apply overrides
      ConfigurationSection overridesSection =
          settingsConfiguration.getSettingOverridesSection(setting.getName());
      if (overridesSection != null) {
        ConfigurationSection toggleOverrides =
            overridesSection.getConfigurationSection(togglePath + "-item");
        if (toggleOverrides != null) {
          itemStack = XItemStack.edit(itemStack, toggleOverrides, Function.identity(), null);
          slot = toggleOverrides.getInt("slot", slot);
        }
      }

      // Apply meta formatting
      if (templateMeta != null) {
        format(user.getPlayer(), value, setting, itemStack, templateMeta);
      }

      return ImmutableMenuItem.of(itemStack, page, slot);
    }

    @NotNull
    private ItemStack getToggleItem(int value) {
      String path = value > 0 ? "enabled" : "disabled";
      ConfigurationSection toggleItemSection =
          Preconditions.checkNotNull(
              itemsConfiguration.getFile().getConfigurationSection(path),
              "Toggle item section is null");
      return Parsers.SETTING_ITEM_PARSER.parse(toggleItemSection, value).getItemStack();
    }
  }
}

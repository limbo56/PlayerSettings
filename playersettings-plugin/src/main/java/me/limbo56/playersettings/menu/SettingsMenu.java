package me.limbo56.playersettings.menu;

import com.google.common.collect.Iterables;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.YmlConfiguration;
import me.limbo56.playersettings.settings.ConfigurationSetting;
import me.limbo56.playersettings.settings.SPlayer;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static me.limbo56.playersettings.utils.ItemUtils.addGlow;
import static me.limbo56.playersettings.utils.ItemUtils.removeGlow;

public class SettingsMenu {
    public static void openMenu(SPlayer sPlayer, int page) {
        List<Setting> settings = getPageSettings(page);

        // Create holder
        SettingsHolder menu = new SettingsHolder(
                getConfiguration().getString("Name"),
                getConfiguration().getInt("Size"),
                page
        );

        if (page != getHighestPage() && getHighestPage() > 1) {
            ConfigurationSetting nextPage = new ConfigurationSetting("NextPage");
            ItemStack item = nextPage.getItem();
            replaceData(item, page);
            menu.renderItem(new CustomItem(nextPage.getSlot(), item, player ->
                    SettingsMenu.openMenu(sPlayer, page + 1)
            ));
        }

        if (page != 1) {
            ConfigurationSetting previousPage = new ConfigurationSetting("PreviousPage");
            ItemStack item = previousPage.getItem();
            replaceData(item, page);
            menu.renderItem(new CustomItem(previousPage.getSlot(), item, player ->
                    SettingsMenu.openMenu(sPlayer, page - 1)
            ));
        }

        // Add items & open inventory
        settings.forEach(setting -> addSetting(sPlayer, menu, setting));
        sPlayer.getPlayer().openInventory(menu.getInventory());
    }

    private static void replaceData(ItemStack itemStack, int page) {
        ItemMeta meta = itemStack.getItemMeta();
        Optional<List<String>> lore = Optional.ofNullable(meta.getLore());
        lore.ifPresent(strings -> {
            Collections.replaceAll(strings, "%current%", String.valueOf(page));
            Collections.replaceAll(strings, "%max%", String.valueOf(getHighestPage()));
        });
        meta.setDisplayName(meta.getDisplayName()
                .replaceAll("%current%", String.valueOf(page))
                .replaceAll("%max%", String.valueOf(getHighestPage()))
        );
        itemStack.setItemMeta(meta);
    }

    private static void addSetting(SPlayer sPlayer, SettingsHolder menu, Setting setting) {
        int menuSize = menu.getSize();
        ItemStack settingItem = setting.getItem();
        ItemStack toggleItem;

        // Check if the item is between the bounds of the menu
        if (setting.getSlot() > menuSize && (setting.getSlot() + 9) > menuSize) {
            Logger logger = PlayerSettings.getPlugin().getLogger();
            String settingWarning = "Setting %s is not between the bounds of the menu (%d-%d)";
            logger.warning(String.format(settingWarning, setting.getRawName(), 0, menuSize));
            return;
        }

        // Add glow if enabled
        int value = sPlayer.getSettingWatcher().getValue(setting);
        if (value > 0) {
            toggleItem = new ConfigurationSetting("Enabled").getItem();
            addGlow(settingItem);
        } else {
            toggleItem = new ConfigurationSetting("Disabled").getItem();
            removeGlow(settingItem);
        }

        menu.renderSetting(setting, toggleItem, value);
    }

    private static int getHighestPage() {
        Collection<Setting> settings = PlayerSettings.getPlugin().getSettingsRegistry().getStored().values();
        return settings.stream()
                .max(Comparator.comparingInt(Setting::getPage))
                .orElse(Iterables.get(settings, 0))
                .getPage();
    }

    private static List<Setting> getPageSettings(int page) {
        return PlayerSettings.getPlugin().getSettingsRegistry()
                .getStored().values().stream()
                .filter(setting -> setting.getPage() == page)
                .collect(Collectors.toList());
    }

    private static YmlConfiguration getConfiguration() {
        return PlayerSettings.getPlugin().getConfigurationStore().getStored().get("menu");
    }
}

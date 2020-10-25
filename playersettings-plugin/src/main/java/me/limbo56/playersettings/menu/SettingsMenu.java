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

    private static final ItemStack ENABLED_ITEM = new ConfigurationSetting("Enabled").getItem();
    private static final ItemStack DISABLED_ITEM = new ConfigurationSetting("Disabled").getItem();

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
            replacePaginationInfo(item, page);
            menu.renderItem(new CustomItem(nextPage.getSlot(), item, player ->
                    SettingsMenu.openMenu(sPlayer, page + 1)
            ));
        }

        if (page != 1) {
            ConfigurationSetting previousPage = new ConfigurationSetting("PreviousPage");
            ItemStack item = previousPage.getItem();
            replacePaginationInfo(item, page);
            menu.renderItem(new CustomItem(previousPage.getSlot(), item, player ->
                    SettingsMenu.openMenu(sPlayer, page - 1)
            ));
        }

        // Add items & open inventory
        settings.forEach(setting -> addSetting(sPlayer, menu, setting));
        sPlayer.getPlayer().openInventory(menu.getInventory());
    }

    private static void replacePaginationInfo(ItemStack itemStack, int page) {
        ItemMeta meta = itemStack.getItemMeta();

        // Replace in lore
        Optional.ofNullable(meta.getLore()).ifPresent(lore -> {
            Collections.replaceAll(lore, "%current%", String.valueOf(page));
            Collections.replaceAll(lore, "%max%", String.valueOf(getHighestPage()));
        });

        // Replace in display name
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
        if (sPlayer.getSettingWatcher().getValue(setting)) {
            toggleItem = ENABLED_ITEM;
            addGlow(settingItem);
        } else {
            toggleItem = DISABLED_ITEM;
            removeGlow(settingItem);
        }

        menu.renderSetting(setting, toggleItem);
    }

    private static int getHighestPage() {
        Collection<Setting> settings = PlayerSettings.getPlugin().getSettingStore().getStored().values();
        return settings.stream()
                .max(Comparator.comparingInt(Setting::getPage))
                .orElse(Iterables.get(settings, 0))
                .getPage();
    }

    private static List<Setting> getPageSettings(int page) {
        return PlayerSettings.getPlugin().getSettingStore()
                .getStored().values().stream()
                .filter(setting -> setting.getPage() == page)
                .collect(Collectors.toList());
    }

    private static YmlConfiguration getConfiguration() {
        return PlayerSettings.getPlugin().getConfigurationStore().getStored().get("menu");
    }
}

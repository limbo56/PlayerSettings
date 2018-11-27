package me.limbo56.playersettings.menu.settings;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.utils.item.ItemBuilder;
import me.limbo56.playersettings.utils.storage.Store;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class VisibilitySetting implements Store<Setting> {
    private Setting visibilitySetting;

    @Override
    public void register() {
        String permission = "setting.visibility";
        String name = "&3Visibility";
        String lore = "&7Shows and hides other players from your sight";

        SettingWatcher visibilityWatcher = new SettingWatcher(permission, true);
        ItemStack visibilityItem = new ItemBuilder(Material.EYE_OF_ENDER)
                .name(name)
                .lore(lore)
                .build();

        visibilitySetting = new Setting("Visibility", visibilityItem, visibilityWatcher);
    }

    @Override
    public void unregister() {
        visibilitySetting = null;
    }

    @Override
    public Setting getStored() {
        return visibilitySetting;
    }
}

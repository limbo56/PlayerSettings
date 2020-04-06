package me.limbo56.playersettings.settings.defined;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class VisibilitySetting implements Setting {
    @Override
    public String getRawName() {
        return "visibility_setting";
    }

    @Override
    public ItemStack getItem() {
        return Item.builder()
                .material(XMaterial.ENDER_EYE.parseMaterial())
                .name("&aVisibility")
                .lore("&7Shows and hides other")
                .lore("&7players from your sight")
                .build();
    }

    @Override
    public int getPage() {
        return 1;
    }

    @Override
    public int getSlot() {
        return 29;
    }

    @Override
    public int getDefaultValue() {
        return 1;
    }

    @Override
    public int getMaxValue() {
        return 1;
    }

    public static class VisibilitySettingCallback implements SettingCallback {
        @Override
        public void notifyChange(Setting setting, Player player, int newValue) {
            if (newValue > 1) {
                Bukkit.getOnlinePlayers().forEach(player::showPlayer);
            } else {
                Bukkit.getOnlinePlayers().forEach(player::hidePlayer);
            }
        }
    }
}

package me.limbo56.playersettings.settings.defined;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class FlySetting implements Setting {
    @Override
    public String getRawName() {
        return "fly_setting";
    }

    @Override
    public ItemStack getItem() {
        return Item.builder()
                .material(Material.FEATHER)
                .name("&aFly")
                .lore("&7Change the way you move")
                .lore("&7around the map")
                .build();
    }

    @Override
    public int getPage() {
        return 1;
    }

    @Override
    public int getSlot() {
        return 7;
    }

    @Override
    public boolean getDefaultValue() {
        return false;
    }

    public static class FlySettingCallback implements SettingCallback {
        @Override
        public void notifyChange(Setting setting, Player player, boolean newValue) {
            player.setAllowFlight(newValue);
            player.setFlying(newValue);
        }
    }
}

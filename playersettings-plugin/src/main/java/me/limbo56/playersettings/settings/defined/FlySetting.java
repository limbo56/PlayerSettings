package me.limbo56.playersettings.settings.defined;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.GameMode;
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
                .material(XMaterial.FEATHER.parseMaterial())
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
    public int getDefaultValue() {
        return 0;
    }

    @Override
    public int getMaxValue() {
        return 10;
    }

    public static class FlySettingCallback implements SettingCallback {
        @Override
        public void notifyChange(Setting setting, Player player, int newValue) {
            GameMode gm = player.getGameMode();
            if (gm != GameMode.CREATIVE && gm != GameMode.SPECTATOR) {
                player.setAllowFlight(newValue > 0);
                player.setFlying(newValue > 0);
            }
            player.setFlySpeed(Math.max(1, newValue) * 0.1F);
        }
    }
}

package me.limbo56.playersettings.settings.defined;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedSetting implements Setting {
    @Override
    public String getRawName() {
        return "speed_setting";
    }

    @Override
    public ItemStack getItem() {
        return Item.builder()
                .material(Material.SUGAR)
                .name("&aSpeed Boost")
                .lore("&7Ever wonder what happens")
                .lore("&7when you eat too much sugar?")
                .build();
    }

    @Override
    public int getPage() {
        return 1;
    }

    @Override
    public int getSlot() {
        return 1;
    }

    @Override
    public boolean getDefaultValue() {
        return false;
    }

    public static class SpeedSettingCallback implements SettingCallback {
        @Override
        public void notifyChange(Setting setting, Player player, boolean newValue) {
            if (newValue) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 2));
            } else {
                player.removePotionEffect(PotionEffectType.SPEED);
            }
        }
    }
}

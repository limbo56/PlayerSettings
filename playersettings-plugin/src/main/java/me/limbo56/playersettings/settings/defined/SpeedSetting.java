package me.limbo56.playersettings.settings.defined;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.utils.Item;
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
                .material(XMaterial.SUGAR.parseMaterial())
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
    public int getDefaultValue() {
        return 0;
    }

    @Override
    public int getMaxValue() {
        return 5;
    }

    public static class SpeedSettingCallback implements SettingCallback {
        @Override
        public void notifyChange(Setting setting, Player player, int newValue) {
            if (newValue > 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0));
                player.setWalkSpeed(newValue == 1 ? 0.2F * 1.4F : newValue * 0.2F);
            } else {
                player.removePotionEffect(PotionEffectType.SPEED);
                player.setWalkSpeed(0.2F);
            }
        }
    }
}

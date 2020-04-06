package me.limbo56.playersettings.settings.defined;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpSetting implements Setting {
    @Override
    public String getRawName() {
        return "jump_setting";
    }

    @Override
    public ItemStack getItem() {
        return Item.builder()
                .material(XMaterial.SLIME_BLOCK.parseMaterial())
                .name("&aJump Boost")
                .lore("&7Ever wanted to be a slime?")
                .lore("&7Click this to feel like one")
                .build();
    }

    @Override
    public int getPage() {
        return 1;
    }

    @Override
    public int getSlot() {
        return 3;
    }

    @Override
    public int getDefaultValue() {
        return 0;
    }

    @Override
    public int getMaxValue() {
        return 1;
    }

    public static class JumpSettingCallback implements SettingCallback {
        @Override
        public void notifyChange(Setting setting, Player player, int newValue) {
            if (newValue > 0) {
                player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, 2));
            } else {
                player.removePotionEffect(PotionEffectType.JUMP);
            }
        }
    }
}

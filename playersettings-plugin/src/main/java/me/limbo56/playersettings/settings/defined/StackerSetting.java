package me.limbo56.playersettings.settings.defined;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StackerSetting implements Setting {

    @Override
    public String getRawName() {
        return "stacker_setting";
    }

    @Override
    public ItemStack getItem() {
        return Item.builder()
                .material(XMaterial.SADDLE.parseMaterial())
                .name("&aStacker")
                .lore("&7Grants the ability to stack")
                .lore("&7players on top of your head")
                .lore("&7or others to stack you")
                .build();
    }

    @Override
    public int getPage() {
        return 1;
    }

    @Override
    public int getSlot() {
        return 5;
    }

    @Override
    public boolean getDefaultValue() {
        return false;
    }

    public static class StackerSettingCallback implements SettingCallback {

        @Override
        public void notifyChange(Setting setting, Player player, boolean newValue) {
            if (newValue) {
                return;
            }

            Entity vehicle = player.getVehicle();

            if (!(vehicle instanceof Player)) {
                return;
            }

            vehicle.eject();
            player.eject();
        }
    }
}

package me.limbo56.playersettings.settings.defined;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.utils.Item;
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
    public int getDefaultValue() {
        return 0;
    }

    @Override
    public int getMaxValue() {
        return 1;
    }
}

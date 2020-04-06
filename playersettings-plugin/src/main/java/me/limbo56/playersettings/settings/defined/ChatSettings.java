package me.limbo56.playersettings.settings.defined;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.inventory.ItemStack;

public class ChatSettings implements Setting {
    @Override
    public String getRawName() {
        return "chat_setting";
    }

    @Override
    public ItemStack getItem() {
        return Item.builder()
                .material(XMaterial.PAPER.parseMaterial())
                .name("&aChat")
                .lore("&7Toggle the chat on or off")
                .build();
    }

    @Override
    public int getPage() {
        return 1;
    }

    @Override
    public int getSlot() {
        return 33;
    }

    @Override
    public int getDefaultValue() {
        return 1;
    }

    @Override
    public int getMaxValue() {
        return 1;
    }
}

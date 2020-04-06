package me.limbo56.playersettings.settings;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.YmlConfiguration;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Stream;

public class ConfigurationSetting implements Setting {
    private String rawName;
    private ConfigurationSection section;

    public ConfigurationSetting(String rawName) {
        this.rawName = rawName;
        this.section = getConfiguration().getConfigurationSection(rawName);
    }

    @Override
    public String getRawName() {
        return rawName;
    }

    public ItemStack getItem() {
        String displayName = section.getString("name");
        String material = section.getString("material");
        int amount = section.getInt("amount");
        List<String> lore = section.getStringList("lore");

        String materialName;
        byte data = -1;
        boolean hasMaterial;
        ItemStack parsedItem;

        if (material.contains(":")) {
            hasMaterial = Stream
                    .of(Material.values())
                    .anyMatch(e -> e.name().equals(material.split(":")[0]));
            materialName = material.split(":")[0];
            data = Byte.parseByte(material.split(":")[1]);
        } else {
            hasMaterial = Stream.of(Material.values()).anyMatch(e -> e.name().equals(material));
            materialName = material;
        }

        if (hasMaterial || (XMaterial.isNewVersion() && materialName.startsWith("LEGACY"))) {
            parsedItem = new ItemStack(Material.valueOf(materialName), amount, data);
        } else {
            parsedItem = XMaterial
                    .matchXMaterial(material)
                    .orElse(XMaterial.BEDROCK)
                    .parseItem(true);
        }

        return Item.builder()
                .item(parsedItem)
                .name(displayName)
                .amount(amount)
                .lore(lore)
                .build();
    }

    public boolean isEnabled() {
        return section.getBoolean("enabled");
    }

    public boolean getExecuteOnJoin() {
        return section.getBoolean("executeOnJoin");
    }

    @Override
    public int getPage() {
        return section.getInt("page");
    }

    @Override
    public int getSlot() {
        return section.getInt("slot");
    }

    @Override
    public int getDefaultValue() {
        return section.getInt("default");
    }

    @Override
    public int getMaxValue() {
        return Math.max(1, section.getInt("max"));
    }

    private YmlConfiguration getConfiguration() {
        return PlayerSettings.getPlugin().getConfiguration("items");
    }
}

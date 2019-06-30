package me.limbo56.playersettings.settings;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.configuration.YmlConfiguration;
import me.limbo56.playersettings.utils.Item;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.ItemStack;

import java.util.List;

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

        Item.ItemBuilder item;

        // Check if the material has data
        if (material.contains(":")) {
            // Split the material and the data
            String[] splitMaterial = material.split(":");

            item = Item.builder()
                    .material(Material.valueOf(splitMaterial[0]))
                    .data(Byte.valueOf(splitMaterial[1]));
        } else {
            item = Item.builder().material(Material.valueOf(material));
        }

        return item.name(displayName).amount(amount).lore(lore).build();
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
    public boolean getDefaultValue() {
        return section.getBoolean("default");
    }

    private YmlConfiguration getConfiguration() {
        return PlayerSettings.getPlugin().getConfiguration("items");
    }
}

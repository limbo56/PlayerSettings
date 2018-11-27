package me.limbo56.playersettings.utils.item;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.ItemsConfiguration;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConfigurationItem {
    private static ItemsConfiguration itemsConfiguration = (ItemsConfiguration) PlayerSettings.getPlugin().getConfigurationStore().getStored().get("items");
    private String name;
    private String startingPath;

    public ConfigurationItem(String name, String stringPath) {
        this.name = name;
        this.startingPath = stringPath + ".";
    }

    public ConfigurationItem(String name) {
        this(name, "");
    }

    public ItemStack getItem() {
        String path = startingPath + name + ".";

        String displayName = itemsConfiguration.getString(path + "name");
        String material = itemsConfiguration.getString(path + "material");
        int amount = itemsConfiguration.getInt(path + "amount");
        List<String> lore = itemsConfiguration.getStringList(path + "lore");

        ItemBuilder itemBuilder;

        // Check if the material has data
        if (displayName.contains(":")) {
            // Split the material and the data
            String[] splitMaterial = material.split(":");

            itemBuilder = new ItemBuilder(Material.valueOf(splitMaterial[0]));
            itemBuilder.data(Byte.valueOf(splitMaterial[1]));
        } else {
            itemBuilder = new ItemBuilder(Material.valueOf(material));
        }

        return itemBuilder.amount(amount).lore(lore).build();
    }
}

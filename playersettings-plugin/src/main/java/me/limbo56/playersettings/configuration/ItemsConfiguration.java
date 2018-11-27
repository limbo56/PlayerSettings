package me.limbo56.playersettings.configuration;

import com.google.common.base.Preconditions;
import me.limbo56.playersettings.utils.item.ConfigurationItem;
import me.limbo56.playersettings.utils.item.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class ItemsConfiguration extends YmlConfiguration {
    ItemsConfiguration() {
        super("items");
    }

    @Override
    protected void addDefaults() {
        // Enable item
        addDefault("Enabled.name", "&a&lOn");
        addDefault("Enabled.lore", Collections.singletonList("&7Click to &cdisable"));
        addDefault("Enabled.material", "INK_SACK:10");

        // Disable item
        addDefault("Disabled.name", "&c&lOff");
        addDefault("Disabled.lore", Collections.singletonList("&7Click to &aenable"));
        addDefault("Disabled.material", "INK_SACK:8");
    }

    public ItemStack createSetting(String rawName) {
        ItemBuilder itemBuilder = new ItemBuilder(Material.DIRT, 1);
        itemBuilder.name("&e" + rawName);
        itemBuilder.lore(Collections.singletonList(""));

        return createSetting(rawName, itemBuilder.build());
    }

    public ItemStack createSetting(String rawName, ItemStack itemStack) {
        Preconditions.checkNotNull(rawName);
        // Check if the item already exists
        if (getString(rawName + ".name") != null) {
            return new ConfigurationItem(rawName).getItem();
        }

        String data = itemStack.getData() != null ? ":" + String.valueOf(itemStack.getData().getData()) : "";

        // Create the item in the configuration
        addDefault(rawName + ".enabled", true);
        addDefault(rawName + ".name", itemStack.getItemMeta().getDisplayName());
        addDefault(rawName + ".material", itemStack.getType() + data);
        addDefault(rawName + ".amount", itemStack.getAmount());
        addDefault(rawName + ".lore", itemStack.getItemMeta().getLore());
        addDefault(rawName + ".page", 1);
        addDefault(rawName + ".slot", 0);

        return new ConfigurationItem(rawName).getItem();
    }
}

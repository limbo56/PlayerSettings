package me.limbo56.playersettings.configuration;

import com.google.common.base.Preconditions;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.settings.ConfigurationSetting;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;

public class ItemsConfiguration extends YmlConfiguration {
    ItemsConfiguration(PlayerSettings plugin) {
        super(plugin, "items");
    }

    @Override
    protected void addDefaults() {
        // Enable item
        addDefault("Enabled.name", "&a&lOn");
        addDefault("Enabled.lore", Collections.singletonList("&7Click to &cdisable"));
        addDefault("Enabled.material", "INK_SACK:10");
        addDefault("Enabled.amount", 1);

        // Disable item
        addDefault("Disabled.name", "&c&lOff");
        addDefault("Disabled.lore", Collections.singletonList("&7Click to &aenable"));
        addDefault("Disabled.material", "INK_SACK:8");
        addDefault("Disabled.amount", 1);

        // Next page item
        addDefault("NextPage.name", "&aNext Page &7(%current%/%max%)");
        addDefault("NextPage.lore", Collections.emptyList());
        addDefault("NextPage.material", "ARROW:0");
        addDefault("NextPage.amount", 1);
        addDefault("NextPage.slot", 53);

        // Next page item
        addDefault("PreviousPage.name", "&cPrevious Page &7(%current%/%max%)");
        addDefault("PreviousPage.lore", Collections.emptyList());
        addDefault("PreviousPage.material", "ARROW:0");
        addDefault("PreviousPage.amount", 1);
        addDefault("PreviousPage.slot", 45);
    }

    public ConfigurationSetting getSetting(Setting setting) {
        String rawName = setting.getRawName();
        ItemStack itemStack = setting.getItem();
        String data = itemStack.getData() != null ? ":" + itemStack.getData().getData() : "";

        Preconditions.checkNotNull(rawName);

        // Check if the item doesn't exist
        if (!contains(rawName)) {
            createSetting(setting, rawName, itemStack, data);
        }

        return new ConfigurationSetting(rawName);
    }

    private void createSetting(Setting setting, String rawName, ItemStack itemStack, String data) {
        Collections.replaceAll(itemStack.getItemMeta().getLore(), "§", "&");

        // Create the item in the configuration
        set(rawName + ".enabled", true);
        set(rawName + ".name", itemStack.getItemMeta().getDisplayName().replaceAll("§", "&"));
        set(rawName + ".material", itemStack.getType() + data);
        set(rawName + ".default", setting.getDefaultValue());
        set(rawName + ".amount", itemStack.getAmount());
        set(rawName + ".lore", itemStack.getItemMeta().getLore());
        set(rawName + ".page", setting.getPage());
        set(rawName + ".slot", setting.getSlot());
        save();
    }
}

package me.limbo56.playersettings.example;

import me.limbo56.playersettings.api.PlayerSettingsAPI;
import me.limbo56.playersettings.api.Setting;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerSettingsExample extends JavaPlugin {
    @Override
    public void onEnable() {
        ItemStack itemStack = new ItemStack(Material.AIR);
        Setting exampleSetting = new Setting("example_setting", itemStack);

        // Register the setting
        getSettingsApi().registerSetting(exampleSetting);
    }

    private PlayerSettingsAPI getSettingsApi() {
        return (PlayerSettingsAPI) Bukkit.getPluginManager().getPlugin("PlayerSettings");
    }
}

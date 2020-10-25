package me.limbo56.playersettings.configuration;

import lombok.Getter;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.utils.ColorUtils;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

@Getter
public abstract class YmlConfiguration extends YamlConfiguration {

    private final PlayerSettings plugin;
    private final String name;
    private final File file;

    public YmlConfiguration(PlayerSettings plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.file = new File(plugin.getDataFolder(), name + ".yml");
        this.createFile();
    }

    protected abstract void addDefaults();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createFile() {
        try {
            // Create configuration file
            plugin.getDataFolder().mkdirs();
            file.createNewFile();

            // Load configuration file
            load(file);
            addDefaults();
            options().copyDefaults(true);
            save();
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            plugin.getLogger().severe("File " + name + " couldn't be created");
        }
    }

    public void save() {
        try {
            super.save(file);
        } catch (IOException exception) {
            exception.printStackTrace();
            plugin.debug("File " + name + " couldn't be saved");
        }
    }

    @Override
    public String getString(String path) {
        return ColorUtils.translateString(super.getString(path));
    }
}

package me.limbo56.playersettings.configuration;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.utils.PluginLogger;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

public abstract class YmlConfiguration {
    private static final File DATA_FOLDER = PlayerSettings.getPlugin().getDataFolder();
    private String name;
    private File file;
    private YamlConfiguration yamlConfiguration;

    public YmlConfiguration(String name) {
        this.name = name;
        this.file = new File(DATA_FOLDER, name + ".yml");
        this.yamlConfiguration = new YamlConfiguration();
        this.createFile();
    }

    protected abstract void addDefaults();

    @SuppressWarnings("ResultOfMethodCallIgnored")
    public void createFile() {
        try {
            // Create configuration file
            DATA_FOLDER.mkdirs();
            file.createNewFile();

            // Load configuration file
            yamlConfiguration.load(file);
            yamlConfiguration.options().copyDefaults(true);
            addDefaults();
            save();
            PluginLogger.finest("File " + name + " created successfully");
        } catch (IOException | InvalidConfigurationException exception) {
            exception.printStackTrace();
            PluginLogger.severe("File " + name + " couldn't be created");
        }
    }

    public void save() {
        try {
            yamlConfiguration.save(file);
            PluginLogger.finest("File " + name + " saved successfully");
        } catch (IOException exception) {
            exception.printStackTrace();
            PluginLogger.finest("File " + name + " couldn't be saved");
        }
    }

    public void addDefault(String key, Object value) {
        yamlConfiguration.addDefault(key, value);
    }

    public void set(String key, Object value) {
        yamlConfiguration.set(key, value);
    }

    public String getName() {
        return name;
    }

    public YamlConfiguration getConfiguration() {
        return yamlConfiguration;
    }

    public boolean contains(String path) {
        return yamlConfiguration.contains(path);
    }

    public boolean getBoolean(String path) {
        return yamlConfiguration.getBoolean(path);
    }

    public int getInt(String path) {
        return yamlConfiguration.getInt(path);
    }

    public double getDouble(String path) {
        return yamlConfiguration.getDouble(path);
    }

    public List<String> getStringList(String path) {
        return yamlConfiguration.getStringList(path);
    }

    public String getString(String path) {
        return yamlConfiguration.getString(path);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) yamlConfiguration.get(path);
    }
}

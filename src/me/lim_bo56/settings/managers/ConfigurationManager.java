package me.lim_bo56.settings.managers;

import me.lim_bo56.settings.Core;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.util.List;

/**
 * Credits to @iSach.
 */
public class ConfigurationManager {

    private static ConfigurationManager messages = new ConfigurationManager("messages");
    private static ConfigurationManager aDefault = new ConfigurationManager("default");
    private static ConfigurationManager menu = new ConfigurationManager("menu");

    public FileConfiguration fileConfiguration;
    private File file;

    private ConfigurationManager(String fileName) {

        file = new File(Core.getInstance().getDataFolder(), fileName + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);
    }

    private ConfigurationManager() {
        file = new File(Core.getInstance().getDataFolder(), "config.yml");
        fileConfiguration = Core.getInstance().getConfig();
    }

    public static ConfigurationManager getMessages() {
        return messages;
    }

    public static ConfigurationManager getDefault() {
        return aDefault;
    }

    public static ConfigurationManager getMenu() {
        return menu;
    }


    public void reload() {
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void set(String path, Object value) {
        fileConfiguration.set(path, value);
        try {
            fileConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean getBoolean(String path) {
        return (boolean) get(path);
    }

    public int getInt(String path) {
        return (int) get(path);
    }

    public double getDouble(String path) {
        return (double) get(path);
    }

    public void addDefault(String path, Object value) {
        if (!fileConfiguration.contains(path))
            set(path, value);
    }

    public ConfigurationSection createConfigurationSection(String path) {
        ConfigurationSection cs = fileConfiguration.createSection(path);
        try {
            fileConfiguration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cs;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) fileConfiguration.get(path);
    }

    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }

    public String getString(String path) {
        return fileConfiguration.getString(path);
    }

    public boolean contains(String path) {
        return fileConfiguration.contains(path);
    }


}
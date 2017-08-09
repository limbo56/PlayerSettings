package me.limbo56.settings.managers;

import me.limbo56.settings.PlayerSettings;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Created by lim_bo56
 * On 9/3/2016
 * At 7:26 PM
 */
public class ConfigurationManager {

    private static ConfigurationManager messages = new ConfigurationManager("messages");
    private static ConfigurationManager aDefault = new ConfigurationManager("default");
    private static ConfigurationManager menu = new ConfigurationManager("menu");
    private static ConfigurationManager config = new ConfigurationManager();

    private FileConfiguration fileConfiguration;

    private File file;

    private PlayerSettings plugin = PlayerSettings.getInstance();

    private ConfigurationManager(String name) {

        if (PlayerSettings.getInstance().getDataFolder().exists()) {
            try {
                plugin.getDataFolder().createNewFile();
            } catch (IOException exception) {
                System.out.println("Couldn't create the data folder for Player Settings!");
            }
        }

        file = new File(plugin.getDataFolder(), name + ".yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                System.out.println("Couldn't create " + name + ".yml!");
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);

    }

    private ConfigurationManager() {

        if (PlayerSettings.getInstance().getDataFolder().exists()) {
            try {
                plugin.getDataFolder().createNewFile();
            } catch (IOException exception) {
                System.out.println("Couldn't create the data folder for Player Settings!");
            }
        }

        file = new File(plugin.getDataFolder(), "config.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException exception) {
                System.out.println("Couldn't create config.yml!");
            }
        }

        fileConfiguration = YamlConfiguration.loadConfiguration(file);

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

    public static ConfigurationManager getConfig() {
        return config;
    }

    public void saveConfig() {
        try {
            fileConfiguration.save(file);
        } catch (IOException exception) {
            System.out.println("Couldn't save " + fileConfiguration.getName() + ".yml!");
        }
    }

    public void reloadConfig() {
        try {
            fileConfiguration = YamlConfiguration.loadConfiguration(file);
        } catch (Exception exception) {
            System.out.println("Couldn't reload " + fileConfiguration.getName() + ".yml!");
        }
    }

    public void set(String path, Object value) {
        fileConfiguration.set(path, value);
        try {
            fileConfiguration.save(file);
        } catch (IOException exception) {
            System.out.println("Couldn't save " + fileConfiguration.getName() + ".yml!");
        }
    }

    public void addDefault(String path, Object value) {
        if (!contains(path))
            set(path, value);
    }

    public boolean getBoolean(String path) {
        return fileConfiguration.getBoolean(path);
    }

    public int getInt(String path) {
        return fileConfiguration.getInt(path);
    }

    public double getDouble(String path) {
        return fileConfiguration.getDouble(path);
    }

    public List<String> getStringList(String path) {
        return fileConfiguration.getStringList(path);
    }

    public String getString(String path) {
        return fileConfiguration.getString(path);
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String path) {
        return (T) fileConfiguration.get(path);
    }

    public boolean contains(String path) {
        return fileConfiguration.contains(path);
    }
}
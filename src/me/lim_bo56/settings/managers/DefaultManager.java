package me.lim_bo56.settings.managers;

/**
 * Created by lim_bo56
 * On 8/14/2016
 * At 11:24 AM
 */
public class DefaultManager {

    private static ConfigurationManager configurationManager;

    public DefaultManager() {
        configurationManager = ConfigurationManager.getDefault();
        loadData();
    }

    public static boolean getData(String path) {
        return configurationManager.getBoolean(path);
    }

    private void addData(String path, boolean enable) {
        configurationManager.addDefault(path, enable);
    }

    private void loadData() {
        addData("Default.Visibility", true);
        addData("Default.Stacker", false);
        addData("Default.Chat", true);
        addData("Default.Vanish", false);
        addData("Default.Fly", false);
        addData("Default.Speed", false);
        addData("Default.Jump", false);
    }

}

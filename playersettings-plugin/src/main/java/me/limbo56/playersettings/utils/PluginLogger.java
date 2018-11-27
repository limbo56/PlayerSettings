package me.limbo56.playersettings.utils;

import me.limbo56.playersettings.PlayerSettings;

import java.util.logging.Level;

public class PluginLogger {
    private static PlayerSettings plugin;
    private static boolean debug;

    static {
        PluginLogger.plugin = PlayerSettings.getPlugin();
        PluginLogger.debug = plugin.getConfiguration().getBoolean("debug");
    }

    public static void severe(String message) {
        log(Level.SEVERE, message);
    }

    public static void warning(String message) {
        log(Level.WARNING, message);
    }

    public static void info(String message) {
        log(Level.INFO, message);
    }

    public static void fine(String message) {
        log(Level.FINE, message);
    }

    public static void finer(String message) {
        log(Level.FINER, message);
    }

    public static void finest(String message) {
        log(Level.FINEST, message);
    }

    private static void log(Level level, String message) {
        if (debug) {
            plugin.getLogger().log(level, message);
        }
    }
}

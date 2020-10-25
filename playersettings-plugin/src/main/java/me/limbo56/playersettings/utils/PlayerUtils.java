package me.limbo56.playersettings.utils;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.settings.SimpleSettingWatcher;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerUtils {

    private static final PlayerSettings PLUGIN = PlayerSettings.getPlugin();

    public static void loadPlayer(Player player) {
        SettingWatcher settingWatcher = new SimpleSettingWatcher(
                player,
                PLUGIN.getSettingStore().getStored(),
                PLUGIN.getSettingStore().getCallbacks().getStored()
        );
        SPlayer sPlayer = new SPlayer(player.getUniqueId(), settingWatcher);

        // Load player
        sPlayer.loadPlayer();
        PLUGIN.getSPlayerStore().addToStore(player.getUniqueId(), sPlayer);
    }

    public static void sendMessage(Player player, String message) {
        player.sendMessage(ColorUtils.translateString(message));
    }

    public static void sendConfigMessage(Player player, String path) {
        String messages = PLUGIN.getConfiguration("messages").getString(path);

        if (messages == null || messages.trim().length() == 0) {
            return;
        }

        player.sendMessage(messages);
    }

    public static void sendConfigMessage(Player player, String path, Function<String, String> modifier) {
        String message = PLUGIN.getConfiguration("messages").getString(path);
        message = modifier.apply(message);
        player.sendMessage(message);
    }

    public static boolean isAllowedWorld(String name) {
        return PlayerSettings.getPlugin()
                .getConfiguration("menu")
                .getStringList("Worlds-Allowed")
                .contains(name);
    }

    public static List<SPlayer> filterPlayers(Predicate<SPlayer> filter) {
        return PlayerSettings.getPlugin().getSPlayerStore().getStored().values().stream()
                .filter(filter)
                .collect(Collectors.toList());
    }
}

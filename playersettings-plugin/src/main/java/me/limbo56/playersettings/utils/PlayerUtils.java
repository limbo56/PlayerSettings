package me.limbo56.playersettings.utils;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.SettingWatcher;
import me.limbo56.playersettings.settings.SPlayer;
import me.limbo56.playersettings.settings.SimpleSettingWatcher;
import org.bukkit.entity.Player;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PlayerUtils {
    private static final PlayerSettings PLUGIN = PlayerSettings.getPlugin();

    public static void loadPlayer(Player player) {
        SettingWatcher settingWatcher = new SimpleSettingWatcher(
                player,
                PLUGIN.getSettingsRegistry().getStored(),
                PLUGIN.getSettingsRegistry().getCallbacks().getStored()
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
        player.sendMessage(PLUGIN.getConfiguration("messages").getString(path));
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

    public static int getPermissionInt(Player player, String permission) {
        permission = permission.toLowerCase() + ".";
        if (player.hasPermission(permission + "*"))
            return Integer.MAX_VALUE;
        int computedLevel = 1;
        for (PermissionAttachmentInfo pai : player.getEffectivePermissions()) {
            String playerPerm = pai.getPermission().toLowerCase();
            if (pai.getValue() && playerPerm.startsWith(permission)) {
                try {
                    String levelString = playerPerm.substring(permission.length());
                    int level = Integer.parseInt(levelString); // throws NumberFormatException
                    if (level < 0)
                        throw new NumberFormatException("Level must be positive");
                    if (level > computedLevel)
                        computedLevel = level;
                } catch (NumberFormatException ex) {
                    PlayerSettings.getPlugin().getLogger().warning("Invalid permission level for " + player.getName() + ": " + playerPerm);
                    PlayerSettings.getPlugin().getLogger().warning(ex.getLocalizedMessage());
                }
            }
        }
        return computedLevel;
    }
}

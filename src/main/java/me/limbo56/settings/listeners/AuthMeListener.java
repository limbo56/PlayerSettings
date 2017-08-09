package me.limbo56.settings.listeners;

import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.PlayerUtils;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMeListener implements Listener {

    @EventHandler
    public void onUserLogin(LoginEvent event) {
        // AuthMe has a Bug where session logins are authenticated twice.
        // This is a workaround for that. TODO: Remove when AuthMe fixes it.
        if (!PlayerSettings.getPlayerManager().getPlayerList().containsKey(event.getPlayer().getUniqueId()))
            PlayerUtils.loadSettings(event.getPlayer());
    }

    @EventHandler
    public void onUserLogout(LogoutEvent event) {
        Player player = event.getPlayer();

        if (player.getGameMode() == GameMode.CREATIVE || player.getGameMode() == GameMode.SPECTATOR) {
            CustomPlayer cPlayer = PlayerUtils.getOrCreateCustomPlayer(player);

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.Fly.Enabled"))
                cPlayer.setFly(ConfigurationManager.getDefault().getBoolean("Default.Fly"));

            if (ConfigurationManager.getMenu().getBoolean("Menu.Items.DoubleJump.Enabled"))
                cPlayer.setDoubleJump(ConfigurationManager.getDefault().getBoolean("Default.DoubleJump"));
        }

        PlayerUtils.saveSettings(event.getPlayer());
    }
}

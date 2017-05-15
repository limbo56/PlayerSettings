package me.limbo56.settings.listeners;

import fr.xephi.authme.events.LoginEvent;
import fr.xephi.authme.events.LogoutEvent;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Utilities;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class AuthMeListener implements Listener {

    @EventHandler
    public void onUserLogin(LoginEvent event) {
        // AuthMe has a Bug where session logins are authenticated twice.
        // This is a workaround for that. TODO: Remove when AuthMe fixes it.
        if (!CustomPlayer.getPlayerList().containsKey(event.getPlayer()))
            Utilities.loadSettings(event.getPlayer());
    }

    @EventHandler
    public void onUserLogout(LogoutEvent event) {
        Utilities.saveSettings(event.getPlayer());
    }

}

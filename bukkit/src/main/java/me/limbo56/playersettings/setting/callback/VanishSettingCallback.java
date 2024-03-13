package me.limbo56.playersettings.setting.callback;

import java.util.Collection;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.Players;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class VanishSettingCallback implements SettingCallback {
  private final UserManager userManager;

  public VanishSettingCallback() {
    userManager = PlayerSettings.getInstance().getUserManager();
  }

  @Override
  public void notifyChange(Player player, Setting setting, int value) {
    if (value <= 0) {
      this.clear(player);
      return;
    }

    player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
    getObservers().forEach(observer -> observer.hidePlayer(player));
  }

  @Override
  public void clear(Player player) {
    player.removePotionEffect(PotionEffectType.INVISIBILITY);
    getObservers().forEach(observer -> observer.showPlayer(player));
  }

  private Collection<Player> getObservers() {
    return Players.filterOnlineUsers(userManager.getUsers());
  }
}

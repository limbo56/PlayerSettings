package me.limbo56.playersettings.setting.callback;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class SpeedSettingCallback implements SettingCallback {
  @Override
  public void notifyChange(Player player, Setting setting, int value) {
    if (value <= 0) {
      this.clear(player);
      return;
    }

    player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
    player.setWalkSpeed(Math.min(1, 0.2F * value));
  }

  @Override
  public void clear(Player player) {
    player.removePotionEffect(PotionEffectType.SPEED);
    player.setWalkSpeed(0.2F);
  }
}

package me.limbo56.playersettings.setting.callback;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class JumpSettingCallback implements SettingCallback {
  @Override
  public void notifyChange(Player player, Setting setting, int value) {
    if (value <= 0) {
      this.clear(player);
      return;
    }

    int amplifier = value * 3;
    clear(player);
    player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, amplifier));
  }

  @Override
  public void clear(Player player) {
    player.removePotionEffect(PotionEffectType.JUMP);
  }
}

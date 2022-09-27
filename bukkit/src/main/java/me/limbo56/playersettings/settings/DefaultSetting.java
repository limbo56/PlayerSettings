package me.limbo56.playersettings.settings;

import com.cryptomorin.xseries.XMaterial;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.setting.ImmutableSetting;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingCallback;
import me.limbo56.playersettings.listeners.ChatSettingListener;
import me.limbo56.playersettings.listeners.FlySettingListener;
import me.limbo56.playersettings.listeners.StackerSettingListener;
import me.limbo56.playersettings.listeners.VanishSettingListener;
import me.limbo56.playersettings.util.ItemBuilder;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public enum DefaultSetting {
  SPEED_SETTING(
      ImmutableSetting.builder()
          .name("speed-setting")
          .item(ImmutableMenuItem.of(Constants.Speed.ITEM, 1, 1))
          .defaultValue(0)
          .maxValue(5)
          .addCallback(Constants.Speed.CALLBACK)
          .build()),
  JUMP_SETTING(
      ImmutableSetting.builder()
          .name("jump-setting")
          .item(ImmutableMenuItem.of(Constants.Jump.ITEM, 1, 3))
          .defaultValue(0)
          .maxValue(10)
          .addCallback(Constants.Jump.CALLBACK)
          .build()),
  STACKER_SETTING(
      ImmutableSetting.builder()
          .name("stacker-setting")
          .item(ImmutableMenuItem.of(Constants.Stacker.ITEM, 1, 5))
          .defaultValue(0)
          .maxValue(1)
          .addListener(new StackerSettingListener())
          .build()),
  FLY_SETTING(
      ImmutableSetting.builder()
          .name("fly-setting")
          .item(ImmutableMenuItem.of(Constants.Fly.ITEM, 1, 7))
          .defaultValue(0)
          .maxValue(10)
          .addCallback(Constants.Fly.CALLBACK)
          .addListener(new FlySettingListener())
          .build()),
  VANISH_SETTING(
      ImmutableSetting.builder()
          .name("vanish-setting")
          .item(ImmutableMenuItem.of(Constants.Vanish.ITEM, 1, 29))
          .defaultValue(0)
          .maxValue(1)
          .addCallback(Constants.Vanish.CALLBACK)
          .addListener(new VanishSettingListener())
          .build()),
  CHAT_SETTING(
      ImmutableSetting.builder()
          .name("chat-setting")
          .item(ImmutableMenuItem.of(Constants.Chat.ITEM, 1, 31))
          .defaultValue(1)
          .maxValue(1)
          .addListener(new ChatSettingListener())
          .build()),
  VISIBILITY_SETTING(
      ImmutableSetting.builder()
          .name("visibility-setting")
          .item(ImmutableMenuItem.of(Constants.Visibility.ITEM, 1, 33))
          .defaultValue(1)
          .maxValue(1)
          .addCallback(Constants.Visibility.CALLBACK)
          .build());

  private final Setting setting;

  DefaultSetting(Setting setting) {
    this.setting = setting;
  }

  public static Collection<Setting> getSettings() {
    return Arrays.stream(DefaultSetting.values())
        .map(DefaultSetting::getSetting)
        .collect(Collectors.toList());
  }

  public Setting getSetting() {
    return setting;
  }

  private static class Constants {
    private static class Speed {
      private static final SettingCallback CALLBACK =
          (setting, player, value) -> {
            if (value == 0) {
              player.removePotionEffect(PotionEffectType.SPEED);
              player.setWalkSpeed(0.2F);
              return;
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0));
            player.setWalkSpeed(0.2F * Math.max(1.4F, value));
          };
      private static final ItemStack ITEM =
          ItemBuilder.builder()
              .material(XMaterial.SUGAR.parseMaterial())
              .name("&aSpeed Boost")
              .lore("&7Boost your speed!")
              .lore("")
              .lore("&f&lLeft click &7to cycle next")
              .lore("&f&lRight click &7to cycle previous")
              .build();
    }

    private static class Jump {
      private static final SettingCallback CALLBACK =
          (setting, player, value) -> {
            if (value == 0) {
              player.removePotionEffect(PotionEffectType.JUMP);
              return;
            }
            int amplifier = value * 3;
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, amplifier));
          };
      private static final ItemStack ITEM =
          ItemBuilder.builder()
              .material(XMaterial.SLIME_BLOCK.parseMaterial())
              .name("&aJump Boost")
              .lore("&7Boost your jump height!")
              .lore("")
              .lore("&f&lLeft click &7to cycle next")
              .lore("&f&lRight click &7to cycle previous")
              .build();
    }

    private static class Stacker {
      private static final ItemStack ITEM =
          ItemBuilder.builder()
              .material(XMaterial.SADDLE.parseMaterial())
              .name("&aStacker")
              .lore("&7Stack players on top of your head!")
              .lore("")
              .lore("&f&lClick &7to toggle")
              .build();
    }

    private static class Fly {
      private static final SettingCallback CALLBACK =
          (Setting setting, Player player, int value) -> {
            GameMode gm = player.getGameMode();
            if (gm != GameMode.CREATIVE && gm != GameMode.SPECTATOR) {
              player.setAllowFlight(value > 0);
            }

            float flySpeed = Math.max(1, value) * 0.1F;
            player.setFlySpeed(flySpeed);
            player.setFlying(value > 0);
          };
      private static final ItemStack ITEM =
          ItemBuilder.builder()
              .material(XMaterial.FEATHER.parseMaterial())
              .name("&aFly")
              .lore("&7Toggle your ability to fly and boost your flight speed!")
              .lore("")
              .lore("&f&lLeft click &7to cycle next")
              .lore("&f&lRight click &7to cycle previous")
              .build();
    }

    private static class Vanish {
      private static final SettingCallback CALLBACK =
          (setting, player, value) -> {
            if (value == 0) {
              player.removePotionEffect(PotionEffectType.INVISIBILITY);
              Bukkit.getOnlinePlayers().forEach(players -> players.showPlayer(player));
              return;
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1));
            Bukkit.getOnlinePlayers().forEach(players -> players.hidePlayer(player));
          };
      private static final ItemStack ITEM =
          ItemBuilder.builder()
              .material(XMaterial.ENDER_PEARL.parseMaterial())
              .name("&aVanish")
              .lore("&7Make yourself invisible to other players!")
              .lore("")
              .lore("&f&lClick &7to toggle")
              .build();
    }

    private static class Chat {
      private static final ItemStack ITEM =
          ItemBuilder.builder()
              .material(XMaterial.PAPER.parseMaterial())
              .name("&aChat")
              .lore("&7Toggle your ability to see or send chat messages!")
              .lore("")
              .lore("&f&lClick &7to toggle")
              .build();
    }

    private static class Visibility {
      private static final SettingCallback CALLBACK =
          (setting, player, value) -> {
            if (value == 0) {
              Bukkit.getOnlinePlayers().forEach(player::hidePlayer);
              return;
            }
            Bukkit.getOnlinePlayers().forEach(player::showPlayer);
          };
      private static final ItemStack ITEM =
          ItemBuilder.builder()
              .material(XMaterial.ENDER_EYE.parseMaterial())
              .name("&aVisibility")
              .lore("&7Shows and hides other players from your sight!")
              .lore("")
              .lore("&f&lClick &7to toggle")
              .build();
    }
  }
}

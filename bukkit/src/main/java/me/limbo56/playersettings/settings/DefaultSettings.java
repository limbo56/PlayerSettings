package me.limbo56.playersettings.settings;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.setting.ImmutableSetting;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingCallback;
import me.limbo56.playersettings.listeners.*;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.ItemBuilder;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

public enum DefaultSettings {
  SPEED_SETTING(
      "speed-setting", Speed.ITEM, 0, 5, new String[] {"join", "respawn"}, Speed.CALLBACK),
  JUMP_SETTING("jump-setting", Jump.ITEM, 0, 10, new String[] {"join", "respawn"}, Jump.CALLBACK),
  STACKER_SETTING("stacker-setting", Stacker.ITEM, 0, 1, new StackerSettingListener()),
  FLY_SETTING("fly-setting", Fly.ITEM, 0, 10, Fly.CALLBACK, new FlySettingListener()),
  VANISH_SETTING("vanish-setting", Vanish.ITEM, 0, 1, Vanish.CALLBACK, new VanishSettingListener()),
  CHAT_SETTING("chat-setting", Chat.ITEM, 1, 1, new ChatSettingListener()),
  VISIBILITY_SETTING(
      "visibility-setting",
      Visibility.ITEM,
      1,
      1,
      Visibility.CALLBACK,
      new VisibilitySettingListener());
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final Setting setting;

  DefaultSettings(
      String settingName,
      MenuItem menuItem,
      int defaultValue,
      int maxValue,
      String[] triggers,
      Collection<SettingCallback> callbacks,
      Collection<Listener> listeners) {
    this.setting =
        ImmutableSetting.builder()
            .name(settingName)
            .item(menuItem)
            .defaultValue(defaultValue)
            .maxValue(maxValue)
            .triggers(triggers)
            .callbacks(callbacks)
            .listeners(listeners)
            .build();
  }

  DefaultSettings(
      String settingName,
      MenuItem menuItem,
      int defaultValue,
      int maxValue,
      SettingCallback callback,
      Listener listener) {
    this(
        settingName,
        menuItem,
        defaultValue,
        maxValue,
        new String[] {"join"},
        singletonList(callback),
        singletonList(listener));
  }

  DefaultSettings(
      String settingName,
      MenuItem menuItem,
      int defaultValue,
      int maxValue,
      String[] triggers,
      SettingCallback... callbacks) {
    this(
        settingName,
        menuItem,
        defaultValue,
        maxValue,
        triggers,
        Arrays.asList(callbacks),
        Collections.emptyList());
  }

  DefaultSettings(
      String settingName,
      MenuItem menuItem,
      int defaultValue,
      int maxValue,
      Listener... listeners) {
    this(
        settingName,
        menuItem,
        defaultValue,
        maxValue,
        new String[] {"join"},
        Collections.emptyList(),
        Arrays.asList(listeners));
  }

  public static Collection<Setting> getSettings() {
    return Arrays.stream(DefaultSettings.values())
        .map(DefaultSettings::getSetting)
        .collect(Collectors.toList());
  }

  private static MenuItem createSettingItem(
      int page, int slot, Material material, String name, String... lore) {
    ItemStack itemstack =
        ItemBuilder.builder().material(material).name(name).lore(Arrays.asList(lore)).build();
    return ImmutableMenuItem.builder().itemStack(itemstack).page(page).slot(slot).build();
  }

  public String getName() {
    return setting.getName();
  }

  public Setting getSetting() {
    return setting;
  }

  private static class Speed {
    private static final MenuItem ITEM =
        createSettingItem(
            1,
            1,
            XMaterial.SUGAR.parseMaterial(),
            "&aSpeed Boost",
            "&7Boost your speed!",
            "",
            "&f&lLeft click &7to cycle next",
            "&f&lRight click &7to cycle previous");
    private static final SettingCallback CALLBACK =
        new SettingCallback() {
          @Override
          public void notifyChange(Setting setting, Player player, int value) {
            if (value <= 0) {
              this.clear(player);
              return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, 99999, 0));
            player.setWalkSpeed(0.2F * Math.max(1.4F, value));
          }

          @Override
          public void clear(Player player) {
            player.removePotionEffect(PotionEffectType.SPEED);
            player.setWalkSpeed(0.2F);
          }
        };
  }

  private static class Jump {
    private static final MenuItem ITEM =
        createSettingItem(
            1,
            3,
            XMaterial.SLIME_BLOCK.parseMaterial(),
            "&aJump Boost",
            "&7Boost your jump height!",
            "",
            "&f&lLeft click &7to cycle next",
            "&f&lRight click &7to cycle previous");
    private static final SettingCallback CALLBACK =
        new SettingCallback() {
          @Override
          public void notifyChange(Setting setting, Player player, int value) {
            if (value <= 0) {
              this.clear(player);
              return;
            }

            int amplifier = value * 3;
            PotionEffect jumpEffect = player.getPotionEffect(PotionEffectType.JUMP);
            if (jumpEffect != null && jumpEffect.getAmplifier() >= amplifier) {
              clear(player);
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.JUMP, 99999, amplifier));
          }

          @Override
          public void clear(Player player) {
            player.removePotionEffect(PotionEffectType.JUMP);
          }
        };
  }

  private static class Stacker {
    private static final MenuItem ITEM =
        createSettingItem(
            1,
            5,
            XMaterial.SADDLE.parseMaterial(),
            "&aStacker",
            "&7Stack players on top of your head!",
            "",
            "&f&lClick &7to toggle");
  }

  private static class Fly {
    private static final MenuItem ITEM =
        createSettingItem(
            1,
            7,
            XMaterial.FEATHER.parseMaterial(),
            "&aFly",
            "&7Toggle your ability to fly and boost your flight speed!",
            "",
            "&f&lLeft click &7to cycle next",
            "&f&lRight click &7to cycle previous");
    private static final SettingCallback CALLBACK =
        new SettingCallback() {
          @Override
          public void notifyChange(Setting setting, Player player, int value) {
            if (!hasFlightGameMode(player)) {
              player.setAllowFlight(value > 0);
            }

            float flightSpeed = Math.max(1, value) * 0.1F;
            player.setFlySpeed(flightSpeed);
          }

          @Override
          public void clear(Player player) {
            player.setAllowFlight(hasFlightGameMode(player));
            player.setFlySpeed(0.1F);
          }

          private boolean hasFlightGameMode(Player player) {
            GameMode gameMode = player.getGameMode();
            return gameMode == GameMode.CREATIVE || gameMode == GameMode.SPECTATOR;
          }
        };
  }

  private static class Vanish {
    private static final MenuItem ITEM =
        createSettingItem(
            1,
            29,
            XMaterial.ENDER_PEARL.parseMaterial(),
            "&aVanish",
            "&7Make yourself invisible to other players!",
            "",
            "&f&lClick &7to toggle");
    private static final SettingCallback CALLBACK =
        new SettingCallback() {
          @Override
          public void notifyChange(Setting setting, Player player, int value) {
            if (value <= 0) {
              this.clear(player);
              return;
            }
            player.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 99999, 1));
            getPlayersWithVisiblityEnabled().forEach(user -> user.getPlayer().hidePlayer(player));
          }

          @Override
          public void clear(Player player) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            getPlayersWithVisiblityEnabled().forEach(user -> user.getPlayer().showPlayer(player));
          }

          private Collection<SettingUser> getPlayersWithVisiblityEnabled() {
            String name = VISIBILITY_SETTING.getName();
            return PLUGIN.getSettingsManager().isSettingLoaded(name)
                ? PLUGIN.getUserManager().getUsersWithSettingValue(name, true)
                : PLUGIN.getUserManager().getUsers();
          }
        };
  }

  private static class Chat {
    private static final MenuItem ITEM =
        createSettingItem(
            1,
            31,
            XMaterial.PAPER.parseMaterial(),
            "&aChat",
            "&7Toggle your ability to see or send chat messages!",
            "",
            "&f&lClick &7to toggle");
  }

  private static class Visibility {
    private static final MenuItem ITEM =
        createSettingItem(
            1,
            33,
            XMaterial.ENDER_EYE.parseMaterial(),
            "&aVisibility",
            "&7Shows and hides other players from your sight!",
            "",
            "&f&lClick &7to toggle");
    private static final SettingCallback CALLBACK =
        new SettingCallback() {
          @Override
          public void notifyChange(Setting setting, Player player, int value) {
            if (value <= 0) {
              getVisiblePlayers().forEach(user -> player.hidePlayer(user.getPlayer()));
              return;
            }
            this.clear(player);
          }

          @Override
          public void clear(Player player) {
            getVisiblePlayers().forEach(user -> player.showPlayer(user.getPlayer()));
          }

          @NotNull
          private Collection<SettingUser> getVisiblePlayers() {
            String name = VANISH_SETTING.getName();
            return PLUGIN.getSettingsManager().isSettingLoaded(name)
                ? PLUGIN.getUserManager().getUsersWithSettingValue(name, false)
                : PLUGIN.getUserManager().getUsers();
          }
        };
  }
}

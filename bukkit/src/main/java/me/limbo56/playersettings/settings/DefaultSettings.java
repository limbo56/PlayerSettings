package me.limbo56.playersettings.settings;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;
import com.google.common.collect.Multimap;
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
import java.util.stream.IntStream;

import static java.util.Collections.singletonList;

public enum DefaultSettings {
  SPEED_SETTING(
      "speed-setting",
      "&6Speed Boost",
      Speed.ITEM,
      0,
      5,
      new String[] {"join", "respawn"},
      Constants.DEFAULT_VALUE_ALIASES,
      Speed.CALLBACK),
  JUMP_SETTING(
      "jump-setting",
      "&6Jump Boost",
      Jump.ITEM,
      0,
      10,
      new String[] {"join", "respawn"},
      Constants.DEFAULT_VALUE_ALIASES,
      Jump.CALLBACK),
  STACKER_SETTING(
      "stacker-setting",
      "&6Stacker",
      Stacker.ITEM,
      0,
      1,
      ArrayListMultimap.create(),
      new StackerSettingListener()),
  FLY_SETTING(
      "fly-setting",
      "&6Fly",
      Fly.ITEM,
      0,
      10,
      Fly.CALLBACK,
      new FlySettingListener(),
      Constants.DEFAULT_VALUE_ALIASES),
  VANISH_SETTING(
      "vanish-setting",
      "&6Vanish",
      Vanish.ITEM,
      0,
      1,
      Vanish.CALLBACK,
      new VanishSettingListener(),
      ArrayListMultimap.create()),
  CHAT_SETTING(
      "chat-setting",
      "&6Vanish",
      Chat.ITEM,
      1,
      1,
      ArrayListMultimap.create(),
      new ChatSettingListener()),
  VISIBILITY_SETTING(
      "visibility-setting",
      "&6Visibility",
      Visibility.ITEM,
      1,
      1,
      Visibility.CALLBACK,
      new VisibilitySettingListener(),
      ArrayListMultimap.create());
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final Setting setting;

  DefaultSettings(
      String settingName,
      String displayName,
      MenuItem menuItem,
      int defaultValue,
      int maxValue,
      String[] triggers,
      Collection<SettingCallback> callbacks,
      Collection<Listener> listeners,
      ListMultimap<String, Integer> valueAliases) {
    this.setting =
        ImmutableSetting.builder()
            .name(settingName)
            .displayName(displayName)
            .item(menuItem)
            .defaultValue(defaultValue)
            .maxValue(maxValue)
            .triggers(triggers)
            .callbacks(callbacks)
            .listeners(listeners)
            .valueAliases(valueAliases)
            .build();
  }

  DefaultSettings(
      String settingName,
      String displayName,
      MenuItem menuItem,
      int defaultValue,
      int maxValue,
      SettingCallback callback,
      Listener listener,
      ListMultimap<String, Integer> valueAliases) {
    this(
        settingName,
        displayName,
        menuItem,
        defaultValue,
        maxValue,
        new String[] {"join"},
        singletonList(callback),
        singletonList(listener),
        valueAliases);
  }

  DefaultSettings(
      String settingName,
      String displayName,
      MenuItem menuItem,
      int defaultValue,
      int maxValue,
      String[] triggers,
      ListMultimap<String, Integer> valueAliases,
      SettingCallback... callbacks) {
    this(
        settingName,
        displayName,
        menuItem,
        defaultValue,
        maxValue,
        triggers,
        Arrays.asList(callbacks),
        Collections.emptyList(),
        valueAliases);
  }

  DefaultSettings(
      String settingName,
      String displayName,
      MenuItem menuItem,
      int defaultValue,
      int maxValue,
      ListMultimap<String, Integer> valueAliases,
      Listener... listeners) {
    this(
        settingName,
        displayName,
        menuItem,
        defaultValue,
        maxValue,
        new String[] {"join"},
        Collections.emptyList(),
        Arrays.asList(listeners),
        valueAliases);
  }

  public static Collection<Setting> getSettings() {
    return Arrays.stream(DefaultSettings.values())
        .map(DefaultSettings::getSetting)
        .collect(Collectors.toList());
  }

  public String getName() {
    return setting.getName();
  }

  public Setting getSetting() {
    return setting;
  }

  public static class Constants {
    public static final ArrayListMultimap<String, Integer> DEFAULT_VALUE_ALIASES =
        IntStream.range(2, 11)
            .boxed()
            .collect(ArrayListMultimap::create, (m, i) -> m.put("x" + i, i), Multimap::putAll);

    static {
      DEFAULT_VALUE_ALIASES.put("off", -1);
      DEFAULT_VALUE_ALIASES.put("on", 1);
    }

    private static MenuItem createSettingItem(
        int page, int slot, Material material, String name, String... lore) {
      ItemStack itemstack =
          ItemBuilder.builder().material(material).name(name).lore(Arrays.asList(lore)).build();
      return ImmutableMenuItem.builder().itemStack(itemstack).page(page).slot(slot).build();
    }

    private static Collection<SettingUser> filterOnline(Collection<SettingUser> users) {
      return users.stream()
          .filter(user -> user.getPlayer() != null && user.getPlayer().isOnline())
          .collect(Collectors.toList());
    }
  }

  private static class Speed {
    private static final MenuItem ITEM =
        Constants.createSettingItem(
            1,
            1,
            XMaterial.SUGAR.parseMaterial(),
            "&6&lSpeed Boost &f%current%",
            "&7Boost your speed!",
            "",
            "&6Left click &7to cycle next",
            "&6Right click &7to cycle previous");
    private static final SettingCallback CALLBACK =
        new SettingCallback() {
          @Override
          public void notifyChange(Setting setting, Player player, int value) {
            if (value <= 0) {
              this.clear(player);
              return;
            }

            player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 0));
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
        Constants.createSettingItem(
            1,
            3,
            XMaterial.SLIME_BLOCK.parseMaterial(),
            "&6&lJump Boost &f%current%",
            "&7Boost your jump height!",
            "",
            "&6Left click &7to cycle next",
            "&6Right click &7to cycle previous");
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
            player.addPotionEffect(
                new PotionEffect(PotionEffectType.JUMP, Integer.MAX_VALUE, amplifier));
          }

          @Override
          public void clear(Player player) {
            player.removePotionEffect(PotionEffectType.JUMP);
          }
        };
  }

  private static class Stacker {
    private static final MenuItem ITEM =
        Constants.createSettingItem(
            1,
            5,
            XMaterial.SADDLE.parseMaterial(),
            "&6&lStacker &f%current%",
            "&7Stack players on top of your head!",
            "",
            "&6Click &7to toggle");
  }

  private static class Fly {
    private static final MenuItem ITEM =
        Constants.createSettingItem(
            1,
            7,
            XMaterial.FEATHER.parseMaterial(),
            "&6&lFly &r&f%current%",
            "&7Toggle your ability to fly and boost your flight speed!",
            "",
            "&6Left click &7to cycle next",
            "&6Right click &7to cycle previous");
    private static final SettingCallback CALLBACK =
        new SettingCallback() {
          @Override
          public void notifyChange(Setting setting, Player player, int value) {
            if (!hasFlightGameMode(player)) {
              boolean hasFlyEnabled = value > 0;
              player.setAllowFlight(hasFlyEnabled);
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
        Constants.createSettingItem(
            1,
            29,
            XMaterial.ENDER_PEARL.parseMaterial(),
            "&6&lVanish &f%current%",
            "&7Make yourself invisible to other players!",
            "",
            "&6Click &7to toggle");
    private static final SettingCallback CALLBACK =
        new SettingCallback() {
          @Override
          public void notifyChange(Setting setting, Player player, int value) {
            if (value <= 0) {
              this.clear(player);
              return;
            }
            player.addPotionEffect(
                new PotionEffect(PotionEffectType.INVISIBILITY, Integer.MAX_VALUE, 1));
            getPlayersWithVisibilityEnabled().forEach(user -> user.getPlayer().hidePlayer(player));
          }

          @Override
          public void clear(Player player) {
            player.removePotionEffect(PotionEffectType.INVISIBILITY);
            getPlayersWithVisibilityEnabled().forEach(user -> user.getPlayer().showPlayer(player));
          }

          private Collection<SettingUser> getPlayersWithVisibilityEnabled() {
            return Constants.filterOnline(
                PLUGIN
                    .getUserManager()
                    .getUsersWithSettingValue(VISIBILITY_SETTING.getName(), true));
          }
        };
  }

  private static class Chat {
    private static final MenuItem ITEM =
        Constants.createSettingItem(
            1,
            31,
            XMaterial.PAPER.parseMaterial(),
            "&6&lChat &f%current%",
            "&7Toggle your ability to see or send chat messages!",
            "",
            "&6Click &7to toggle");
  }

  private static class Visibility {

    private static final MenuItem ITEM =
        Constants.createSettingItem(
            1,
            33,
            XMaterial.ENDER_EYE.parseMaterial(),
            "&6&lVisibility %current%",
            "&7Shows and hides other players from your sight!",
            "",
            "&6Click &7to toggle");
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
            return Constants.filterOnline(
                PLUGIN.getUserManager().getUsersWithSettingValue(VANISH_SETTING.getName(), false));
          }
        };
  }
}

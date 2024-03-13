package me.limbo56.playersettings.setting;

import com.cryptomorin.xseries.XMaterial;
import com.google.common.collect.ImmutableListMultimap;
import com.google.common.collect.Lists;
import java.util.List;
import java.util.stream.IntStream;
import me.limbo56.playersettings.api.ImmutableMenuItem;
import me.limbo56.playersettings.api.ImmutableSetting;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.listener.*;
import me.limbo56.playersettings.setting.callback.*;
import me.limbo56.playersettings.util.Item;
import org.bukkit.Material;
import org.jetbrains.annotations.NotNull;

public class Settings {
  // Settings constants
  private static Setting speed;
  private static Setting jump;
  private static Setting stacker;
  private static Setting fly;
  private static Setting vanish;
  private static Setting chat;
  private static Setting visibility;

  public static void register() {
    speed = createSpeedSetting();
    jump = createJumpSetting();
    stacker = createStackerSettings();
    fly = createFlySetting();
    vanish = createVanishSetting();
    chat = createChatSetting();
    visibility = createVisibilitySetting();
  }

  public static Setting speed() {
    return speed;
  }

  public static Setting jump() {
    return jump;
  }

  public static Setting stacker() {
    return stacker;
  }

  public static Setting fly() {
    return fly;
  }

  public static Setting vanish() {
    return vanish;
  }

  public static Setting chat() {
    return chat;
  }

  public static Setting visibility() {
    return visibility;
  }

  public static Setting[] getDefaultSettings() {
    return new Setting[] {speed, jump, stacker, fly, vanish, chat, visibility};
  }

  @NotNull
  private static ImmutableSetting createSpeedSetting() {
    return defaultBuilder()
        .name("speed-setting")
        .displayName("&6Speed Boost")
        .item(
            createMenuItem(
                true,
                1,
                1,
                XMaterial.SUGAR.parseMaterial(),
                "&6&lSpeed Boost &f(%current%)",
                "&7Boost your speed!"))
        .maxValue(5)
        .triggers("join", "respawn")
        .addCallback(new SpeedSettingCallback())
        .build();
  }

  @NotNull
  private static ImmutableSetting createJumpSetting() {
    return defaultBuilder()
        .name("jump-setting")
        .displayName("&6Jump Boost")
        .item(
            createMenuItem(
                true,
                1,
                3,
                XMaterial.SLIME_BLOCK.parseMaterial(),
                "&6&lJump Boost &f(%current%)",
                "&7Boost your jump height!"))
        .maxValue(10)
        .triggers("join", "respawn")
        .addCallback(new JumpSettingCallback())
        .build();
  }

  @NotNull
  private static ImmutableSetting createStackerSettings() {
    return defaultBuilder()
        .name("stacker-setting")
        .displayName("&6Stacker")
        .item(
            createMenuItem(
                false,
                1,
                5,
                XMaterial.SADDLE.parseMaterial(),
                "&6&lStacker &f(%current%)",
                "&7Stack players on top of your head!"))
        .addListener(new StackerSettingListener())
        .build();
  }

  @NotNull
  private static ImmutableSetting createFlySetting() {
    return defaultBuilder()
        .name("fly-setting")
        .displayName("&6Fly")
        .item(
            createMenuItem(
                true,
                1,
                7,
                XMaterial.FEATHER.parseMaterial(),
                "&6&lFly &f(%current%)",
                "&7Toggle your ability to fly and boost your flight speed!"))
        .maxValue(10)
        .addCallback(new FlySettingCallback())
        .addListener(new FlySettingListener())
        .build();
  }

  @NotNull
  private static ImmutableSetting createVanishSetting() {
    return defaultBuilder()
        .name("vanish-setting")
        .displayName("&6Vanish")
        .item(
            createMenuItem(
                false,
                1,
                29,
                XMaterial.ENDER_PEARL.parseMaterial(),
                "&6&lVanish &f(%current%)",
                "&7Make yourself invisible to other players!"))
        .addCallback(new VanishSettingCallback())
        .addListener(new VanishSettingListener())
        .build();
  }

  @NotNull
  private static ImmutableSetting createChatSetting() {
    return defaultBuilder()
        .name("chat-setting")
        .displayName("&6Chat")
        .item(
            createMenuItem(
                false,
                1,
                31,
                XMaterial.PAPER.parseMaterial(),
                "&6&lChat &f(%current%)",
                "&7Toggle your ability to see or send chat messages!"))
        .defaultValue(1)
        .addListener(new ChatSettingListener())
        .build();
  }

  @NotNull
  private static ImmutableSetting createVisibilitySetting() {
    return defaultBuilder()
        .name("visibility-setting")
        .displayName("&6Visibility")
        .item(
            createMenuItem(
                false,
                1,
                33,
                XMaterial.PAPER.parseMaterial(),
                "&6&lVisibility &f(%current%)",
                "&7Shows and hides other players from your sight!"))
        .defaultValue(1)
        .triggers("join", "respawn")
        .addCallback(new VisibilitySettingCallback())
        .addListener(new VisibilitySettingListener())
        .build();
  }

  private static MenuItem createMenuItem(
      boolean augmentable, int page, int slot, Material material, String name, String... lore) {
    List<String> loreList = Lists.newArrayList(lore);
    loreList.add("");
    if (augmentable) {
      loreList.add("&6Left click &7to cycle next");
      loreList.add("&6Right click &7to cycle previous");
    } else {
      loreList.add("&6Click &7to toggle");
    }

    return ImmutableMenuItem.builder()
        .page(page)
        .slot(slot)
        .itemStack(Item.builder().material(material).name(name).lore(loreList).build())
        .build();
  }

  private static ImmutableSetting.Builder defaultBuilder() {
    return ImmutableSetting.builder()
        .valueAliases(Constants.DEFAULT_VALUE_ALIASES)
        .defaultValue(0)
        .maxValue(1)
        .triggers();
  }

  public static final class Constants {
    public static final ImmutableListMultimap<String, Integer> DEFAULT_VALUE_ALIASES;

    static {
      ImmutableListMultimap.Builder<String, Integer> builder = ImmutableListMultimap.builder();
      builder.put("off", 0);
      builder.put("on", 1);
      IntStream.range(0, 11).boxed().forEach((i) -> builder.put("x" + i, i));
      DEFAULT_VALUE_ALIASES = builder.build();
    }
  }
}

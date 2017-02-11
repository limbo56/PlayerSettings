package me.limbo56.settings.utils;

import me.limbo56.settings.config.MenuConfiguration;
import me.limbo56.settings.config.MessageConfiguration;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.player.CustomPlayer;

import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.HashMap;
import java.util.List;

/**
 * Created by lim_bo56
 * On 8/14/2016
 * At 11:06 AM
 */
public class Cache {

    public static final String CHAT_TITLE = "&6PlayerSettings &f&l>&9&l> ";

    public static final String ENABLED_NAME = MenuConfiguration.get("Menu.Items.Enabled.Name");
    public static final String DISABLED_NAME = MenuConfiguration.get("Menu.Items.Disabled.Name");

    public static final String NO_PERMISSIONS = MessageConfiguration.get("No-Permissions");

    public static final List<String> ENABLED_LORE = ConfigurationManager.getMenu().getStringList("Menu.Items.Enabled.Lore");
    public static final List<String> DISABLED_LORE = ConfigurationManager.getMenu().getStringList("Menu.Items.Disabled.Lore");
    public static final List<String> WORLDS_ALLOWED = ConfigurationManager.getMenu().getStringList("worlds-allowed");

    public static final Permission CMD_RELOAD = new Permission("settings.reload");
    public static final Permission SPEED_PERMISSION = new Permission("settings.speed");
    public static final Permission JUMP_PERMISSION = new Permission("settings.jump");
    public static final Permission FLY_PERMISSION = new Permission("settings.fly");
    public static final Permission VANISH_PERMISSION = new Permission("settings.vanish");
    public static final Permission RADIO_PERMISSION = new Permission("settings.radio");

    public static final PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 1000000000, 0);
    public static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, 1000000000, ConfigurationManager.getDefault().getInt("Speed.level") - 1);
    public static final PotionEffect JUMP = new PotionEffect(PotionEffectType.JUMP, 1000000000, ConfigurationManager.getDefault().getInt("Jump.level") - 1);
}
package me.lim_bo56.settings.utils;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.managers.ConfigurationManager;
import org.bukkit.entity.Player;
import org.bukkit.permissions.Permission;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;

/**
 * Created by lim_bo56
 * On 8/14/2016
 * At 11:06 AM
 */
public class Variables {

    public static final boolean Sql = Core.getInstance().getConfig().getBoolean("MySQL.enable");
    public static final String CHAT_TITLE = ColorUtils.Color("&6PlayerSettings &f&l>&9&l> &r");
    public static final Permission CMD_RELOAD = new Permission("settings.reload");
    public static final PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 0);
    public static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, 100000, 1);
    public static final PotionEffect JUMP = new PotionEffect(PotionEffectType.JUMP, 100000, 1);
    public static ArrayList<Player> VISIBILITY_LIST = new ArrayList<Player>();
    public static ArrayList<Player> SPEED_LIST = new ArrayList<Player>();
    public static ArrayList<Player> JUMP_LIST = new ArrayList<Player>();
    public static ArrayList<Player> FLY_LIST = new ArrayList<Player>();
    public static ArrayList<Player> CHAT_LIST = new ArrayList<Player>();
    public static ArrayList<Player> STACKER_LIST = new ArrayList<Player>();
    public static ArrayList<Player> VANISH_LIST = new ArrayList<Player>();
    public static boolean defaultVisibility = ConfigurationManager.getDefault().getBoolean("Default.Visibility");
    public static boolean defaultStacker = ConfigurationManager.getDefault().getBoolean("Default.Stacker");
    public static boolean defaultChat = ConfigurationManager.getDefault().getBoolean("Default.Chat");
    public static boolean defaultVanish = ConfigurationManager.getDefault().getBoolean("Default.Vanish");
    public static boolean defaultFly = ConfigurationManager.getDefault().getBoolean("Default.Fly");
    public static boolean defaultSpeed = ConfigurationManager.getDefault().getBoolean("Default.Speed");
    public static boolean defaultJump = ConfigurationManager.getDefault().getBoolean("Default.Jump");

}

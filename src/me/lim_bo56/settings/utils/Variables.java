package me.lim_bo56.settings.utils;

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

    public static final String CHAT_TITLE = ColorUtils.Color("&6PlayerSettings &f&l>&9&l> &r");
    public static final Permission CMD_RELOAD = new Permission("settings.reload");
    public static final PotionEffect INVISIBILITY = new PotionEffect(PotionEffectType.INVISIBILITY, 100000, 0);
    public static final PotionEffect SPEED = new PotionEffect(PotionEffectType.SPEED, 100000, 1);
    public static final PotionEffect JUMP = new PotionEffect(PotionEffectType.JUMP, 100000, 1);
    public static final ArrayList<Player> VISIBILITY_LIST = new ArrayList<>();
    public static final ArrayList<Player> SPEED_LIST = new ArrayList<>();
    public static final ArrayList<Player> JUMP_LIST = new ArrayList<>();
    public static final ArrayList<Player> FLY_LIST = new ArrayList<>();
    public static final ArrayList<Player> CHAT_LIST = new ArrayList<>();
    public static final ArrayList<Player> STACKER_LIST = new ArrayList<>();
    public static final ArrayList<Player> VANISH_LIST = new ArrayList<>();
    public static final boolean defaultVisibility = ConfigurationManager.getDefault().get("Default.Visibility");
    public static final boolean defaultStacker = ConfigurationManager.getDefault().get("Default.Stacker");
    public static final boolean defaultChat = ConfigurationManager.getDefault().get("Default.Chat");
    public static final boolean defaultVanish = ConfigurationManager.getDefault().get("Default.Vanish");
    public static final boolean defaultFly = ConfigurationManager.getDefault().get("Default.Fly");
    public static final boolean defaultSpeed = ConfigurationManager.getDefault().get("Default.Speed");
    public static final boolean defaultJump = ConfigurationManager.getDefault().get("Default.Jump");

}

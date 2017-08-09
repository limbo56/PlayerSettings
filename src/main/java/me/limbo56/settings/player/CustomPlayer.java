package me.limbo56.settings.player;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.managers.ConfigurationManager;
import me.limbo56.settings.utils.Utilities;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * Created by lim_bo56
 * On 8/11/2016
 * At 11:50 AM
 */
public class CustomPlayer {

    public boolean doubleJumpStatus;
    private Player player;
    private UUID uuid;
    private boolean visibility;
    private boolean stacker;
    private boolean chat;
    private boolean vanish;
    private boolean fly;
    private boolean speed;
    private boolean jump;
    private boolean radio;
    private boolean doubleJump;

    public CustomPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.visibility = ConfigurationManager.getDefault().getBoolean("Default.Visibility");
        this.stacker = ConfigurationManager.getDefault().getBoolean("Default.Stacker");
        this.chat = ConfigurationManager.getDefault().getBoolean("Default.Chat");
        this.vanish = ConfigurationManager.getDefault().getBoolean("Default.Vanish");
        this.fly = ConfigurationManager.getDefault().getBoolean("Default.Fly");
        this.speed = ConfigurationManager.getDefault().getBoolean("Default.Speed");
        this.jump = ConfigurationManager.getDefault().getBoolean("Default.Jump");
        this.radio = (Utilities.hasRadioPlugin() && ConfigurationManager.getDefault().getBoolean("Default.Radio"));
        this.doubleJump = ConfigurationManager.getDefault().getBoolean("Default.DoubleJump");
        this.doubleJumpStatus = false;

        if (ConfigurationManager.getConfig().getBoolean("Debug")) {
            PlayerSettings.getInstance().log("CustomPlayer: UUID '" + uuid + "' created:");
            PlayerSettings.getInstance().log("CustomPlayer: Visibility: " + visibility + ", Stacker: " + stacker + ", Chat: " + chat + ", Vanish: " + vanish + ", Fly: " + fly + ", Speed: " + speed + ", Jump: " + jump + ", Radio: " + radio + ", DoubleJump: " + doubleJump);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public boolean getVisibility() {
        return visibility;
    }

    public void setVisibility(final boolean visibility) {
        this.visibility = visibility;
    }

    public boolean getStacker() {
        return stacker;
    }

    public void setStacker(final boolean stacker) {
        this.stacker = stacker;
    }

    public boolean getChat() {
        return chat;
    }

    public void setChat(final boolean chat) {
        this.chat = chat;
    }

    public boolean getVanish() {
        return vanish;
    }

    public void setVanish(final boolean vanish) {
        this.vanish = vanish;
    }

    public boolean getFly() {
        return fly;
    }

    public void setFly(final boolean fly) {
        this.fly = fly;
    }

    public boolean getSpeed() {
        return speed;
    }

    public void setSpeed(final boolean speed) {
        this.speed = speed;
    }

    public boolean getJump() {
        return jump;
    }

    public void setJump(final boolean jump) {
        this.jump = jump;
    }

    public boolean getRadio() {
        return radio;
    }

    public void setRadio(final boolean radio) {
        this.radio = radio;
    }

    public boolean getDoubleJump() {
        return doubleJump;
    }

    public void setDoubleJump(final boolean doubleJump) {
        this.doubleJump = doubleJump;
    }
}
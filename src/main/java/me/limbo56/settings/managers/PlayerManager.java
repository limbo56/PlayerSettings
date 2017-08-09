package me.limbo56.settings.managers;

import me.limbo56.settings.PlayerSettings;
import me.limbo56.settings.player.CustomPlayer;
import me.limbo56.settings.utils.Utilities;
import org.bukkit.Bukkit;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

/**
 * Created by limbo56(David)
 * On 6/15/2017
 * At 9:02 PM
 */
public class PlayerManager {

    private final String ADD = "INSERT INTO `PlayerSettings` (UUID, Visibility, Stacker, Chat, Vanish, Fly, Speed, Jump, Radio, DoubleJump) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    private final String SAVE = "UPDATE `PlayerSettings` SET Visibility = ?, Stacker = ?, Chat = ?, Vanish = ?, Fly = ?, Speed = ?, Jump = ?, Radio = ?, DoubleJump = ?";

    private HashMap<UUID, CustomPlayer> playerList = new HashMap<>();

    public boolean containsPlayer(CustomPlayer customPlayer) {
        if (PlayerSettings.getInstance().getConfig().getBoolean("MySQL.enable")) {
            try {
                PreparedStatement select = PlayerSettings.getMySqlManager().getCurrentConnection().prepareStatement(
                        "SELECT UUID FROM `PlayerSettings` WHERE UUID = ?");

                select.setString(1, customPlayer.getUuid().toString());

                ResultSet resultset = select.executeQuery();

                boolean containsPlayerUUID = resultset.next();

                if (ConfigurationManager.getConfig().getBoolean("Debug"))
                    PlayerSettings.getInstance().log("containsPlayer: Checking UUID '" + customPlayer.getUuid().toString() + "', returning " + containsPlayerUUID);

                select.close();
                resultset.close();

                return containsPlayerUUID;
            } catch (SQLException exception) {
                if (ConfigurationManager.getConfig().getBoolean("Debug"))
                    PlayerSettings.getInstance().log("containsPlayer: SQLException, returning false");
                exception.printStackTrace();
                return false;
            }
        } else {
            if (ConfigurationManager.getConfig().getBoolean("Debug"))
                PlayerSettings.getInstance().log("containsPlayer: MySQL is Disabled, returning false");
            return false;
        }
    }

    public void addPlayer(final CustomPlayer customPlayer) {
        if (PlayerSettings.getInstance().getConfig().getBoolean("MySQL.enable")) {
            final int
                    visibility = (customPlayer.getVisibility()) ? 1 : 0,
                    stacker = (customPlayer.getStacker()) ? 1 : 0,
                    chat = (customPlayer.getChat()) ? 1 : 0,
                    vanish = (customPlayer.getVanish()) ? 1 : 0,
                    fly = (customPlayer.getFly()) ? 1 : 0,
                    speed = (customPlayer.getSpeed()) ? 1 : 0,
                    jump = (customPlayer.getJump()) ? 1 : 0,
                    radio = (customPlayer.getRadio()) ? 1 : 0,
                    doubleJump = (customPlayer.getDoubleJump()) ? 1 : 0;

            Bukkit.getScheduler().runTaskAsynchronously(PlayerSettings.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement insert = PlayerSettings.getMySqlManager().getCurrentConnection().prepareStatement(ADD);

                        insert.setString(1, customPlayer.getUuid().toString());
                        insert.setInt(2, visibility);
                        insert.setInt(3, stacker);
                        insert.setInt(4, chat);
                        insert.setInt(5, vanish);
                        insert.setInt(6, fly);
                        insert.setInt(7, speed);
                        insert.setInt(8, jump);
                        insert.setInt(9, radio);
                        insert.setInt(10, doubleJump);

                        insert.executeUpdate();

                        if (ConfigurationManager.getConfig().getBoolean("Debug")) {
                            PlayerSettings.getInstance().log("addPlayer: UUID '" + customPlayer.getUuid().toString() + "' added to the database:");
                            PlayerSettings.getInstance().log("addPlayer: Visibility: " + visibility + ", Stacker: " + stacker + ", Chat: " + chat + ", Vanish: " + vanish + ", Fly: " + fly + ", Speed: " + speed + ", Jump: " + jump + ", Radio: " + radio + ", DoubleJump: " + doubleJump);
                        }
                        insert.close();

                    } catch (SQLException exception) {
                        if (ConfigurationManager.getConfig().getBoolean("Debug"))
                            PlayerSettings.getInstance().log("addPlayer: SQLException, doing nothing");
                        exception.printStackTrace();
                    }
                }
            });
        } else if (ConfigurationManager.getConfig().getBoolean("Debug"))
            PlayerSettings.getInstance().log("addPlayer: MySQL is Disabled, doing nothing");
    }

    public void loadSettings(CustomPlayer customPlayer) {
        if (PlayerSettings.getInstance().getConfig().getBoolean("MySQL.enable")) {
            if (PlayerSettings.getMySqlManager().checkTable()) {
                customPlayer.setVisibility(getBoolean(customPlayer, "Visibility"));
                customPlayer.setStacker(getBoolean(customPlayer, "Stacker"));
                customPlayer.setChat(getBoolean(customPlayer, "Chat"));
                customPlayer.setVanish(getBoolean(customPlayer, "Vanish"));
                customPlayer.setFly(!getBoolean(customPlayer, "DoubleJump") && getBoolean(customPlayer, "Fly"));
                customPlayer.setSpeed(getBoolean(customPlayer, "Speed"));
                customPlayer.setJump(getBoolean(customPlayer, "Jump"));
                customPlayer.setRadio(Utilities.hasRadioPlugin() && getBoolean(customPlayer, "Radio"));
                customPlayer.setDoubleJump(!getBoolean(customPlayer, "Fly") && getBoolean(customPlayer, "DoubleJump"));
            } else if (ConfigurationManager.getConfig().getBoolean("Debug"))
                PlayerSettings.getInstance().log("loadSettings: checkTable returned false, table is not existent");
        } else if (ConfigurationManager.getConfig().getBoolean("Debug"))
            PlayerSettings.getInstance().log("loadSettings: MySQL is Disabled, doing nothing");
    }

    public void saveSettingsSync(CustomPlayer customPlayer) {
        if (PlayerSettings.getInstance().getConfig().getBoolean("MySQL.enable")) {
            final int visibility = (customPlayer.getVisibility()) ? 1 : 0,
                    stacker = (customPlayer.getStacker()) ? 1 : 0,
                    chat = (customPlayer.getChat()) ? 1 : 0,
                    vanish = (customPlayer.getVanish()) ? 1 : 0,
                    fly = (customPlayer.getFly()) ? 1 : 0,
                    speed = (customPlayer.getSpeed()) ? 1 : 0,
                    jump = (customPlayer.getJump()) ? 1 : 0,
                    radio = ((Utilities.hasRadioPlugin() && customPlayer.getRadio()) ? 1 : 0),
                    doubleJump = (customPlayer.getDoubleJump()) ? 1 : 0;

            try {
                PreparedStatement update = PlayerSettings.getMySqlManager().getCurrentConnection().prepareStatement(SAVE);

                update.setInt(1, visibility);
                update.setInt(2, stacker);
                update.setInt(3, chat);
                update.setInt(4, vanish);
                update.setInt(5, fly);
                update.setInt(6, speed);
                update.setInt(7, jump);
                update.setInt(8, radio);
                update.setInt(9, doubleJump);

                update.executeUpdate();

                if (ConfigurationManager.getConfig().getBoolean("Debug")) {
                    PlayerSettings.getInstance().log("saveSettings: UUID '" + customPlayer.getUuid().toString() + "' settigns updated in the database:");
                    PlayerSettings.getInstance().log("saveSettings: Visibility: " + visibility + ", Stacker: " + stacker + ", Chat: " + chat + ", Vanish: " + vanish + ", Fly: " + fly + ", Speed: " + speed + ", Jump: " + jump + ", Radio: " + radio + ", DoubleJump: " + doubleJump);
                }

                update.close();
            } catch (SQLException exception) {
                if (ConfigurationManager.getConfig().getBoolean("Debug"))
                    PlayerSettings.getInstance().log("saveSettings: SQLException, doing nothing");
                exception.printStackTrace();
            }
        } else if (ConfigurationManager.getConfig().getBoolean("Debug"))
            PlayerSettings.getInstance().log("saveSettings: MySQL is Disabled, doing nothing");
    }

    public void saveSettingsAsync(final CustomPlayer customPlayer) {
        if (PlayerSettings.getInstance().getConfig().getBoolean("MySQL.enable")) {
            Bukkit.getScheduler().runTaskAsynchronously(PlayerSettings.getInstance(), new Runnable() {
                @Override
                public void run() {
                    final int visibility = (customPlayer.getVisibility()) ? 1 : 0,
                            stacker = (customPlayer.getStacker()) ? 1 : 0,
                            chat = (customPlayer.getChat()) ? 1 : 0,
                            vanish = (customPlayer.getVanish()) ? 1 : 0,
                            fly = (customPlayer.getFly()) ? 1 : 0,
                            speed = (customPlayer.getSpeed()) ? 1 : 0,
                            jump = (customPlayer.getJump()) ? 1 : 0,
                            radio = ((Utilities.hasRadioPlugin() && customPlayer.getRadio()) ? 1 : 0),
                            doubleJump = (customPlayer.getDoubleJump()) ? 1 : 0;

                    try {
                        PreparedStatement update = PlayerSettings.getMySqlManager().getCurrentConnection().prepareStatement(SAVE);

                        update.setInt(1, visibility);
                        update.setInt(2, stacker);
                        update.setInt(3, chat);
                        update.setInt(4, vanish);
                        update.setInt(5, fly);
                        update.setInt(6, speed);
                        update.setInt(7, jump);
                        update.setInt(8, radio);
                        update.setInt(9, doubleJump);

                        update.executeUpdate();

                        if (ConfigurationManager.getConfig().getBoolean("Debug")) {
                            PlayerSettings.getInstance().log("saveSettings: UUID '" + customPlayer.getUuid().toString() + "' settings updated in the database:");
                            PlayerSettings.getInstance().log("saveSettings: Visibility: " + customPlayer.getVisibility() + ", Stacker: " + customPlayer.getStacker() + ", Chat: " + customPlayer.getChat() + ", Vanish: " + customPlayer.getVanish() + ", Fly: " + customPlayer.getFly() + ", Speed: " + customPlayer.getDoubleJump() + ", Jump: " + customPlayer.getDoubleJump() + ", Radio: " + customPlayer.getDoubleJump() + ", DoubleJump: " + customPlayer.getDoubleJump());
                        }

                        update.close();
                    } catch (SQLException exception) {
                        if (ConfigurationManager.getConfig().getBoolean("Debug"))
                            PlayerSettings.getInstance().log("saveSettings: SQLException, doing nothing");

                        exception.printStackTrace();
                    }

                }
            });
        }
    }

    private boolean getBoolean(CustomPlayer customPlayer, String str) {
        try {
            PreparedStatement select = PlayerSettings.getMySqlManager().getCurrentConnection().prepareStatement("SELECT ? FROM `PlayerSettings` WHERE UUID = ?");

            select.setString(1, str);
            select.setString(2, customPlayer.getUuid().toString());

            ResultSet rs = select.executeQuery();

            if (rs.next()) {
                if (rs.wasNull()) {
                    if (ConfigurationManager.getConfig().getBoolean("Debug"))
                        PlayerSettings.getInstance().log("getBoolean: Value '" + str + "' was null, getting default");

                    return ConfigurationManager.getDefault().getBoolean("Default." + str);
                } else {
                    if (ConfigurationManager.getConfig().getBoolean("Debug"))
                        PlayerSettings.getInstance().log("getBoolean: Value '" + str + "', returning " + rs.getBoolean(1));

                    return rs.getBoolean(1);
                }
            }

            rs.close();
        } catch (SQLException e) {
            if (ConfigurationManager.getConfig().getBoolean("Debug"))
                PlayerSettings.getInstance().log("getBoolean: SQLException, returning false");

            e.printStackTrace();
            return false;
        }

        if (ConfigurationManager.getConfig().getBoolean("Debug"))
            PlayerSettings.getInstance().log("getBoolean: Value '" + str + "' not found, returning false");

        return false;
    }

    public HashMap<UUID, CustomPlayer> getPlayerList() {
        return playerList;
    }
}

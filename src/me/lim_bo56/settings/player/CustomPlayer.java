package me.lim_bo56.settings.player;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.mysql.MySqlConnection;
import me.lim_bo56.settings.utils.Utilities;
import me.lim_bo56.settings.utils.Variables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

/**
 * Created by lim_bo56
 * On 8/11/2016
 * At 11:50 AM
 */
public class CustomPlayer {

    private Player player;
    private UUID uuid;

    public CustomPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
    }

    public Player getPlayer() {
        return player;
    }

    private UUID getUuid() {
        return uuid;
    }

    public boolean containsPlayer() {
        if (Core.getInstance().getConfig().getBoolean("MySQL.enable")) {
            try {
                PreparedStatement sql = MySqlConnection.getInstance().getCurrentConnection().prepareStatement(
                        "SELECT UUID FROM `players` WHERE UUID = '" + getUuid().toString() + "'");
                ResultSet resultset = sql.executeQuery();
                boolean containsPlayerUUID = resultset.next();

                sql.close();
                resultset.close();

                return containsPlayerUUID;
            } catch (SQLException exception) {
                exception.printStackTrace();
                return false;
            }
        } else {
            return false;
        }
    }

    public void addPlayer() {
        if (Core.getInstance().getConfig().getBoolean("MySQL.enable")) {
            final int
                    visibility = (ConfigurationManager.getDefault().get("Default.Visibility")) ? 1 : 0,
                    stacker = (ConfigurationManager.getDefault().get("Default.Stacker")) ? 1 : 0,
                    chat = (ConfigurationManager.getDefault().get("Default.Chat")) ? 1 : 0,
                    vanish = (ConfigurationManager.getDefault().get("Default.Vanish")) ? 1 : 0,
                    fly = (ConfigurationManager.getDefault().get("Default.Fly")) ? 1 : 0,
                    speed = (ConfigurationManager.getDefault().get("Default.Speed")) ? 1 : 0,
                    jump = (ConfigurationManager.getDefault().get("Default.Jump")) ? 1 : 0;

            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {
                        PreparedStatement sql = MySqlConnection.getInstance().getCurrentConnection().prepareStatement(
                                "INSERT INTO players (UUID, Visibility, Stacker, Chat, Vanish, Fly, Speed, Jump) VALUES (" +
                                        "'" + uuid.toString() + "', " +
                                        "'" + visibility + "', " +
                                        "'" + stacker + "', " +
                                        "'" + chat + "', " +
                                        "'" + vanish + "', " +
                                        "'" + fly + "', " +
                                        "'" + speed + "', " +
                                        "'" + jump + "')");

                        sql.execute();
                        sql.close();

                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        }
    }

    public void loadSettings() {
        if (Core.getInstance().getConfig().getBoolean("MySQL.enable")) {

            boolean visibility = getBoolean("Visibility"),
                    stacker = getBoolean("Stacker"),
                    chat = getBoolean("Chat"),
                    vanish = getBoolean("Vanish"),
                    fly = getBoolean("Fly"),
                    speed = getBoolean("Speed"),
                    jump = getBoolean("Jump");

            if (visibility) Variables.VISIBILITY_LIST.put(player.getUniqueId(), true);
            if (stacker) Variables.STACKER_LIST.put(player.getUniqueId(), true);
            if (chat) Variables.CHAT_LIST.put(player.getUniqueId(), true);
            if (vanish) Variables.VANISH_LIST.put(player.getUniqueId(), true);
            if (fly) Variables.FLY_LIST.put(player.getUniqueId(), true);
            if (speed) Variables.SPEED_LIST.put(player.getUniqueId(), true);
            if (jump) Variables.JUMP_LIST.put(player.getUniqueId(), true);

        }

        Utilities.addToDefault(player);

    }

    public void saveSettings() {
        if (Core.getInstance().getConfig().getBoolean("MySQL.enable")) {

            final int visibility = (hasVisibility()) ? 1 : 0,
                    stacker = (hasStacker()) ? 1 : 0,
                    chat = (hasChat()) ? 1 : 0,
                    vanish = (hasVanish()) ? 1 : 0,
                    fly = (hasFly()) ? 1 : 0,
                    speed = (hasSpeed()) ? 1 : 0,
                    jump = (hasJump()) ? 1 : 0;

            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {

                        Statement statement = MySqlConnection.getInstance().getCurrentConnection().createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

                        statement.addBatch("UPDATE `players` SET `Visibility` = " + visibility + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `players` SET `Stacker` = " + stacker + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `players` SET `Chat` = " + chat + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `players` SET `Vanish` = " + vanish + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `players` SET `Fly` = " + fly + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `players` SET `Speed` = " + speed + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `players` SET `Jump` = " + jump + " WHERE UUID = '" + uuid.toString() + "'");

                        statement.executeBatch();
                        statement.close();

                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }

                }
            });
        }
    }

    private boolean getBoolean(String str) {
        try {
            ResultSet rs = MySqlConnection.getInstance().getCurrentConnection().createStatement().executeQuery(
                    "SELECT `" + str + "` FROM `players` WHERE `UUID` = '" + getUuid().toString() + "'");

            if (rs.next()) {
                return rs.getBoolean(1);
            }

            rs.close();

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

    public void setVisibility(final boolean visibility) {
        if (Variables.VISIBILITY_LIST.containsKey(player.getUniqueId())) {
            Variables.VISIBILITY_LIST.remove(player.getUniqueId());
        }

        Variables.VISIBILITY_LIST.put(player.getUniqueId(), visibility);

    }

    public boolean hasVisibility() {
        if (!Variables.VISIBILITY_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Variables.VISIBILITY_LIST.get(player.getUniqueId());
    }

    public void setStacker(final boolean stacker) {
        if (Variables.STACKER_LIST.containsKey(player.getUniqueId())) {
            Variables.STACKER_LIST.remove(player.getUniqueId());
        }

        Variables.STACKER_LIST.put(player.getUniqueId(), stacker);
    }

    public boolean hasStacker() {
        if (!Variables.STACKER_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Variables.STACKER_LIST.get(player.getUniqueId());
    }

    public void setChat(final boolean chat) {
        if (Variables.CHAT_LIST.containsKey(player.getUniqueId())) {
            Variables.CHAT_LIST.remove(player.getUniqueId());
        }

        Variables.CHAT_LIST.put(player.getUniqueId(), chat);
    }

    public boolean hasChat() {
        if (!Variables.CHAT_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Variables.CHAT_LIST.get(player.getUniqueId());
    }

    public void setVanish(final boolean vanish) {
        if (Variables.VANISH_LIST.containsKey(player.getUniqueId())) {
            Variables.VANISH_LIST.remove(player.getUniqueId());
        }

        Variables.VANISH_LIST.put(player.getUniqueId(), vanish);
    }

    public boolean hasVanish() {
        if (!Variables.VANISH_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Variables.VANISH_LIST.get(player.getUniqueId());
    }

    public void setFly(final boolean fly) {
        if (Variables.FLY_LIST.containsKey(player.getUniqueId())) {
            Variables.FLY_LIST.remove(player.getUniqueId());
        }

        Variables.FLY_LIST.put(player.getUniqueId(), fly);
    }

    public boolean hasFly() {
        if (!Variables.FLY_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Variables.FLY_LIST.get(player.getUniqueId());
    }

    public void setSpeed(final boolean speed) {
        if (Variables.SPEED_LIST.containsKey(player.getUniqueId())) {
            Variables.SPEED_LIST.remove(player.getUniqueId());
        }

        Variables.SPEED_LIST.put(player.getUniqueId(), speed);
    }

    public boolean hasSpeed() {
        if (!Variables.SPEED_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Variables.SPEED_LIST.get(player.getUniqueId());
    }

    public void setJump(final boolean jump) {
        if (Variables.JUMP_LIST.containsKey(player.getUniqueId())) {
            Variables.JUMP_LIST.remove(player.getUniqueId());
        }

        Variables.JUMP_LIST.put(player.getUniqueId(), jump);
    }

    public boolean hasJump() {
        if (!Variables.JUMP_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Variables.JUMP_LIST.get(player.getUniqueId());
    }

}

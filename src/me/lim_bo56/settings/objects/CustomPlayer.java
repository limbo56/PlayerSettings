package me.lim_bo56.settings.objects;

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

    private boolean Sql = ConfigurationManager.getConfig().getBoolean("MySQL.enable");
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
        if (Sql) {
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
        if (Sql) {
            final int
                    visibility = (Variables.defaultVisibility) ? 1 : 0,
                    stacker = (Variables.defaultStacker) ? 1 : 0,
                    chat = (Variables.defaultChat) ? 1 : 0,
                    vanish = (Variables.defaultVanish) ? 1 : 0,
                    fly = (Variables.defaultFly) ? 1 : 0,
                    speed = (Variables.defaultSpeed) ? 1 : 0,
                    jump = (Variables.defaultJump) ? 1 : 0;

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
        if (Sql) {

            boolean visibiliy = getBoolean("Visibility"),
                    stacker = getBoolean("Stacker"),
                    chat = getBoolean("Chat"),
                    vanish = getBoolean("Vanish"),
                    fly = getBoolean("Fly"),
                    speed = getBoolean("Speed"),
                    jump = getBoolean("Jump");

            if (visibiliy) Variables.VISIBILITY_LIST.add(player.getUniqueId());
            if (stacker) Variables.STACKER_LIST.add(player.getUniqueId());
            if (chat) Variables.CHAT_LIST.add(player.getUniqueId());
            if (vanish) Variables.VANISH_LIST.add(player.getUniqueId());
            if (fly) Variables.FLY_LIST.add(player.getUniqueId());
            if (speed) Variables.SPEED_LIST.add(player.getUniqueId());
            if (jump) Variables.JUMP_LIST.add(player.getUniqueId());

        }

        Utilities.addToDefault(player);

    }

    public void saveSettings() {
        if (Sql) {

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
        if (visibility)
            Variables.VISIBILITY_LIST.add(player.getUniqueId());
        else
            Variables.VISIBILITY_LIST.remove(player.getUniqueId());
    }

    public synchronized boolean hasVisibility() {
        return (Variables.VISIBILITY_LIST.contains(player.getUniqueId()));
    }

    public void setStacker(final boolean stacker) {
        if (stacker)
            Variables.STACKER_LIST.add(player.getUniqueId());
        else
            Variables.STACKER_LIST.remove(player.getUniqueId());
    }

    public synchronized boolean hasStacker() {
        return Variables.STACKER_LIST.contains(player.getUniqueId());
    }

    public void setChat(final boolean chat) {
        if (chat)
            Variables.CHAT_LIST.add(player.getUniqueId());
        else
            Variables.CHAT_LIST.remove(player.getUniqueId());
    }

    public synchronized boolean hasChat() {
        return Variables.CHAT_LIST.contains(player.getUniqueId());
    }

    public void setVanish(final boolean vanish) {
        if (vanish)
            Variables.VANISH_LIST.add(player.getUniqueId());
        else
            Variables.VANISH_LIST.remove(player.getUniqueId());
    }

    public synchronized boolean hasVanish() {
        return Variables.VANISH_LIST.contains(player.getUniqueId());
    }

    public void setFly(final boolean fly) {
        if (fly)
            Variables.FLY_LIST.add(player.getUniqueId());
        else
            Variables.FLY_LIST.remove(player.getUniqueId());
    }

    public synchronized boolean hasFly() {
        return Variables.FLY_LIST.contains(player.getUniqueId());
    }

    public void setSpeed(final boolean speed) {
        if (speed)
            Variables.SPEED_LIST.add(player.getUniqueId());
        else
            Variables.SPEED_LIST.remove(player.getUniqueId());
    }

    public synchronized boolean hasSpeed() {
        return Variables.SPEED_LIST.contains(player.getUniqueId());
    }

    public void setJump(final boolean jump) {
        if (jump)
            Variables.JUMP_LIST.add(player.getUniqueId());
        else
            Variables.JUMP_LIST.remove(player.getUniqueId());
    }

    public synchronized boolean hasJump() {
        return Variables.JUMP_LIST.contains(player.getUniqueId());
    }

}

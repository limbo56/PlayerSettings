package me.lim_bo56.settings.player;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.managers.ConfigurationManager;
import me.lim_bo56.settings.mysql.MySqlConnection;
import me.lim_bo56.settings.utils.Utilities;
import me.lim_bo56.settings.utils.Cache;
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
                        "SELECT UUID FROM `PlayerSettings` WHERE UUID = '" + getUuid().toString() + "'");
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
                                "INSERT INTO `PlayerSettings` (UUID, Visibility, Stacker, Chat, Vanish, Fly, Speed, Jump) VALUES (" +
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
            if (MySqlConnection.getInstance().checkTable()) {

                boolean visibility = getBoolean("Visibility"),
                        stacker = getBoolean("Stacker"),
                        chat = getBoolean("Chat"),
                        vanish = getBoolean("Vanish"),
                        fly = getBoolean("Fly"),
                        speed = getBoolean("Speed"),
                        jump = getBoolean("Jump");

                if (visibility) Cache.VISIBILITY_LIST.put(player.getUniqueId(), true);
                if (stacker) Cache.STACKER_LIST.put(player.getUniqueId(), true);
                if (chat) Cache.CHAT_LIST.put(player.getUniqueId(), true);
                if (vanish) Cache.VANISH_LIST.put(player.getUniqueId(), true);
                if (fly) Cache.FLY_LIST.put(player.getUniqueId(), true);
                if (speed) Cache.SPEED_LIST.put(player.getUniqueId(), true);
                if (jump) Cache.JUMP_LIST.put(player.getUniqueId(), true);
            }
        } else {
            Utilities.addToDefault(player);
        }
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

                        statement.addBatch("UPDATE `PlayerSettings` SET `Visibility` = " + visibility + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `PlayerSettings` SET `Stacker` = " + stacker + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `PlayerSettings` SET `Chat` = " + chat + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `PlayerSettings` SET `Vanish` = " + vanish + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `PlayerSettings` SET `Fly` = " + fly + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `PlayerSettings` SET `Speed` = " + speed + " WHERE UUID = '" + uuid.toString() + "'");
                        statement.addBatch("UPDATE `PlayerSettings` SET `Jump` = " + jump + " WHERE UUID = '" + uuid.toString() + "'");

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
                    "SELECT `" + str + "` FROM `PlayerSettings` WHERE `UUID` = '" + getUuid().toString() + "'");

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
        if (Cache.VISIBILITY_LIST.containsKey(player.getUniqueId())) {
            Cache.VISIBILITY_LIST.remove(player.getUniqueId());
        }

        Cache.VISIBILITY_LIST.put(player.getUniqueId(), visibility);

    }

    public boolean hasVisibility() {
        if (!Cache.VISIBILITY_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Cache.VISIBILITY_LIST.get(player.getUniqueId());
    }

    public void setStacker(final boolean stacker) {
        if (Cache.STACKER_LIST.containsKey(player.getUniqueId())) {
            Cache.STACKER_LIST.remove(player.getUniqueId());
        }

        Cache.STACKER_LIST.put(player.getUniqueId(), stacker);
    }

    public boolean hasStacker() {
        if (!Cache.STACKER_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Cache.STACKER_LIST.get(player.getUniqueId());
    }

    public void setChat(final boolean chat) {
        if (Cache.CHAT_LIST.containsKey(player.getUniqueId())) {
            Cache.CHAT_LIST.remove(player.getUniqueId());
        }

        Cache.CHAT_LIST.put(player.getUniqueId(), chat);
    }

    public boolean hasChat() {
        if (!Cache.CHAT_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Cache.CHAT_LIST.get(player.getUniqueId());
    }

    public void setVanish(final boolean vanish) {
        if (Cache.VANISH_LIST.containsKey(player.getUniqueId())) {
            Cache.VANISH_LIST.remove(player.getUniqueId());
        }

        Cache.VANISH_LIST.put(player.getUniqueId(), vanish);
    }

    public boolean hasVanish() {
        if (!Cache.VANISH_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Cache.VANISH_LIST.get(player.getUniqueId());
    }

    public void setFly(final boolean fly) {
        if (Cache.FLY_LIST.containsKey(player.getUniqueId())) {
            Cache.FLY_LIST.remove(player.getUniqueId());
        }

        Cache.FLY_LIST.put(player.getUniqueId(), fly);
    }

    public boolean hasFly() {
        if (!Cache.FLY_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Cache.FLY_LIST.get(player.getUniqueId());
    }

    public void setSpeed(final boolean speed) {
        if (Cache.SPEED_LIST.containsKey(player.getUniqueId())) {
            Cache.SPEED_LIST.remove(player.getUniqueId());
        }

        Cache.SPEED_LIST.put(player.getUniqueId(), speed);
    }

    public boolean hasSpeed() {
        if (!Cache.SPEED_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Cache.SPEED_LIST.get(player.getUniqueId());
    }

    public void setJump(final boolean jump) {
        if (Cache.JUMP_LIST.containsKey(player.getUniqueId())) {
            Cache.JUMP_LIST.remove(player.getUniqueId());
        }

        Cache.JUMP_LIST.put(player.getUniqueId(), jump);
    }

    public boolean hasJump() {
        if (!Cache.JUMP_LIST.containsKey(player.getUniqueId()))
            return false;
        else
            return Cache.JUMP_LIST.get(player.getUniqueId());
    }

}

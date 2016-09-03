package me.lim_bo56.settings.objects;

import me.lim_bo56.settings.Core;
import me.lim_bo56.settings.mysql.MySqlConnection;
import me.lim_bo56.settings.utils.Variables;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

/**
 * Created by lim_bo56
 * On 8/11/2016
 * At 11:50 AM
 */
public class CustomPlayer {

    private Player player;

    private UUID uuid;

    private String name;

    private MySqlConnection mysql = MySqlConnection.getInstance();

    public CustomPlayer(Player player) {
        this.player = player;
        this.uuid = player.getUniqueId();
        this.name = player.getName();
    }

    public Player getPlayer() {
        return player;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getName() {
        return name;
    }

    public boolean containsPlayer() {
        if (Variables.Sql) {
            try {
                PreparedStatement sql = MySqlConnection.getInstance().getCurrentConnection().prepareStatement(
                        "SELECT UUID FROM `players` WHERE UUID = ?");
                sql.setString(1, getUuid().toString());
                ResultSet resultset = sql.executeQuery();
                boolean containsPlayerUUID = resultset.next();

                sql.close();
                resultset.close();

                return containsPlayerUUID;
            } catch (SQLException exception) {
                exception.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public void addPlayer() {
        if (Variables.Sql) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {
                @Override
                public void run() {
                    try {

                        int visibility = 0, stacker = 0, chat = 0, vanish = 0, fly = 0, speed = 0, jump = 0;

                        if (Variables.defaultVisibility) visibility = 1;

                        if (Variables.defaultStacker) stacker = 1;

                        if (Variables.defaultChat) chat = 1;

                        if (Variables.defaultVanish) vanish = 1;

                        if (Variables.defaultFly) fly = 1;

                        if (Variables.defaultSpeed) speed = 1;

                        if (Variables.defaultJump) jump = 1;

                        PreparedStatement sql = MySqlConnection.getInstance().getCurrentConnection().prepareStatement(
                                "INSERT INTO players (UUID, Visibility, Stacker, Chat, Vanish, Fly, Speed, Jump) VALUES (" +
                                        "`" + getUuid().toString() + "`," +
                                        "`" + visibility + "`," +
                                        "`" + stacker + "`," +
                                        "`" + chat + "`," +
                                        "`" + vanish + "`," +
                                        "`" + fly + "`," +
                                        "`" + speed + "`," +
                                        "`" + jump + "`" +
                                        ");");

                        sql.execute();
                        sql.close();

                    } catch (SQLException exception) {
                        exception.printStackTrace();
                    }
                }
            });
        } else {
            if (Variables.defaultVisibility) Variables.VISIBILITY_LIST.add(player);
            if (Variables.defaultStacker) Variables.STACKER_LIST.add(player);
            if (Variables.defaultChat) Variables.CHAT_LIST.add(player);
            if (Variables.defaultVanish) Variables.VANISH_LIST.add(player);
            if (Variables.defaultFly) Variables.FLY_LIST.add(player);
            if (Variables.defaultSpeed) Variables.SPEED_LIST.add(player);
            if (Variables.defaultJump) Variables.JUMP_LIST.add(player);
        }
    }

    public void setVisibility(final boolean visibility) {
        if (Variables.Sql) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {

                @Override
                public void run() {

                    try {

                        int outcome;

                        if (visibility) outcome = 1;
                        else outcome = 0;

                        PreparedStatement sql = mysql.getCurrentConnection()
                                .prepareStatement("UPDATE `players` SET `Visibility` = " + outcome + " WHERE UUID = '"
                                        + getUuid().toString() + "'");

                        sql.execute();
                        sql.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            if (visibility) {
                Variables.VISIBILITY_LIST.add(player);
            } else {
                Variables.VISIBILITY_LIST.remove(player);
            }
        }
    }

    public boolean hasVisibility() {
        if (Variables.Sql) {
            try {
                ResultSet rs = mysql.getCurrentConnection().createStatement().executeQuery(
                        "SELECT `Visibility` FROM `players` WHERE `UUID` = '" + getUuid().toString() + "'");

                if (rs.next()) {
                    return rs.getBoolean(1);
                }

                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return (Variables.VISIBILITY_LIST.contains(player));
        }
        return false;
    }

    public void setStacker(final boolean stacker) {
        if (Variables.Sql) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {

                @Override
                public void run() {

                    try {

                        int outcome;

                        if (stacker)
                            outcome = 1;
                        else
                            outcome = 0;

                        PreparedStatement sql = mysql.getCurrentConnection()
                                .prepareStatement("UPDATE `players` SET `Stacker` = " + outcome + " WHERE UUID = '"
                                        + getUuid().toString() + "'");

                        sql.execute();
                        sql.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            if (stacker) {
                Variables.STACKER_LIST.add(player);
            } else {
                Variables.STACKER_LIST.remove(player);
            }
        }
    }

    public boolean hasStacker() {
        if (Variables.Sql) {
            try {
                ResultSet rs = mysql.getCurrentConnection().createStatement().executeQuery(
                        "SELECT `Stacker` FROM `players` WHERE `UUID` = '" + getUuid().toString() + "'");

                if (rs.next()) {
                    return rs.getBoolean(1);
                }

                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return Variables.STACKER_LIST.contains(player);
        }
        return false;
    }

    public void setChat(final boolean chat) {
        if (Variables.Sql) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {

                @Override
                public void run() {

                    try {

                        int outcome;

                        if (chat)
                            outcome = 1;
                        else
                            outcome = 0;

                        PreparedStatement sql = mysql.getCurrentConnection()
                                .prepareStatement("UPDATE `players` SET `Chat` = " + outcome + " WHERE UUID = '"
                                        + getUuid().toString() + "'");

                        sql.execute();
                        sql.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            if (chat) {
                Variables.CHAT_LIST.add(player);
            } else {
                Variables.CHAT_LIST.remove(player);
            }
        }
    }

    public boolean hasChat() {
        if (Variables.Sql) {
            try {
                ResultSet rs = mysql.getCurrentConnection().createStatement().executeQuery(
                        "SELECT `Chat` FROM `players` WHERE `UUID` = '" + getUuid().toString() + "'");

                if (rs.next()) {
                    return rs.getBoolean(1);
                }

                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return Variables.CHAT_LIST.contains(player);
        }
        return false;
    }

    public void setVanish(final boolean vanish) {
        if (Variables.Sql) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {

                @Override
                public void run() {

                    try {

                        int outcome;

                        if (vanish)
                            outcome = 1;
                        else
                            outcome = 0;

                        PreparedStatement sql = mysql.getCurrentConnection()
                                .prepareStatement("UPDATE `players` SET `Vanish` = " + outcome + " WHERE UUID = '"
                                        + getUuid().toString() + "'");

                        sql.execute();
                        sql.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            if (vanish) {
                Variables.VANISH_LIST.add(player);
            } else {
                Variables.VANISH_LIST.remove(player);
            }
        }
    }

    public boolean hasVanish() {
        if (Variables.Sql) {
            try {
                ResultSet rs = mysql.getCurrentConnection().createStatement().executeQuery(
                        "SELECT `Vanish` FROM `players` WHERE `UUID` = '" + getUuid().toString() + "'");
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return Variables.VANISH_LIST.contains(player);
        }
        return false;
    }

    public void setFly(final boolean fly) {
        if (Variables.Sql) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {

                @Override
                public void run() {
                    try {

                        int outcome;

                        if (fly)
                            outcome = 1;
                        else
                            outcome = 0;

                        PreparedStatement sql = mysql.getCurrentConnection()
                                .prepareStatement("UPDATE `players` SET `Fly` = " + outcome + " WHERE UUID = '"
                                        + getUuid().toString() + "'");

                        sql.execute();
                        sql.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            if (fly) {
                Variables.FLY_LIST.add(player);
            } else {
                Variables.FLY_LIST.remove(player);
            }
        }
    }

    public boolean hasFly() {
        if (Variables.Sql) {
            try {
                ResultSet rs = mysql.getCurrentConnection().createStatement().executeQuery(
                        "SELECT `Fly` FROM `players` WHERE `UUID` = '" + getUuid().toString() + "'");
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return Variables.FLY_LIST.contains(player);
        }
        return false;
    }

    public void setSpeed(final boolean speed) {
        if (Variables.Sql) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {

                @Override
                public void run() {

                    try {

                        int outcome;

                        if (speed)
                            outcome = 1;
                        else
                            outcome = 0;

                        PreparedStatement sql = mysql.getCurrentConnection()
                                .prepareStatement("UPDATE `players` SET `Speed` = " + outcome + " WHERE UUID = '"
                                        + getUuid().toString() + "'");

                        sql.execute();
                        sql.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            if (speed) {
                Variables.SPEED_LIST.add(player);
            } else {
                Variables.SPEED_LIST.remove(player);
            }
        }
    }

    public boolean hasSpeed() {
        if (Variables.Sql) {
            try {
                ResultSet rs = mysql.getCurrentConnection().createStatement().executeQuery(
                        "SELECT `Speed` FROM `players` WHERE `UUID` = '" + getUuid().toString() + "'");
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return Variables.SPEED_LIST.contains(player);
        }
        return false;
    }

    public void setJump(final boolean jump) {
        if (Variables.Sql) {
            Bukkit.getScheduler().runTaskAsynchronously(Core.getInstance(), new Runnable() {

                @Override
                public void run() {

                    try {

                        int outcome;

                        if (jump)
                            outcome = 1;
                        else
                            outcome = 0;

                        PreparedStatement sql = mysql.getCurrentConnection()
                                .prepareStatement("UPDATE `players` SET `Jump` = " + outcome + " WHERE UUID = '"
                                        + getUuid().toString() + "'");

                        sql.execute();
                        sql.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }

            });
        } else {
            if (jump) {
                Variables.JUMP_LIST.add(player);
            } else {
                Variables.JUMP_LIST.remove(player);
            }
        }
    }

    public boolean hasJump() {
        if (Variables.Sql) {
            try {
                ResultSet rs = mysql.getCurrentConnection().createStatement().executeQuery(
                        "SELECT `Jump` FROM `players` WHERE `UUID` = '" + getUuid().toString() + "'");
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
                rs.close();

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        } else {
            return Variables.JUMP_LIST.contains(player);
        }
        return false;
    }

}

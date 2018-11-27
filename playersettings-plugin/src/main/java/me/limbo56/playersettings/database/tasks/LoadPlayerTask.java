package me.limbo56.playersettings.database.tasks;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.player.SPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadPlayerTask extends DatabaseTask {
    private SPlayer sPlayer;

    public LoadPlayerTask(Connection connection, SPlayer sPlayer) {
        super(connection);
        this.sPlayer = sPlayer;
    }

    @Override
    public void run() throws SQLException {
        String loadPlayerQuery = "SELECT * FROM player_settings WHERE uuid=?";

        PreparedStatement loadPlayerStatement = getConnection().prepareStatement(loadPlayerQuery);
        loadPlayerStatement.setString(1, sPlayer.getUuid().toString());

        ResultSet resultSet = loadPlayerStatement.executeQuery();

        while (resultSet.next()) {
            String settingName = resultSet.getString("settingName");
            boolean enabled = resultSet.getBoolean("enabled");

            Setting setting = sPlayer.getSetting(settingName);
            setting.getSettingWatcher().setEnabled(enabled);
        }
    }
}

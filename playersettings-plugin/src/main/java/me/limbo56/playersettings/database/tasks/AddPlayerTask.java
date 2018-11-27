package me.limbo56.playersettings.database.tasks;

import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.database.exceptions.PlayerUpdateException;
import me.limbo56.playersettings.player.SPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddPlayerTask extends DatabaseTask {
    private SPlayer player;

    public AddPlayerTask(Connection connection, SPlayer player) {
        super(connection);
        this.player = player;
    }

    @Override
    public void run() throws SQLException {
        String addPlayerQuery = "INSERT IGNORE INTO players (uuid) VALUES (?)";

        // Add the player
        PreparedStatement addPlayerStatement = getConnection().prepareStatement(addPlayerQuery);
        addPlayerStatement.setString(1, player.getUuid().toString());
        addPlayerStatement.execute();
        addPlayerStatement.close();

        // Add the settings for the player
        player.getSettingStore().getStored().forEach(this::addSetting);
    }

    private void addSetting(String name, Setting setting) {
        String addSettingsQuery = "INSERT IGNORE INTO player_settings (uuid, settingName, value) VALUES (?, ?, ?)";

        try {
            PreparedStatement addSettingStatement = getConnection().prepareStatement(addSettingsQuery);
            addSettingStatement.setString(1, player.getUuid().toString());
            addSettingStatement.setString(2, name);
            addSettingStatement.setBoolean(3, setting.getSettingWatcher().isEnabled());
            addSettingStatement.execute();
            addSettingStatement.close();
        } catch (SQLException exception) {
            PlayerUpdateException playerUpdateException = new PlayerUpdateException("Could not add player " + player.getPlayer().getName());
            playerUpdateException.initCause(exception);
            playerUpdateException.printStackTrace();
        }
    }
}

package me.limbo56.playersettings.database.tasks;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.settings.SPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

public class SavePlayerTask extends DatabaseTask {
    private static final String SAVE_QUERY = "INSERT INTO player_settings (owner, settingName, value)" +
            "VALUES (?, ?, ?)" +
            "ON DUPLICATE KEY " +
            "UPDATE value = VALUES(value)";
    private SPlayer player;

    public SavePlayerTask(PlayerSettings plugin, Connection connection, SPlayer player) {
        super(plugin, connection);
        this.player = player;
    }

    @Override
    public void run() {
        try (Connection connection = getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SAVE_QUERY);
            for (Map.Entry<String, Setting> entry : getPlugin().getSettingsRegistry().getStored().entrySet()) {
                String rawName = entry.getKey();
                Setting setting = entry.getValue();

                statement.setString(1, player.getUuid().toString());
                statement.setString(2, rawName);
                statement.setInt(3, player.getSettingWatcher().getValue(setting));
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            getPlugin().getLogger().severe("Could not save player " + player.getPlayer().getName());
            e.printStackTrace();
        }
    }
}

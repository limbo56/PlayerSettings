package me.limbo56.playersettings.database.tasks;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.settings.ConfigurationSetting;
import me.limbo56.playersettings.settings.SPlayer;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoadPlayerTask extends DatabaseTask {
    private static final String LOAD_QUERY = "SELECT * FROM player_settings WHERE owner=?";
    private SPlayer sPlayer;

    public LoadPlayerTask(PlayerSettings plugin, Connection connection, SPlayer sPlayer) {
        super(plugin, connection);
        this.sPlayer = sPlayer;
    }

    @Override
    public void run() {
        Player player = sPlayer.getPlayer();

        try (Connection connection = getConnection()) {
            PreparedStatement loadStatement = connection.prepareStatement(LOAD_QUERY);
            loadStatement.setString(1, player.getUniqueId().toString());

            ResultSet resultSet = loadStatement.executeQuery();
            while (resultSet.next()) {
                String settingName = resultSet.getString("settingName");
                int value = resultSet.getInt("value");

                Setting setting = getPlugin().getSetting(settingName);
                sPlayer.getSettingWatcher().setValue(setting, value, !new ConfigurationSetting(settingName).getExecuteOnJoin());
            }
        } catch (SQLException e) {
            getPlugin().getLogger().severe("Failed to load settings for player " + player.getName());
            e.printStackTrace();
        }
    }
}

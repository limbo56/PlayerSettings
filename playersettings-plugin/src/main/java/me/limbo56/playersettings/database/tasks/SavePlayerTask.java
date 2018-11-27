package me.limbo56.playersettings.database.tasks;

import me.limbo56.playersettings.database.exceptions.PlayerUpdateException;
import me.limbo56.playersettings.player.SPlayer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SavePlayerTask extends DatabaseTask {
    private SPlayer player;

    public SavePlayerTask(Connection connection, SPlayer player) {
        super(connection);
        this.player = player;
    }

    @Override
    public void run() {
        String saveQuery = "UPDATE player_settings SET value=? WHERE uuid=? AND settingName=?";

        player.getSettingStore().getStored().forEach((name, setting) -> {
            try {
                PreparedStatement statement = getConnection().prepareStatement(saveQuery);

                statement.setBoolean(1, setting.getSettingWatcher().isEnabled());
                statement.setString(2, player.getUuid().toString());
                statement.setString(3, setting.getRawName());

                statement.execute();
                statement.close();
            } catch (SQLException exception) {
                PlayerUpdateException playerUpdateException = new PlayerUpdateException("Could not save player " + player.getPlayer().getName());
                playerUpdateException.initCause(exception);
                playerUpdateException.printStackTrace();
            }
        });
    }
}

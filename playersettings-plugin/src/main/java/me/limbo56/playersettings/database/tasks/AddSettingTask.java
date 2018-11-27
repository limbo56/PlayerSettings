package me.limbo56.playersettings.database.tasks;

import me.limbo56.playersettings.api.Setting;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddSettingTask extends DatabaseTask {
    private Setting setting;

    public AddSettingTask(Connection connection, Setting setting) {
        super(connection);
        this.setting = setting;
    }

    @Override
    public void run() throws SQLException {
        String addSettingQuery = "INSERT IGNORE INTO settings (settingName) VALUES (?)";

        PreparedStatement addSettingStatement = getConnection().prepareStatement(addSettingQuery);
        addSettingStatement.setString(1, setting.getRawName());
        addSettingStatement.execute();
        addSettingStatement.close();
    }
}

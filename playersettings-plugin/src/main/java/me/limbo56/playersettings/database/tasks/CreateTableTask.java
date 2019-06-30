package me.limbo56.playersettings.database.tasks;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.utils.database.Table;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTableTask extends DatabaseTask {
    private Table table;

    public CreateTableTask(PlayerSettings plugin, Connection connection, Table table) {
        super(plugin, connection);
        this.table = table;
    }

    @Override
    public void run() {
        try (PreparedStatement preparedStatement = getConnection().prepareStatement(table.toString())) {
            preparedStatement.execute();
        } catch (SQLException e) {
            getPlugin().getLogger().severe("Could not create table");
            e.printStackTrace();
        }
    }
}

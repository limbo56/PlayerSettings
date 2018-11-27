package me.limbo56.playersettings.database.tasks;

import me.limbo56.playersettings.utils.database.DatabaseTable;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CreateTableTask extends DatabaseTask {
    private DatabaseTable databaseTable;

    public CreateTableTask(Connection connection, DatabaseTable databaseTable) {
        super(connection);
        this.databaseTable = databaseTable;
    }

    @Override
    public void run() throws SQLException {
        PreparedStatement preparedStatement = getConnection().prepareStatement(databaseTable.toString());
        preparedStatement.execute();
        preparedStatement.close();
    }
}

package me.limbo56.playersettings.database.exceptions;

import java.sql.SQLException;

public class DatabaseUpdateException extends SQLException {
    public DatabaseUpdateException(String outcome) {
        super(outcome);
    }
}

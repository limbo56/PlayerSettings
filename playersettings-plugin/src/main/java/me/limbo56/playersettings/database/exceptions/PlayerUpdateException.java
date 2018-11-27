package me.limbo56.playersettings.database.exceptions;

import java.sql.SQLException;

public class PlayerUpdateException extends SQLException {
    public PlayerUpdateException(String outcome) {
        super(outcome);
    }
}

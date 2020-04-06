package me.limbo56.playersettings.database.tables;

import me.limbo56.playersettings.utils.database.Field;
import me.limbo56.playersettings.utils.database.Table;
import me.limbo56.playersettings.utils.database.constraints.NotNullConstraint;
import me.limbo56.playersettings.utils.database.constraints.PrimaryKeyConstraint;

import java.sql.JDBCType;
import java.util.function.Supplier;

public class PlayerSettingsTable implements Supplier<Table> {
    @Override
    public Table get() {
        // Define constraints
        PrimaryKeyConstraint ownerPrimary = new PrimaryKeyConstraint("owner", "settingName");

        // Define fields
        Field owner = new Field("owner", JDBCType.VARCHAR, "255");
        Field setting = new Field("settingName", JDBCType.VARCHAR, "255", new NotNullConstraint());
        Field value = new Field("value", JDBCType.INTEGER, new NotNullConstraint());

        // Add fields to table
        Table playerSettingsTable = new Table("player_settings");
        playerSettingsTable.addConstraint(ownerPrimary);
        playerSettingsTable.addField(owner);
        playerSettingsTable.addField(setting);
        playerSettingsTable.addField(value);
        return playerSettingsTable;
    }
}

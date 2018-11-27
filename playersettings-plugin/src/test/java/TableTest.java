import me.limbo56.playersettings.utils.database.DatabaseField;
import me.limbo56.playersettings.utils.database.DatabaseTable;
import me.limbo56.playersettings.utils.database.constraints.ForeignKeyConstraint;
import me.limbo56.playersettings.utils.database.constraints.NotNullConstraint;
import org.junit.Test;

import java.sql.JDBCType;

import static org.junit.Assert.assertEquals;

public class TableTest {
    @Test
    public void testCreateDatabase() {
        String expectedQuery = "CREATE TABLE IF NOT EXISTS player_settings (" +
                "`uuid` VARCHAR(255) NOT NULL," +
                " " +
                "`settingName` VARCHAR(255) NOT NULL," +
                " " +
                "`value` BOOLEAN NOT NULL," + " " +
                "FOREIGN KEY (`uuid`) REFERENCES `players`(`uuid`)," +
                " " +
                "FOREIGN KEY (`settingName`) REFERENCES `settings`(`settingName`));";

        // Create table
        DatabaseTable playerSettingsTable = new DatabaseTable("player_settings");

        // Create constraints
        NotNullConstraint notNullConstraint = new NotNullConstraint();
        ForeignKeyConstraint uuidFieldConstraint = new ForeignKeyConstraint("uuid", "players", "uuid");
        ForeignKeyConstraint settingNameFieldConstraint = new ForeignKeyConstraint("settingName", "settings", "settingName");

        // Create fields
        DatabaseField uuidField = new DatabaseField("uuid", JDBCType.VARCHAR, "255", notNullConstraint);
        DatabaseField settingNameField = new DatabaseField("settingName", JDBCType.VARCHAR, "255", notNullConstraint);
        DatabaseField valueField = new DatabaseField("value", JDBCType.BOOLEAN, notNullConstraint);

        // Add table constraints
        playerSettingsTable.addConstraint(uuidFieldConstraint);
        playerSettingsTable.addConstraint(settingNameFieldConstraint);

        // Add table fields
        playerSettingsTable.addField(uuidField);
        playerSettingsTable.addField(settingNameField);
        playerSettingsTable.addField(valueField);

        String actualQuery = playerSettingsTable.toString();

        assertEquals(expectedQuery, actualQuery);
    }
}

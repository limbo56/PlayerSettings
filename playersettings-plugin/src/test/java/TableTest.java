import me.limbo56.playersettings.utils.database.Field;
import me.limbo56.playersettings.utils.database.Table;
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
                "`value` INTEGER NOT NULL," + " " +
                "FOREIGN KEY (`uuid`) REFERENCES `players`(`uuid`)," +
                " " +
                "FOREIGN KEY (`settingName`) REFERENCES `defined`(`settingName`));";

        // Create table
        Table playerSettingsTable = new Table("player_settings");

        // Create constraints
        NotNullConstraint notNullConstraint = new NotNullConstraint();
        ForeignKeyConstraint uuidFieldConstraint = new ForeignKeyConstraint("uuid", "players", "uuid");
        ForeignKeyConstraint settingNameFieldConstraint = new ForeignKeyConstraint("settingName", "defined", "settingName");

        // Create fields
        Field uuidField = new Field("uuid", JDBCType.VARCHAR, "255", notNullConstraint);
        Field settingNameField = new Field("settingName", JDBCType.VARCHAR, "255", notNullConstraint);
        Field valueField = new Field("value", JDBCType.INTEGER, notNullConstraint);

        // Add table constraints
        playerSettingsTable.addConstraint(uuidFieldConstraint);
        playerSettingsTable.addConstraint(settingNameFieldConstraint);

        // Add table fields
        playerSettingsTable.addField(uuidField);
        playerSettingsTable.addField(settingNameField);
        playerSettingsTable.addField(valueField);

        assertEquals(expectedQuery, playerSettingsTable.toString());
    }
}

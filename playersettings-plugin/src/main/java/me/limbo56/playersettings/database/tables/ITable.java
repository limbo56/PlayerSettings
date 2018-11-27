package me.limbo56.playersettings.database.tables;

import me.limbo56.playersettings.utils.database.DatabaseTable;

public interface ITable {
    /**
     * Returns a table provided by the implementing class
     *
     * @return Table
     */
    DatabaseTable getTable();
}

package me.limbo56.playersettings.utils.database;

import me.limbo56.playersettings.utils.database.constraints.TableConstraint;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTable {
    private String name;
    private List<TableConstraint> tableConstraints = new ArrayList<>();
    private List<DatabaseField> databaseFields = new ArrayList<>();

    public DatabaseTable(String name) {
        this.name = name;
    }

    public void addConstraint(TableConstraint tableConstraint) {
        tableConstraints.add(tableConstraint);
    }

    public void addField(DatabaseField databaseField) {
        databaseFields.add(databaseField);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CREATE TABLE IF NOT EXISTS " +
                name + " " +
                "(" +
                StringUtils.join(databaseFields, ", ") +
                ", " +
                StringUtils.join(tableConstraints, ", ") +
                ");";
    }
}

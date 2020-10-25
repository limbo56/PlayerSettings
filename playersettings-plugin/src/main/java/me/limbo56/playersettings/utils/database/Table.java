package me.limbo56.playersettings.utils.database;

import me.limbo56.playersettings.utils.database.constraints.TableConstraint;
import org.apache.commons.lang.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Table {

    private final String name;
    private final List<TableConstraint> tableConstraints = new ArrayList<>();
    private final List<Field> fields = new ArrayList<>();

    public Table(String name) {
        this.name = name;
    }

    public void addConstraint(TableConstraint tableConstraint) {
        tableConstraints.add(tableConstraint);
    }

    public void addField(Field field) {
        fields.add(field);
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "CREATE TABLE IF NOT EXISTS " +
                name + " " +
                "(" +
                StringUtils.join(fields, ", ") +
                ", " +
                StringUtils.join(tableConstraints, ", ") +
                ");";
    }
}

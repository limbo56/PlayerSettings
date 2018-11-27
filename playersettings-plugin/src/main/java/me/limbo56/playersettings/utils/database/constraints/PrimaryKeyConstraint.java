package me.limbo56.playersettings.utils.database.constraints;

public class PrimaryKeyConstraint implements TableConstraint {
    private String columnName;

    public PrimaryKeyConstraint(String columnName) {
        this.columnName = columnName;
    }

    @Override
    public String toString() {
        return String.format("PRIMARY KEY (`%s`)", columnName);
    }
}

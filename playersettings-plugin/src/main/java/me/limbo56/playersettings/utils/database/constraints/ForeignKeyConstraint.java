package me.limbo56.playersettings.utils.database.constraints;

public class ForeignKeyConstraint implements TableConstraint {
    private String columnName;
    private String referenceTable;
    private String referenceColumn;

    public ForeignKeyConstraint(String columnName, String referenceTable, String referenceColumn) {
        this.columnName = columnName;
        this.referenceTable = referenceTable;
        this.referenceColumn = referenceColumn;
    }

    public String getColumnName() {
        return columnName;
    }

    public String getReferenceTable() {
        return referenceTable;
    }

    public String getReferenceColumn() {
        return referenceColumn;
    }

    @Override
    public String toString() {
        return String.format("FOREIGN KEY (`%s`) REFERENCES `%s`(`%s`)", columnName, referenceTable, referenceColumn);
    }
}

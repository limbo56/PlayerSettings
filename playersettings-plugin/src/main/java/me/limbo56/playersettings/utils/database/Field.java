package me.limbo56.playersettings.utils.database;

import me.limbo56.playersettings.utils.database.constraints.FieldConstraint;
import org.apache.commons.lang.StringUtils;

import java.sql.JDBCType;
import java.util.Arrays;
import java.util.List;

public class Field {

    private final String name;
    private final JDBCType type;
    private final String typeData;
    private final List<FieldConstraint> fieldConstraints;

    public Field(String name, JDBCType type) {
        this(name, type, "");
    }

    public Field(String name, JDBCType type, String typeData) {
        this(name, type, typeData, new FieldConstraint[0]);
    }

    public Field(String name, JDBCType type, FieldConstraint... fieldConstraints) {
        this(name, type, "", fieldConstraints);
    }

    public Field(String name, JDBCType type, String typeData, FieldConstraint... fieldConstraints) {
        this.name = name;
        this.type = type;
        this.typeData = typeData;
        this.fieldConstraints = Arrays.asList(fieldConstraints);
    }

    public String getName() {
        return name;
    }

    public String getTypeData() {
        return typeData;
    }

    public List<FieldConstraint> getFieldConstraints() {
        return fieldConstraints;
    }

    @Override
    public String toString() {
        boolean hasTypeData = typeData.length() > 0;
        String name = this.name;
        String typeName = type.getName();
        String typeData = hasTypeData ? "(" + getTypeData() + ")" : "";
        String constraintString = StringUtils.join(fieldConstraints, " ");

        return String.format("`%s` %s%s %s", name, typeName, typeData, constraintString);
    }
}

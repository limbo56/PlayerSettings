package me.limbo56.playersettings.utils.database.constraints;

public class NotNullConstraint implements FieldConstraint {
    @Override
    public String toString() {
        return "NOT NULL";
    }
}

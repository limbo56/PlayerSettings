package me.limbo56.playersettings.database.tasks;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.limbo56.playersettings.PlayerSettings;

import java.sql.Connection;

@AllArgsConstructor
@Getter
public abstract class DatabaseTask implements Runnable {
    private PlayerSettings plugin;
    private Connection connection;
}

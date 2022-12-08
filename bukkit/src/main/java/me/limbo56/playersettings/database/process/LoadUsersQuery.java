package me.limbo56.playersettings.database.process;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.api.setting.Setting;
import me.limbo56.playersettings.api.setting.SettingWatcher;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

public class LoadUsersQuery implements DatabaseQuery<Collection<SettingWatcher>> {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private static final String LOAD_QUERY = "SELECT * FROM playersettings_settings WHERE owner=?";
  private final Connection connection;
  private final Collection<UUID> uuids;

  public LoadUsersQuery(Connection connection, Collection<UUID> uuids) {
    this.connection = connection;
    this.uuids = uuids;
  }

  @Override
  public Collection<SettingWatcher> query() throws SQLException {
    Collection<SettingWatcher> settingWatcherList = new ArrayList<>();
    for (UUID uuid : uuids) {
      SettingWatcher settingWatcher = PLUGIN.getUserManager().getSettingWatcher(uuid);
      loadDatabaseSettings(settingWatcher);
      loadNewSettings(settingWatcher);
      settingWatcherList.add(settingWatcher);
    }
    return settingWatcherList;
  }

  private void loadDatabaseSettings(SettingWatcher settingWatcher) throws SQLException {
    UUID uuid = settingWatcher.getOwner();
    PreparedStatement loadStatement = connection.prepareStatement(LOAD_QUERY);
    loadStatement.setString(1, uuid.toString());

    ResultSet resultSet = loadStatement.executeQuery();
    while (resultSet.next()) {
      String settingName = resultSet.getString("settingName");
      int value = resultSet.getInt("value");

      // Ignore setting if it's not loaded/enabled
      Setting setting = PLUGIN.getSettingsManager().getSetting(settingName);
      if (setting == null) {
        continue;
      }

      int safeValue = getSafeValue(Bukkit.getPlayer(uuid), value, setting);
      settingWatcher.setValue(settingName, safeValue, isSilent(setting));
    }
  }

  private void loadNewSettings(SettingWatcher settingWatcher) {
    List<String> watchedSettings = Arrays.asList(settingWatcher.getWatched());
    for (Setting setting : PLUGIN.getSettingsManager().getSettingMap().values()) {
      // Ignore setting if it's already being watched
      if (watchedSettings.contains(setting.getName())) {
        continue;
      }

      settingWatcher.setValue(setting.getName(), setting.getDefaultValue(), isSilent(setting));
    }
  }

  private int getSafeValue(Player player, int value, Setting setting) {
    int maxValue = PlayerSettingsProvider.getSettingPermissionLevel(player, setting);
    return Math.abs(value) > maxValue ? setting.getDefaultValue() : value;
  }

  private boolean isSilent(Setting setting) {
    return Arrays.stream(setting.getTriggers()).noneMatch(trigger -> trigger.equals("join"));
  }
}

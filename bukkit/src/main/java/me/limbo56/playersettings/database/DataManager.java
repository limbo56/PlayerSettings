package me.limbo56.playersettings.database;

import java.util.Collection;
import me.limbo56.playersettings.user.SettingUser;

public interface DataManager {
  void connect();

  void disconnect();

  void createDefaultTable();

  void loadUsers(Collection<SettingUser> users);

  void saveUsers(Collection<SettingUser> users);
}

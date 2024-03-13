package me.limbo56.playersettings.user.task;

import java.util.Map;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.menu.SettingsMenuManager;
import me.limbo56.playersettings.user.UserManager;
import me.limbo56.playersettings.util.TaskChain;

public class UnloadUserTask implements TaskChain.Task {
  private final UUID uuid;
  private final SettingsMenuManager settingsMenuManager;
  private final UserManager userManager;

  public UnloadUserTask(UUID uuid) {
    PlayerSettings plugin = PlayerSettings.getInstance();
    this.uuid = uuid;
    this.settingsMenuManager = plugin.getSettingsMenuManager();
    this.userManager = plugin.getUserManager();
  }

  @Override
  public void accept(Map<String, Object> data) {
    settingsMenuManager.unload(uuid);
    userManager.saveUser(uuid);
  }
}

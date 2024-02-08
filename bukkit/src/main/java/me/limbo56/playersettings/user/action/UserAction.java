package me.limbo56.playersettings.user.action;

import java.util.function.Consumer;
import me.limbo56.playersettings.user.SettingUser;

public interface UserAction extends Consumer<SettingUser> {
  boolean shouldExecute();
}

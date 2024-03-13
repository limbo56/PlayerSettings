package me.limbo56.playersettings.hook;

import me.limbo56.playersettings.PlayerSettings;

public abstract class Hook {
  protected final PlayerSettings plugin;

  protected Hook(PlayerSettings plugin) {
    this.plugin = plugin;
  }

  protected abstract void register();

  public boolean shouldRegister() {
    return true;
  }
}

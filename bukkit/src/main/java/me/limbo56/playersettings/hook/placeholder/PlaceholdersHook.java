package me.limbo56.playersettings.hook.placeholder;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.hook.Hook;
import org.bukkit.Bukkit;

public class PlaceholdersHook extends Hook {
  public PlaceholdersHook(PlayerSettings plugin) {
    super(plugin);
  }

  @Override
  protected void register() {
    new PlayerSettingsPlaceholders(plugin).register();
  }

  @Override
  public boolean shouldRegister() {
    return Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null;
  }
}

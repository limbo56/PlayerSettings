package me.limbo56.playersettings.hook;

import me.limbo56.playersettings.PlayerSettings;
import org.bstats.bukkit.Metrics;

public class MetricsHook extends Hook {
  public MetricsHook(PlayerSettings plugin) {
    super(plugin);
  }

  @Override
  public void register() {
    new Metrics(plugin, 16730);
  }

  @Override
  public boolean shouldRegister() {
    return plugin.getConfiguration().hasMetricsEnabled();
  }
}

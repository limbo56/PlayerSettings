package me.limbo56.playersettings.configuration;

import org.jetbrains.annotations.NotNull;

public class PluginConfiguration extends BaseConfiguration {
  @Override
  @NotNull
  String getFileName() {
    return "config.yml";
  }
}

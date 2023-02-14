package me.limbo56.playersettings.configuration;

import org.jetbrains.annotations.NotNull;

public class MessagesConfiguration extends BaseConfiguration {
  @Override
  @NotNull
  String getFileName() {
    return "messages.yml";
  }

  public String getMessagePrefix() {
    return getFile().getString("prefix", "");
  }
}

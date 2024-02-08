package me.limbo56.playersettings.configuration;

import java.util.Collections;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import org.jetbrains.annotations.NotNull;

public class MessagesConfiguration extends BaseConfiguration {
  public MessagesConfiguration(PlayerSettings plugin) {
    super(plugin);
  }

  @Override
  @NotNull
  String getFileName() {
    return "messages.yml";
  }

  public boolean hasMessage(String path) {
    return configuration.contains(path);
  }

  public String getMessagePrefix() {
    return getMessage("prefix");
  }

  public String getMessage(String path) {
    return getMessage(path, "");
  }

  public List<String> getMessageList(String path) {
    return getMessageList(path, Collections.emptyList());
  }

  public String getMessage(String path, String defaultMessage) {
    return configuration.getString(path, defaultMessage);
  }

  public List<String> getMessageList(String path, List<String> defaultMessage) {
    if (hasMessage(path)) {
      return configuration.getStringList(path);
    } else {
      return defaultMessage;
    }
  }
}

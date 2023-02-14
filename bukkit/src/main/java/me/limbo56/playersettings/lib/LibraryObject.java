package me.limbo56.playersettings.lib;

import java.util.Map;

public interface LibraryObject {
  String getGroupId();

  String getArtifactId();

  String getVersion();

  String getId();

  Map<String, String> getRelocations();
}

package me.limbo56.playersettings.util;

import static me.limbo56.playersettings.PlayerSettingsProvider.getPlugin;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Permissions {
  public static int getPermissionLevel(CommandSender sender, String permission, int defaultLevel) {
    // Give max level if sender is operator or has the 'settings.*' permission
    String permissionPath = permission + ".";
    if (sender.isOp() || sender.hasPermission(permissionPath + "*")) {
      return Integer.MAX_VALUE;
    }

    try {
      List<String> permissions = getSenderPermissions(sender);
      List<String> levels = getPermissionLevels(permissionPath, permissions);
      return getMaxPermissionLevel(levels, defaultLevel);
    } catch (NumberFormatException | NullPointerException e) {
      getPlugin()
          .getLogger()
          .warning("Invalid permission '" + permissionPath + "' for " + sender.getName());
      e.printStackTrace();
      return defaultLevel;
    }
  }

  private static List<String> getSenderPermissions(CommandSender sender) {
    return sender.getEffectivePermissions().stream()
        .map(PermissionAttachmentInfo::getPermission)
        .collect(Collectors.toList());
  }

  private static List<String> getPermissionLevels(String permission, List<String> permissions) {
    return permissions.stream()
        .filter(listPermission -> listPermission.startsWith(permission))
        .map(listPermission -> listPermission.substring(permission.length()))
        .collect(Collectors.toList());
  }

  private static int getMaxPermissionLevel(List<String> levels, int defaultLevel)
      throws NumberFormatException {
    return levels.stream()
        .map(PlayerSettingsProvider::parseSettingValue)
        .filter(Objects::nonNull)
        .max(Integer::compareTo)
        .orElse(defaultLevel);
  }
}

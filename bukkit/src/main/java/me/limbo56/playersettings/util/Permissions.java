package me.limbo56.playersettings.util;

import me.limbo56.playersettings.api.setting.Setting;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;

import java.util.List;
import java.util.stream.Collectors;

import static me.limbo56.playersettings.PlayerSettingsProvider.getPlugin;

public class Permissions {
  public static int getSettingPermissionLevel(CommandSender sender, Setting setting) {
    String permission = "playersettings." + setting.getName().toLowerCase();
    return getPermissionLevel(sender, permission, setting.getDefaultValue());
  }

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
    return levels.stream().map(Integer::parseInt).max(Integer::compareTo).orElse(defaultLevel);
  }
}

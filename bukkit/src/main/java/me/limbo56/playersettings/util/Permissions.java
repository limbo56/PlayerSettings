package me.limbo56.playersettings.util;

import java.util.ArrayList;
import java.util.List;
import me.limbo56.playersettings.api.Setting;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.PermissionAttachmentInfo;

public class Permissions {
  private Permissions() {}

  public static int getSettingPermissionLevel(CommandSender sender, Setting setting) {
    String permission = "playersettings." + setting.getName().toLowerCase();
    return getPermissionLevel(sender, permission, setting.getDefaultValue());
  }

  public static int getPermissionLevel(CommandSender sender, String permission, int defaultLevel) {
    // Give max level if sender is operator or has the 'settings.*' permission
    if (sender.isOp() || sender.hasPermission(permission + ".*")) {
      return Integer.MAX_VALUE;
    }

    try {
      List<Integer> levels = getPermissionLevels(sender, permission);
      return getMaxPermissionLevel(levels, defaultLevel);
    } catch (NumberFormatException exception) {
      PluginLogger.severe(
          "Invalid permission '" + permission + "' for " + sender.getName(), exception);
      return defaultLevel;
    }
  }

  private static List<Integer> getPermissionLevels(CommandSender sender, String permission) {
    List<String> permissions = getSenderPermissions(sender);
    List<Integer> levels = new ArrayList<>();
    for (String p : permissions) {
      if (p.startsWith(permission + ".")) {
        Integer parseInt = Integer.parseInt(p.substring(permission.length() + 1));
        levels.add(parseInt);
      }
    }
    return levels;
  }

  private static List<String> getSenderPermissions(CommandSender sender) {
    List<String> permissions = new ArrayList<>();
    for (PermissionAttachmentInfo permissionAttachmentInfo : sender.getEffectivePermissions()) {
      String permission = permissionAttachmentInfo.getPermission();
      permissions.add(permission);
    }
    return permissions;
  }

  private static int getMaxPermissionLevel(List<Integer> levels, int defaultLevel) {
    boolean seen = false;
    Integer best = null;
    for (Integer level : levels) {
      if (!seen || level.compareTo(best) > 0) {
        seen = true;
        best = level;
      }
    }
    return seen ? best : defaultLevel;
  }
}

package me.limbo56.playersettings.util;

import java.util.Collection;
import java.util.stream.Collectors;
import me.limbo56.playersettings.hook.AdventureHook;
import me.limbo56.playersettings.message.text.Text;
import me.limbo56.playersettings.user.SettingUser;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Players {
  private Players() {}

  public static Collection<Player> filterOnlineUsers(Collection<SettingUser> users) {
    return filterOnline(users.stream().map(SettingUser::getPlayer).collect(Collectors.toList()));
  }

  public static Collection<Player> filterOnline(Collection<Player> players) {
    return players.stream()
        .filter(player -> player != null && player.isOnline())
        .collect(Collectors.toList());
  }

  public static void sendMessage(CommandSender sender, String text) {
    sendMessage(sender, Text.create(text));
  }

  public static void sendMessage(Player player, Component component) {
    AdventureHook.adventure().player(player).sendMessage(component);
  }

  public static void sendMessage(CommandSender sender, Text text) {
    sendMessage(sender, text, "");
  }

  public static void sendMessage(CommandSender sender, Text text, String delimiter) {
    String message = String.join(delimiter, text.getTextLines());
    if (!message.isEmpty()) {
      sender.sendMessage(message);
    }
  }

  public static void sendVersionMessage(Player player) {
    PluginUpdater.getVersionMessage()
        .thenAcceptAsync(
            message ->
                Players.sendMessage(player, PluginUpdater.PREFIX + String.join("\n", message)))
        .exceptionally(
            exception -> {
              PluginLogger.severe(
                  "An exception occurred while sending the version message", exception);
              return null;
            });
  }
}

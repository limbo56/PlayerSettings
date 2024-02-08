package me.limbo56.playersettings.util;

import java.util.Collection;
import java.util.stream.Collectors;
import me.limbo56.playersettings.hook.AdventureHook;
import me.limbo56.playersettings.user.SettingUser;
import me.limbo56.playersettings.util.text.TextMessage;
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
    sendMessage(sender, TextMessage.builder().from(text));
  }

  public static void sendMessage(Player player, Component component) {
    AdventureHook.adventure().player(player).sendMessage(component);
  }

  public static void sendMessage(CommandSender sender, TextMessage textMessage) {
    sendMessage(sender, textMessage, "");
  }

  public static void sendMessage(CommandSender sender, TextMessage textMessage, String delimiter) {
    String message = String.join(delimiter, textMessage.getTextLines());
    if (!message.isEmpty()) {
      sender.sendMessage(message);
    }
  }

  public static void sendUpdateMessage(Player player) {
    Messages.getUpdateMessage()
        .thenAcceptAsync(
            message ->
                Players.sendMessage(player, Messages.INTERNAL_PREFIX + String.join("\n", message)))
        .exceptionally(
            exception -> {
              PluginLogger.severe(
                  "An exception occurred while sending the version message", exception);
              return null;
            });
  }
}

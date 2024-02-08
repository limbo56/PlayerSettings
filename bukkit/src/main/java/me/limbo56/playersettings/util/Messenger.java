package me.limbo56.playersettings.util;

import com.google.common.collect.Sets;
import java.util.function.Function;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.util.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.util.text.TextMessage;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public final class Messenger {
  private final MessagesConfiguration messagesConfiguration;

  public Messenger(PlayerSettings plugin) {
    this.messagesConfiguration =
        plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class);
  }

  public void sendMessage(Player player, String text) {
    sendMessage(player, text, new PlaceholderAPIModifier(player));
  }

  public void sendMessage(Player player, Component message) {
    Players.sendMessage(player, formatMessage(message));
  }

  @SafeVarargs
  public final void sendMessage(Player player, String text, Function<String, String>... modifiers) {
    if (!text.isEmpty()) {
      TextMessage message =
          TextMessage.builder()
              .addModifier(new PlaceholderAPIModifier(player))
              .addModifiers(Sets.newHashSet(modifiers))
              .from(text);
      Players.sendMessage(player, formatMessage(message));
    }
  }

  public void sendMessage(CommandSender sender, String text) {
    if (!text.isEmpty()) {
      Players.sendMessage(sender, formatMessage(text));
    }
  }

  private Component formatMessage(Component message) {
    return getMessagePrefix().asTextComponent().append(message);
  }

  private TextMessage formatMessage(TextMessage message) {
    return getMessagePrefix().append(message);
  }

  private TextMessage formatMessage(String message) {
    return getMessagePrefix().append(TextMessage.builder().from(message));
  }

  private TextMessage getMessagePrefix() {
    return TextMessage.builder().from(messagesConfiguration.getMessagePrefix());
  }
}

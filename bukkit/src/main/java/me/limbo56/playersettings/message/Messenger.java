package me.limbo56.playersettings.message;

import com.google.common.collect.ObjectArrays;
import java.util.function.Function;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.MessagesConfiguration;
import me.limbo56.playersettings.message.text.PlaceholderAPIModifier;
import me.limbo56.playersettings.message.text.Text;
import me.limbo56.playersettings.util.Players;
import net.kyori.adventure.text.Component;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Messenger {
  private final MessageProvider messageProvider;

  public Messenger(PlayerSettings plugin) {
    this.messageProvider =
        new MessageProvider(
            plugin.getConfigurationManager().getConfiguration(MessagesConfiguration.class));
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
      Function<String, String>[] defaultModifiers =
          ObjectArrays.concat(new PlaceholderAPIModifier(player), modifiers);
      Players.sendMessage(player, formatMessage(Text.create(text, defaultModifiers)));
    }
  }

  public void sendMessage(CommandSender sender, String text) {
    if (!text.isEmpty()) {
      Players.sendMessage(sender, formatMessage(text));
    }
  }

  private Component formatMessage(Component message) {
    return messageProvider.getMessagePrefix().asTextComponent().append(message);
  }

  private Text formatMessage(Text message) {
    return messageProvider.getMessagePrefix().append(message);
  }

  private Text formatMessage(String message) {
    return messageProvider.getMessagePrefix().append(Text.create(message));
  }

  public MessageProvider getMessageProvider() {
    return messageProvider;
  }
}

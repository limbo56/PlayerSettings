package me.limbo56.playersettings.util;

import com.google.common.base.Preconditions;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.command.CommandSender;

/** Utility object to create, modify, and handle text */
public class Text {
  private final Set<Function<String, String>> modifiers = new HashSet<>();
  private final Collection<String> text;

  private Text(Collection<String> text) {
    this.text = text;
  }

  /**
   * Creates a new Text object from a string
   *
   * @param text Text to be modified
   * @return New Text object
   */
  public static Text from(String text) {
    return new Text(Collections.singletonList(text));
  }

  /**
   * Creates a new Text object from a list of strings
   *
   * @param text Text to be modified
   * @return New Text object
   */
  public static Text from(Collection<String> text) {
    return new Text(text);
  }

  /**
   * Creates a new Text object from a message stored in the messages configuration
   *
   * @param path Path to the message in the messages configuration
   * @return New Text object
   */
  public static Text fromMessages(String path) {
    String message = PlayerSettingsProvider.getPlugin().getMessagesConfiguration().getString(path);
    Preconditions.checkNotNull(message, "Message '" + path + "' is not a valid message");
    return new Text(Collections.singletonList(message));
  }

  /**
   * Creates and adds a new modifier to the Text object. The modifier will replace all instances of
   * the placeholder with the value.
   *
   * @param placeholder Placeholder to be replaced
   * @param value Value to replace the placeholder with
   * @return Text object
   */
  public Text placeholder(String placeholder, String value) {
    modifiers.add(text -> text.replaceAll(placeholder, value));
    return this;
  }

  /**
   * Applies all modifiers to the text and returns the modified text.
   *
   * @return Modified text
   */
  public List<String> build() {
    return this.apply(this.text);
  }

  /**
   * Applies all modifiers to the text and returns the first result
   *
   * @return First result from modified text list
   */
  public String first() {
    return this.apply(this.text).get(0);
  }

  /**
   * Applies all modifiers to the text and sends a chat message using it to a {@link CommandSender}
   *
   * @param sender {@link CommandSender} to send the message to
   */
  public void sendMessage(CommandSender sender) {
    this.sendMessage(sender, "");
  }

  public void sendMessage(CommandSender sender, String prefix) {
    String message = String.join("\n", this.build());
    if (message.isEmpty()) {
      return;
    }
    sender.sendMessage(ColorUtil.translateColorCodes(prefix) + message);
  }

  /**
   * Applies all the modifiers to the text
   *
   * @param text Text to modify
   * @return Modified text
   */
  private List<String> apply(Collection<String> text) {
    return text.stream()
        .map(modifiers.stream().reduce(Function::andThen).orElse(Function.identity()))
        .map(ColorUtil::translateColorCodes)
        .collect(Collectors.toList());
  }
}

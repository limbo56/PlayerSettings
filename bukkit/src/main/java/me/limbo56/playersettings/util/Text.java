package me.limbo56.playersettings.util;

import com.google.common.base.Preconditions;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import me.clip.placeholderapi.PlaceholderAPI;
import me.limbo56.playersettings.PlayerSettingsProvider;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/** Utility object to create, modify, and handle text */
public class Text {
  private static final @NotNull LegacyComponentSerializer LEGACY_COMPONENT_SERIALIZER =
      LegacyComponentSerializer.legacySection();
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
    String message =
        PlayerSettingsProvider.getPlugin().getMessagesConfiguration().getFile().getString(path);
    Preconditions.checkNotNull(message, "Message '" + path + "' is not a valid message");
    return new Text(Collections.singletonList(message));
  }

  public static Text fromMessages(String path, String defaultMessage) {
    String message =
        PlayerSettingsProvider.getPlugin().getMessagesConfiguration().getFile().getString(path);
    return new Text(Collections.singletonList(message == null ? defaultMessage : message));
  }

  public static Text fromMessages(String path, List<String> defaultMessage) {
    YamlConfiguration messagesConfiguration =
        PlayerSettingsProvider.getPlugin().getMessagesConfiguration().getFile();
    List<String> message =
        messagesConfiguration.contains(path)
            ? messagesConfiguration.getStringList(path)
            : defaultMessage;
    return from(String.join("\n", message));
  }

  /**
   * Creates and adds a new modifier to the Text object. The modifier will replace all instances of
   * the placeholder with the value.
   *
   * @param placeholder Placeholder to replace
   * @param value Value to replace the placeholder
   * @return Text object
   */
  public Text usePlaceholder(String placeholder, String value) {
    return this.addModifier(text -> text.replaceAll(placeholder, value));
  }

  /**
   * Adds a modifier that will replace any placeholders detected by the PlaceholderAPI plugin.
   *
   * @param player Player to load placeholders for
   * @return Text object instance
   */
  public Text usePlaceholderApi(Player player) {
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      return this.addModifier(text -> PlaceholderAPI.setPlaceholders(player, text));
    } else {
      return this;
    }
  }

  /**
   * Adds a modifier that will be applied when rendering the Text object.
   *
   * @param modifier Modifier to add
   * @return Text object instance
   */
  public Text addModifier(Function<String, String> modifier) {
    modifiers.add(modifier);
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

  public TextComponent text() {
    TextComponent.Builder textBuilder = Component.text();
    for (String modifiedText : this.apply(this.text)) {
      textBuilder.append(LEGACY_COMPONENT_SERIALIZER.deserialize(modifiedText));
    }
    return textBuilder.build();
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
    sender.sendMessage(Colors.translateColorCodes(prefix) + message);
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
        .map(Colors::translateColorCodes)
        .collect(Collectors.toList());
  }
}

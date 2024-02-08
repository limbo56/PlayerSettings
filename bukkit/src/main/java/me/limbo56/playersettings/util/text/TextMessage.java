package me.limbo56.playersettings.util.text;

import com.google.common.collect.Lists;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.limbo56.playersettings.util.Colors;
import me.limbo56.playersettings.util.Text;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/** Utility class for creating and manipulating text messages. */
public class TextMessage {
  private final List<String> textLines;

  private TextMessage(List<String> textLines) {
    this.textLines = Collections.unmodifiableList(textLines);
  }

  /**
   * Creates a new {@link Builder} for constructing TextMessage instances.
   *
   * @return A new instance of Builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Appends the contents of another {@link TextMessage} instance to the current one.
   *
   * @param other The {@link TextMessage} instance whose contents to append.
   * @return A new {@link TextMessage} instance containing the combined contents of both instances.
   */
  public TextMessage append(TextMessage other) {
    return new TextMessage(
        Stream.concat(textLines.stream(), other.textLines.stream()).collect(Collectors.toList()));
  }

  /**
   * Gets the modified text lines.
   *
   * @return Modified text lines.
   */
  public List<String> getTextLines() {
    return textLines;
  }

  /**
   * Gets the first text line from the modified text.
   *
   * @return First text line from the modified text, or an empty string if the text is empty.
   */
  public String getFirstTextLine() {
    return textLines.isEmpty() ? "" : textLines.get(0);
  }

  /**
   * Converts the text to a {@link net.kyori.adventure.text.TextComponent}.
   *
   * @return {@link net.kyori.adventure.text.TextComponent} representation of the text.
   */
  public TextComponent asTextComponent() {
    TextComponent.Builder textBuilder = Component.text();

    for (int i = 0; i < textLines.size(); i++) {
      String modifiedText = textLines.get(i);
      textBuilder.append(Text.fromLegacySection(modifiedText));
      if (i < textLines.size() - 1) {
        textBuilder.appendNewline();
      }
    }

    return textBuilder.build();
  }

  /** Builder class for constructing TextMessage instances with modifiers. */
  public static class Builder {
    private final List<Function<String, String>> modifiers = new LinkedList<>();

    public Builder() {
      // Default modifier for translating color codes
      this.modifiers.add(Colors::translateColorCodes);
    }

    /**
     * Adds a modifier that replaces placeholders with specified values.
     *
     * @param placeholder The placeholder string to replace.
     * @param value The value to replace the placeholder with.
     * @return This builder instance for method chaining.
     */
    public Builder replacePlaceholder(String placeholder, String value) {
      modifiers.add(ReplaceModifier.of(placeholder, value));
      return this;
    }

    /**
     * Adds multiple modifiers to the text.
     *
     * @param modifierSet The set of modifier functions to apply.
     * @return This builder instance for method chaining.
     */
    public Builder addModifiers(Set<Function<String, String>> modifierSet) {
      modifiers.addAll(modifierSet);
      return this;
    }

    /**
     * Adds multiple modifiers to the text.
     *
     * @param modifiers The modifier functions to apply.
     * @return This builder instance for method chaining.
     */
    @SafeVarargs
    public final Builder addModifiers(Function<String, String>... modifiers) {
      if (modifiers != null) {
        Collections.addAll(this.modifiers, modifiers);
      }
      return this;
    }

    /**
     * Adds a custom modifier to the text.
     *
     * @param modifier The custom modifier function to apply.
     * @return This builder instance for method chaining.
     */
    public Builder addModifier(Function<String, String> modifier) {
      modifiers.add(modifier);
      return this;
    }

    /**
     * Constructs a TextMessage instance from a single text line.
     *
     * @param text The text line to construct the TextMessage instance.
     * @return The constructed TextMessage instance.
     */
    public TextMessage from(String text) {
      return from(Collections.singletonList(text));
    }

    /**
     * Constructs a TextMessage instance from a list of text lines.
     *
     * @param textList The list of text lines to construct the TextMessage instance.
     * @return The constructed TextMessage instance.
     */
    public TextMessage from(List<String> textList) {
      return new TextMessage(applyModifiers(textList));
    }

    private List<String> applyModifiers(List<String> textList) {
      return textList.stream()
          .map(Lists.reverse(modifiers).stream().reduce(Function.identity(), Function::andThen))
          .collect(Collectors.toList());
    }
  }
}

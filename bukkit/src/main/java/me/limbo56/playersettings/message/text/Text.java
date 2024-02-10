package me.limbo56.playersettings.message.text;

import com.google.common.collect.Lists;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import me.limbo56.playersettings.util.Adventure;
import me.limbo56.playersettings.util.Colors;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;

/** Utility class for creating and manipulating text. */
public class Text {
  private final List<String> textLines;

  private Text(List<String> textLines) {
    this.textLines = Collections.unmodifiableList(textLines);
  }

  /**
   * Creates a new {@link Builder} for constructing Text instances.
   *
   * @return A new instance of Builder.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates a new {@link Text} instance.
   *
   * @param text The text line to construct the Text instance.
   * @param modifiers The modifier functions to apply.
   * @return The constructed {@link Text} instance.
   */
  @SafeVarargs
  public static Text create(String text, Function<String, String>... modifiers) {
    return Text.builder().addModifiers(modifiers).from(text);
  }

  /**
   * Creates a new {@link Text} instance.
   *
   * @param text The list of text lines to construct the Text instance.
   * @param modifiers The modifier functions to apply.
   * @return The constructed {@link Text} instance.
   */
  @SafeVarargs
  public static Text create(List<String> text, Function<String, String>... modifiers) {
    return Text.builder().addModifiers(modifiers).from(text);
  }

  /**
   * Appends the contents of another {@link Text} instance to the current one.
   *
   * @param other The {@link Text} instance whose contents to append.
   * @return A new {@link Text} instance containing the combined contents of both instances.
   */
  public Text append(Text other) {
    return new Text(
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
      textBuilder.append(Adventure.fromLegacySection(modifiedText));
      if (i < textLines.size() - 1) {
        textBuilder.appendNewline();
      }
    }

    return textBuilder.build();
  }

  /** Builder class for constructing {@link Text} instances with modifiers. */
  public static class Builder {
    private final List<Function<String, String>> modifiers = new LinkedList<>();

    public Builder() {
      // Default modifier for translating color codes
      this.modifiers.add(Colors::translateColorCodes);
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
     * Constructs a {@link Text} instance from a single text line.
     *
     * @param text The text line to construct the Text instance.
     * @return The constructed {@link Text} instance.
     */
    public Text from(String text) {
      return from(Collections.singletonList(text));
    }

    /**
     * Constructs a {@link Text} instance from a list of text lines.
     *
     * @param textList The list of text lines to construct the Text instance.
     * @return The constructed {@link Text} instance.
     */
    public Text from(List<String> textList) {
      return new Text(applyModifiers(textList));
    }

    private List<String> applyModifiers(List<String> textList) {
      return textList.stream()
          .map(Lists.reverse(modifiers).stream().reduce(Function.identity(), Function::andThen))
          .collect(Collectors.toList());
    }
  }
}

package me.limbo56.playersettings.api.setting;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import me.limbo56.playersettings.api.MenuItem;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.Listener;
import org.immutables.value.Value;

/**
 * An object that represents a {@code Setting}.
 *
 * <p>Creating an instance of this class is a fairly easy due to the provided builder. Below is an
 * example of how to create a setting using the builder:
 *
 * <pre>{@code
 * ImmutableSetting.builder()
 *     .item(ImmutableMenuItem.of(itemStack, page, slot))
 *     .name("example-setting")
 *     .defaultValue(0)
 *     .maxValue(1)
 *     .addCallback(
 *         (setting, player, value) -> player.sendMessage(setting.getName() + " - " + value))
 *     .build();
 * }</pre>
 */
@Value.Immutable
@Value.Style(
    depluralize = true,
    get = {"should*", "is*", "get*"})
public interface Setting extends ConfigurationSerializable {
  /**
   * Gets the name of the setting
   *
   * @return name of the setting
   */
  String getName();

  /**
   * Gets the {@link MenuItem} that will display this setting
   *
   * @return {@link MenuItem} that displays this setting
   */
  MenuItem getItem();

  /**
   * Gets the default value for the setting
   *
   * @return default value of the setting
   */
  int getDefaultValue();

  /**
   * Gets the maximum value for the setting
   *
   * @return maximum value of the setting
   */
  int getMaxValue();

  /**
   * Gets whether the setting is enabled in the configuration
   *
   * @return whether the setting is enabled
   */
  @Value.Default
  default boolean isEnabled() {
    return true;
  }

  /**
   * Gets the flag that indicates whether the setting effects should be executed when a player joins
   *
   * @return whether the setting effects should be applied when a player joins
   */
  @Value.Default
  default boolean shouldExecuteOnJoin() {
    return true;
  }

  /**
   * Gets the list of callbacks that will be executed when the setting is changed
   *
   * @return list of {@link SettingCallback}
   */
  List<SettingCallback> getCallbacks();

  /**
   * Gets the list of listeners that this setting requires to work
   *
   * @return List of {@link Listener}s
   */
  List<Listener> getListeners();

  @Override
  default Map<String, Object> serialize() {
    Map<String, Object> mappedObject = new LinkedHashMap<>();
    mappedObject.put("enabled", isEnabled());
    mappedObject.put("default", getDefaultValue());
    mappedObject.put("max", getMaxValue());
    mappedObject.put("executeOnJoin", shouldExecuteOnJoin());
    return mappedObject;
  }
}

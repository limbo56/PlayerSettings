package me.limbo56.playersettings.api;

import com.google.common.collect.ImmutableListMultimap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.immutables.value.Value;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * Represents a configurable setting that can be used within the plugin.
 *
 * <p>Settings can be customized using the provided builder. Below is an example of how to create a
 * setting using the builder:
 *
 * <pre>{@code
 * ImmutableSetting.builder()
 *     .item(ImmutableMenuItem.of(itemStack, page, slot))
 *     .name("example-setting")
 *     .displayName("&6Example")
 *     .defaultValue(0)
 *     .maxValue(1)
 *     .triggers("join", "respawn")
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
   * Retrieves the unique identifier of the setting.
   *
   * @return The name of the setting.
   */
  @NotNull
  String getName();

  /**
   * Retrieves the display name of the setting.
   *
   * @return The display name of the setting.
   */
  String getDisplayName();

  /**
   * Retrieves the menu item associated with this setting.
   *
   * @return The menu item displaying this setting.
   */
  @Value.Default
  default MenuItem getItem() {
    return ImmutableMenuItem.builder()
        .itemStack(new ItemStack(Material.BEDROCK))
        .page(1)
        .slot(0)
        .build();
  }

  /**
   * Retrieves the default value of the setting.
   *
   * @return The default value of the setting.
   */
  int getDefaultValue();

  /**
   * Retrieves the maximum value allowed for the setting.
   *
   * @return The maximum value allowed for the setting.
   */
  int getMaxValue();

  /**
   * Checks if the setting is enabled.
   *
   * @return True if the setting is enabled, false otherwise.
   */
  @Value.Default
  default boolean isEnabled() {
    return true;
  }

  /**
   * Retrieves the triggers/events associated with this setting.
   *
   * @return An array of triggers/events.
   */
  String[] getTriggers();

  /**
   * Checks if the setting has a specific trigger/event.
   *
   * @param trigger The trigger/event to check.
   * @return True if the setting has the trigger/event, false otherwise.
   */
  default boolean hasTrigger(String trigger) {
    for (String settingTrigger : getTriggers()) {
      if (settingTrigger.equals(trigger)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Retrieves the permission string required to disable this setting
   *
   * @return The permission required to disable this setting
   */
  @Nullable
  String getDisablePermission();

  /**
   * Retrieves the list of callbacks executed when the setting is changed.
   *
   * @return The list of setting callbacks.
   */
  List<SettingCallback> getCallbacks();

  /**
   * Retrieves the list of listeners required for this setting.
   *
   * @return The list of listeners.
   */
  List<Listener> getListeners();

  /**
   * Retrieves a map of value aliases for the setting.
   *
   * <p>These aliases allow associating user input strings with integer values for the setting.
   *
   * @return A map of aliases to values.
   */
  @Value.Default
  default ImmutableListMultimap<String, Integer> getValueAliases() {
    return ImmutableListMultimap.of();
  }

  @Override
  default @NotNull Map<String, Object> serialize() {
    Map<String, Object> mappedObject = new LinkedHashMap<>();
    mappedObject.put("enabled", isEnabled());
    mappedObject.put("name", getDisplayName());
    mappedObject.put("default", getDefaultValue());
    mappedObject.put("max", getMaxValue());
    mappedObject.put("triggers", getTriggers());
    if (getDisablePermission() != null) {
      mappedObject.put("disable-permission", getDisablePermission());
    }
    return mappedObject;
  }
}

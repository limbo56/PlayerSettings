package me.limbo56.playersettings.setting;

import com.google.common.collect.ImmutableListMultimap;
import java.util.List;
import me.limbo56.playersettings.api.MenuItem;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.api.SettingCallback;
import org.bukkit.event.Listener;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ForwardingSetting implements Setting {
  protected Setting setting;

  protected ForwardingSetting(Setting setting) {
    this.setting = setting;
  }

  @Override
  public @NotNull String getName() {
    return setting.getName();
  }

  @Override
  public String getDisplayName() {
    return setting.getDisplayName();
  }

  @Override
  public MenuItem getItem() {
    return setting.getItem();
  }

  @Override
  public int getDefaultValue() {
    return setting.getDefaultValue();
  }

  @Override
  public int getMaxValue() {
    return setting.getMaxValue();
  }

  @Override
  public String[] getTriggers() {
    return setting.getTriggers();
  }

  @Override
  public @Nullable String getDisablePermission() {
    return setting.getDisablePermission();
  }

  @Override
  public List<SettingCallback> getCallbacks() {
    return setting.getCallbacks();
  }

  @Override
  public List<Listener> getListeners() {
    return setting.getListeners();
  }

  @Override
  public ImmutableListMultimap<String, Integer> getValueAliases() {
    return setting.getValueAliases();
  }
}

package me.limbo56.playersettings.hook;

import me.limbo56.playersettings.PlayerSettings;
import net.kyori.adventure.platform.bukkit.BukkitAudiences;
import org.jetbrains.annotations.NotNull;

public class AdventureHook extends Hook {
  private static BukkitAudiences adventure;

  protected AdventureHook(PlayerSettings plugin) {
    super(plugin);
  }

  public static @NotNull BukkitAudiences adventure() {
    return adventure;
  }

  public static void unloadAdventure() {
    if (adventure != null) {
      adventure.close();
      adventure = null;
    }
  }

  @Override
  protected void register() {
    if (adventure == null) {
      adventure = BukkitAudiences.create(plugin);
    }
  }
}

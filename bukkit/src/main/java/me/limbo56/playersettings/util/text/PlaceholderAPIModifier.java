package me.limbo56.playersettings.util.text;

import java.util.function.Function;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class PlaceholderAPIModifier implements Function<String, String> {
  private final Player player;

  public PlaceholderAPIModifier(Player player) {
    this.player = player;
  }

  @Override
  public String apply(String text) {
    if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
      return PlaceholderAPI.setPlaceholders(player, text);
    }
    return text;
  }
}

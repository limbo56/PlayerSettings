package me.limbo56.playersettings.listener;

import java.util.ArrayList;
import java.util.List;
import me.limbo56.playersettings.PlayerSettings;
import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

public class ListenerManager {
  private final PlayerSettings plugin;
  private final List<Listener> listeners;

  public ListenerManager(PlayerSettings plugin) {
    this.plugin = plugin;
    this.listeners = new ArrayList<>();
  }

  public void registerDefaultListeners() {
    registerListener(new PlayerListener(plugin));
    registerListener(new InventoryListener(plugin));
  }

  public void registerListener(Listener listener) {
    this.listeners.add(listener);
    Bukkit.getPluginManager().registerEvents(listener, plugin);
  }

  public void unregisterListener(Listener listener) {
    this.listeners.remove(listener);
    HandlerList.unregisterAll(listener);
  }

  public void unloadAll() {
    this.listeners.forEach(HandlerList::unregisterAll);
    this.listeners.clear();
  }
}

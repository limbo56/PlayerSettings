package me.limbo56.playersettings.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;

import java.util.ArrayList;
import java.util.List;

import static me.limbo56.playersettings.PlayerSettingsProvider.getPlugin;

public class ListenerManager {
  private final List<Listener> listenerList = new ArrayList<>();

  public void registerListener(Listener listener) {
    listenerList.add(listener);
    Bukkit.getPluginManager().registerEvents(listener, getPlugin());
  }

  public void unregisterListener(Listener listener) {
    listenerList.remove(listener);
    HandlerList.unregisterAll(listener);
  }

  public void unloadAll() {
    listenerList.forEach(HandlerList::unregisterAll);
    listenerList.clear();
  }
}

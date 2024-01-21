package me.limbo56.playersettings.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

public class TaskChain {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final Map<String, Supplier<Object>> loaderMap = new ConcurrentHashMap<>();
  private final Queue<Consumer<Map<String, Object>>> asyncCallbacks = new ConcurrentLinkedQueue<>();
  private final Queue<Consumer<Map<String, Object>>> syncCallbacks = new ConcurrentLinkedQueue<>();

  public TaskChain loadAsync(String key, Supplier<Object> objectSupplier) {
    loaderMap.put(key, objectSupplier);
    return this;
  }

  public TaskChain async(Consumer<Map<String, Object>> dataConsumer) {
    asyncCallbacks.add(dataConsumer);
    return this;
  }

  public TaskChain sync(Consumer<Map<String, Object>> dataConsumer) {
    syncCallbacks.add(dataConsumer);
    return this;
  }

  public void runAsync() {
    Bukkit.getScheduler().runTaskAsynchronously(PLUGIN, this::runAsyncChain);
  }

  private void runAsyncChain() {
    Map<String, Object> data = loadData();
    for (Consumer<Map<String, Object>> asyncCallback : asyncCallbacks) {
      asyncCallback.accept(data);
    }
    Bukkit.getScheduler()
        .runTask(PLUGIN, () -> syncCallbacks.forEach(dataConsumer -> dataConsumer.accept(data)));
  }

  public void runSync() {
    Bukkit.getScheduler().runTask(PLUGIN, this::runSyncChain);
  }

  public void runSyncLater(long delay) {
    Bukkit.getScheduler().runTaskLater(PLUGIN, this::runSyncChain, delay);
  }

  private void runSyncChain() {
    Map<String, Object> data = loadData();
    asyncCallbacks.forEach(dataConsumer -> dataConsumer.accept(data));
    syncCallbacks.forEach(dataConsumer -> dataConsumer.accept(data));
  }

  @NotNull
  private Map<String, Object> loadData() {
    Map<String, Object> dataMap = new HashMap<>();
    for (Map.Entry<String, Supplier<Object>> entry : loaderMap.entrySet()) {
      if (dataMap.put(entry.getKey(), entry.getValue().get()) != null) {
        throw new IllegalStateException("Duplicate key");
      }
    }
    return dataMap;
  }
}

package me.limbo56.playersettings.util;

import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import org.bukkit.Bukkit;
import org.jetbrains.annotations.NotNull;

import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
    asyncCallbacks.forEach(dataConsumer -> dataConsumer.accept(data));
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
    return loaderMap.entrySet().stream()
        .collect(
            Collectors.toMap(
                Map.Entry::getKey, stringSupplierEntry -> stringSupplierEntry.getValue().get()));
  }
}

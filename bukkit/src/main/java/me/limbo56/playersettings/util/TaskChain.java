package me.limbo56.playersettings.util;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Consumer;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public class TaskChain {
  private final Map<String, Supplier<Object>> loaderMap = new ConcurrentHashMap<>();
  private final Queue<Task> asyncTasks = new ConcurrentLinkedQueue<>();
  private final Queue<Task> syncTasks = new ConcurrentLinkedQueue<>();

  public TaskChain loadAsync(String key, Supplier<Object> objectSupplier) {
    loaderMap.put(key, objectSupplier);
    return this;
  }

  public TaskChain async(Task task) {
    asyncTasks.add(task);
    return this;
  }

  public TaskChain sync(Task task) {
    syncTasks.add(task);
    return this;
  }

  public void runAsync(JavaPlugin plugin) {
    Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> runAsyncChain(plugin));
  }

  private void runAsyncChain(JavaPlugin plugin) {
    Map<String, Object> data = loadData();
    for (Task asyncConsumer : asyncTasks) {
      asyncConsumer.accept(data);
    }
    Bukkit.getScheduler()
        .runTask(plugin, () -> syncTasks.forEach(callback -> callback.accept(data)));
  }

  public void runSync(JavaPlugin plugin) {
    Bukkit.getScheduler().runTask(plugin, this::runSyncChain);
  }

  public void runSyncLater(JavaPlugin plugin, long delay) {
    Bukkit.getScheduler().runTaskLater(plugin, this::runSyncChain, delay);
  }

  private void runSyncChain() {
    Map<String, Object> data = loadData();
    asyncTasks.forEach(callback -> callback.accept(data));
    syncTasks.forEach(callback -> callback.accept(data));
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

  /**
   * Represents a task in a {@link TaskChain}.
   *
   * <p>A task is an extension of a {@link Consumer} that accepts a data map shared across the chain
   * of tasks.
   */
  public interface Task extends Consumer<Map<String, Object>> {}
}

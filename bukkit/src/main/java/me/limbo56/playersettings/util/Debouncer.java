package me.limbo56.playersettings.util;

import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;
import org.jetbrains.annotations.NotNull;

public class Debouncer<T> {
  protected final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
  protected final Map<T, ScheduledFuture<?>> delayedTasks = new ConcurrentHashMap<>();
  protected final Consumer<T> callback;
  protected final long intervalMillis;

  public Debouncer(Consumer<T> callback, long intervalMillis) {
    this.callback = callback;
    this.intervalMillis = intervalMillis;
  }

  public void call(T key) {
    delayedTasks.compute(key, this::createDelayedTask);
  }

  @NotNull
  protected ScheduledFuture<?> createDelayedTask(T k, ScheduledFuture<?> task) {
    if (task != null) {
      task.cancel(false); // Cancel any existing task for this key
    }
    return executor.schedule(() -> callback.accept(k), intervalMillis, TimeUnit.MILLISECONDS);
  }

  public void terminate() {
    executor.shutdownNow();
  }
}

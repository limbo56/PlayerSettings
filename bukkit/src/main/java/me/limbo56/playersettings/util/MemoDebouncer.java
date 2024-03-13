package me.limbo56.playersettings.util;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;

public class MemoDebouncer<T, V> extends Debouncer<T> {
  private final Map<T, V> taskValues = new ConcurrentHashMap<>();
  private final Function<T, V> valueFunction;

  public MemoDebouncer(Function<T, V> valueFunction, Consumer<T> callback, long intervalMillis) {
    super(callback, intervalMillis);
    this.valueFunction = valueFunction;
  }

  @Override
  protected @NotNull ScheduledFuture<?> createDelayedTask(T k, ScheduledFuture<?> task) {
    V value = valueFunction.apply(k);
    if (task != null) {
      task.cancel(false); // Cancel any existing task for this key
    }
    return executor.schedule(
        () -> {
          if (!taskValues.containsKey(k) || !taskValues.get(k).equals(value)) {
            taskValues.put(k, value);
            callback.accept(k);
          }
        },
        intervalMillis,
        TimeUnit.MILLISECONDS);
  }
}

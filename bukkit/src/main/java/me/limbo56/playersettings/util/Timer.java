package me.limbo56.playersettings.util;

import java.time.Duration;
import java.time.Instant;

public class Timer {
  private final Instant startTime;
  private Instant stopTime;

  private Timer() {
    this.startTime = Instant.now();
  }

  public static Timer start() {
    return new Timer();
  }

  public void stop() {
    if (stopTime == null) {
      stopTime = Instant.now();
    }
  }

  public Duration getElapsedDuration() {
    return Duration.between(startTime, stopTime);
  }
}

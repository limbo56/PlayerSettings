package me.limbo56.playersettings.util;

import java.time.Duration;
import java.time.Instant;

public class ElapsedTimer {
  private final Instant startTime;
  private Instant stopTime;

  private ElapsedTimer() {
    this.startTime = Instant.now();
  }

  public static ElapsedTimer start() {
    return new ElapsedTimer();
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

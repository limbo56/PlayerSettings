package me.limbo56.playersettings.util.data;

public interface Parser<K, V> {
  V parse(K key);
}

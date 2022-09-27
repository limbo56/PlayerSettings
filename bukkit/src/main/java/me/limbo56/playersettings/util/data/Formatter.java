package me.limbo56.playersettings.util.data;

public interface Formatter<K, V> {
  V format(K key);
}

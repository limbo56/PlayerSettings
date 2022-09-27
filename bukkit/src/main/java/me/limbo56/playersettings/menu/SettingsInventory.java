package me.limbo56.playersettings.menu;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.jetbrains.annotations.NotNull;

public class SettingsInventory implements InventoryHolder {
  private static final PlayerSettings plugin = PlayerSettingsProvider.getPlugin();
  private final UUID owner;
  private final String title;
  private final int size;
  private final int page;
  private final Map<Integer, InventoryItem> renderedItems = new HashMap<>();
  private Inventory inventory;

  public SettingsInventory(UUID owner, String title, int size, int page) {
    this.owner = owner;
    this.title = title;
    this.size = size;
    this.page = page;
  }

  public void renderItem(InventoryItem item) {
    renderedItems.put(item.getSlot(), item);
    createInventory();
    inventory.setItem(item.getSlot(), item.getItem());
  }

  private void createInventory() {
    if (this.inventory == null) {
      this.inventory = Bukkit.createInventory(this, size, title);
    }
  }

  public InventoryItem getItem(int slot) {
    return renderedItems.values().stream()
        .filter(inventoryItem -> inventoryItem.getSlot() == slot)
        .findFirst()
        .orElse(null);
  }

  public String getTitle() {
    return title;
  }

  public int getSize() {
    return size;
  }

  public int getPage() {
    return page;
  }

  public UUID getOwner() {
    return owner;
  }

  public SettingUser getUser() {
    return plugin.getUserManager().getUser(owner);
  }

  @Override
  public @NotNull Inventory getInventory() {
    createInventory();
    return this.inventory;
  }
}

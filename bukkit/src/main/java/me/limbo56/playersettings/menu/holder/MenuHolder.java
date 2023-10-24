package me.limbo56.playersettings.menu.holder;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import me.limbo56.playersettings.user.SettingUser;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

public class MenuHolder implements InventoryHolder {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();
  private final UUID owner;
  private final String title;
  private final int size;
  private final Map<Integer, SettingsMenuItem> menuItemMap = new HashMap<>();
  private final Inventory inventory;

  public MenuHolder(UUID owner, String title, int size) {
    this.owner = owner;
    this.title = title;
    this.size = size;
    this.inventory = Bukkit.createInventory(this, size, title);
  }

  public void renderItem(SettingsMenuItem menuItem) {
    ItemStack itemStack = menuItem.getItemStack();
    int slot = menuItem.getSlot();
    inventory.setItem(slot, itemStack);
    menuItemMap.put(slot, menuItem);
  }

  public SettingsMenuItem getMenuItem(int slot) {
    return menuItemMap.values().stream()
        .filter(menuItem -> menuItem.getSlot() == slot)
        .findFirst()
        .orElse(null);
  }

  @NotNull
  public String getTitle() {
    return title;
  }

  public int getSize() {
    return size;
  }

  @NotNull
  public UUID getOwnerUniqueId() {
    return owner;
  }

  public SettingUser getOwner() {
    return PLUGIN.getUserManager().getUser(getOwnerUniqueId());
  }

  @Override
  @NotNull
  public Inventory getInventory() {
    return this.inventory;
  }
}

package me.limbo56.playersettings.menu;

import me.limbo56.playersettings.menu.actions.InventoryItemAction;
import org.bukkit.inventory.ItemStack;

public class InventoryItem {
  private final int slot;
  private final ItemStack item;
  private final InventoryItemAction clickAction;

  public InventoryItem(int slot, ItemStack item, InventoryItemAction clickAction) {
    this.slot = slot;
    this.item = item;
    this.clickAction = clickAction;
  }

  public int getSlot() {
    return slot;
  }

  public ItemStack getItem() {
    return item;
  }

  public InventoryItemAction getClickAction() {
    return clickAction;
  }
}

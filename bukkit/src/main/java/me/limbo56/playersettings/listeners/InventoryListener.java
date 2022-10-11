package me.limbo56.playersettings.listeners;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.menu.InventoryItem;
import me.limbo56.playersettings.menu.SettingsInventory;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    HumanEntity whoClicked = event.getWhoClicked();
    if (!PlayerSettingsProvider.isAllowedWorld(whoClicked.getWorld().getName())) return;
    if (!(whoClicked instanceof Player)) return;

    // Check if the inventory is the settings menu
    Inventory topInventory = event.getView().getTopInventory();
    if (!(topInventory.getHolder() instanceof SettingsInventory)) {
      return;
    }

    // Check if item clicked is not null
    ItemStack itemStack = event.getCurrentItem();
    if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) {
      return;
    }

    // Check if item is not null
    SettingsInventory holder = (SettingsInventory) topInventory.getHolder();
    InventoryItem item = holder.getItem(event.getRawSlot());
    if (item == null) {
      return;
    }

    event.setCancelled(true);
    item.getClickAction().execute(event.getClick(), holder.getUser());
  }
}
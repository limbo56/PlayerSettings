package me.limbo56.playersettings.listeners;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.PlayerSettingsProvider;
import me.limbo56.playersettings.menu.holder.MenuHolder;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
  private static final PlayerSettings PLUGIN = PlayerSettingsProvider.getPlugin();

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    HumanEntity whoClicked = event.getWhoClicked();
    if (!PLUGIN.getPluginConfiguration().isAllowedWorld(whoClicked.getWorld().getName())
        || !(whoClicked instanceof Player)) {
      return;
    }

    // Check if the inventory is the settings menu
    Inventory topInventory = event.getView().getTopInventory();
    if (!(topInventory.getHolder() instanceof MenuHolder)) {
      return;
    }
    event.setCancelled(true);

    // Check if item clicked is not null
    ItemStack itemStack = event.getCurrentItem();
    if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) {
      return;
    }

    // Check if item is not null
    MenuHolder holder = (MenuHolder) topInventory.getHolder();
    SettingsMenuItem item = holder.getMenuItem(event.getRawSlot());
    if (item == null) {
      return;
    }

    item.executeClickAction(event.getClick(), holder.getOwner());
  }
}

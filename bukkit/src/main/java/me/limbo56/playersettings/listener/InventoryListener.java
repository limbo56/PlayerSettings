package me.limbo56.playersettings.listener;

import com.cryptomorin.xseries.XMaterial;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.configuration.PluginConfiguration;
import me.limbo56.playersettings.menu.SettingsMenu;
import me.limbo56.playersettings.menu.item.SettingsMenuItem;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
  private final PluginConfiguration pluginConfiguration;

  public InventoryListener(PlayerSettings plugin) {
    this.pluginConfiguration = plugin.getConfiguration();
  }

  @EventHandler
  public void onInventoryClick(InventoryClickEvent event) {
    HumanEntity whoClicked = event.getWhoClicked();
    if (!(whoClicked instanceof Player)
        || !pluginConfiguration.isAllowedWorld(whoClicked.getWorld().getName())) {
      return;
    }

    // Check if the inventory is the settings menu
    Inventory topInventory = event.getView().getTopInventory();
    if (!(topInventory.getHolder() instanceof SettingsMenu)) {
      return;
    }
    event.setCancelled(true);

    // Check if item clicked is not null
    ItemStack itemStack = event.getCurrentItem();
    if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) {
      return;
    }

    // Check if item is not null
    SettingsMenu holder = (SettingsMenu) topInventory.getHolder();
    SettingsMenuItem item = holder.getMenuItem(event.getRawSlot());
    if (item == null) {
      return;
    }

    item.executeClickAction(event.getClick(), holder.getOwner());
  }
}

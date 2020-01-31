package me.limbo56.playersettings.listeners;

import com.cryptomorin.xseries.XMaterial;
import lombok.AllArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.menu.CustomItem;
import me.limbo56.playersettings.menu.SettingsHolder;
import me.limbo56.playersettings.settings.SPlayer;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import static me.limbo56.playersettings.utils.PlayerUtils.isAllowedWorld;

@AllArgsConstructor
public class InventoryListener implements Listener {
    private PlayerSettings plugin;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!isAllowedWorld(event.getWhoClicked().getWorld().getName())) return;
        if (!(event.getView().getTopInventory().getHolder() instanceof SettingsHolder)) return;
        if (!(event.getWhoClicked() instanceof Player)) return;

        // Check if not empty
        ItemStack itemStack = event.getCurrentItem();
        if (itemStack == null || itemStack.getType() == XMaterial.AIR.parseMaterial()) return;

        // Check if item is not null
        SettingsHolder holder = (SettingsHolder) event.getView().getTopInventory().getHolder();
        CustomItem item = holder.getItem(event.getRawSlot());
        if (item == null) return;

        SPlayer sPlayer = plugin.getSPlayer(event.getWhoClicked().getUniqueId());

        // Cancel event and execute action
        event.setCancelled(true);
        item.getClickAction().accept(sPlayer);
    }
}

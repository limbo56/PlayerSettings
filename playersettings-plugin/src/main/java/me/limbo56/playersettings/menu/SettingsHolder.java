package me.limbo56.playersettings.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import me.limbo56.playersettings.PlayerSettings;
import me.limbo56.playersettings.api.Setting;
import me.limbo56.playersettings.settings.SPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Getter
@RequiredArgsConstructor
public class SettingsHolder implements InventoryHolder {
    private final String title;
    private final int size;
    private final int page;
    private List<CustomItem> renderedItems = new ArrayList<>();

    public void renderItem(CustomItem customItem) {
        this.renderedItems.add(customItem);
    }

    public void renderSetting(Setting setting, ItemStack toggleItem, int oldValue) {
        Consumer<SPlayer> playerConsumer = sPlayer -> {
            Player player = sPlayer.getPlayer();
            // Do not recalculate value as, in case it changed, it would be inconsistent
            // (UX shows "will set to X" but will actually set to Y)
            int newValue = oldValue < 0 ? -oldValue : oldValue + 1;
            Setting configSetting = PlayerSettings.getPlugin().getSettingsRegistry().getSetting(setting.getRawName());
            if (newValue > configSetting.getMaxValue())
                newValue = 0;
            String value = String.valueOf(newValue);
            player.performCommand("settings set " + setting.getRawName() + " " + value);
            SettingsMenu.openMenu(sPlayer, setting.getPage());
        };
        // fixme this is ugly AF
        Consumer<SPlayer> toggleConsumer = sPlayer -> {
            Player player = sPlayer.getPlayer();
            int newValue = oldValue == 0 ? 1 : -oldValue;
            String value = String.valueOf(newValue);
            player.performCommand("settings set " + setting.getRawName() + " " + value);
            SettingsMenu.openMenu(sPlayer, setting.getPage());
        };
        ItemStack itemStack = setting.getItem();
        itemStack.setAmount(Math.max(1, Math.abs(oldValue)));
        this.renderItem(new CustomItem(setting.getSlot(), itemStack, playerConsumer));
        this.renderItem(new CustomItem(setting.getSlot() + 9, toggleItem, toggleConsumer));
    }

    public CustomItem getItem(int slot) {
        return renderedItems.stream()
                .filter(customItem -> customItem.getSlot() == slot)
                .findFirst()
                .orElse(null);
    }

    @Override
    public Inventory getInventory() {
        Inventory inventory = Bukkit.createInventory(this, size, title);
        for (CustomItem item : renderedItems) inventory.setItem(item.getSlot(), item.getItem());
        return inventory;
    }
}

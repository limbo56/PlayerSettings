package me.limbo56.playersettings.menu;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
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

    public void renderSetting(Setting setting, ItemStack toggleItem) {
        Consumer<SPlayer> playerConsumer = sPlayer -> {
            Player player = sPlayer.getPlayer();
            String value = String.valueOf(!sPlayer.getSettingWatcher().getValue(setting))
                    .replaceAll("true", "on")
                    .replaceAll("false", "off");
            player.performCommand("settings set " + setting.getRawName() + " " + value);
            SettingsMenu.openMenu(sPlayer, setting.getPage());
        };
        this.renderItem(new CustomItem(setting.getSlot(), setting.getItem(), playerConsumer));
        this.renderItem(new CustomItem(setting.getSlot() + 9, toggleItem, playerConsumer));
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

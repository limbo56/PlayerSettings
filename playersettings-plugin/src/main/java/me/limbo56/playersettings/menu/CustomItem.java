package me.limbo56.playersettings.menu;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.limbo56.playersettings.settings.SPlayer;
import org.bukkit.inventory.ItemStack;

import java.util.function.Consumer;

@AllArgsConstructor
@Getter
public class CustomItem {
    private int slot;
    private ItemStack item;
    private Consumer<SPlayer> clickAction;
}

package me.limbo56.settings.menu;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/**
 * Created by limbo56(David)
 * On 6/9/2017
 * At 4:18 PM
 */
interface Togglable {

    void setEnabled(Inventory inventory);

    void setDisabled(Inventory inventory);

    boolean getCondition(Player player);

}

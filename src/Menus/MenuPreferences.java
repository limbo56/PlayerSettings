package Menus;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import Extra.AllStrings;
import PreferencesMain.MainPreferences;

/**
 * 
 * @author lim_bo56
 *
 */

public class MenuPreferences extends AllStrings {
    
	static MainPreferences plugin;
	
	 public static Inventory PreferencesMenu;
     public static void openPreferencesMenu(Player p) {
		PreferencesMenu = Bukkit.createInventory(null, 27, MenuPreferencesName);
		
		if(MainPreferences.getInstance().getConfig().getBoolean("MainMenu." + "Glass")) {
        for(int i = 0; i < 27; i++) {
        	ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getWoolData());
        	PreferencesMenu.setItem(i, MainPreferences.Glass(glass, "§7"));
        }
		}
		
		PreferencesMenu.setItem(11, MainPreferences.nolore(Material.getMaterial(PlayerPreferencesMenuItem), 1, 0, PlayerPreferencesMenuName));
		PreferencesMenu.setItem(15, MainPreferences.nolore(Material.getMaterial(LobbyPreferencesMenuItem), 1, 0, LobbyPreferencesMenuName));
        
        
		
		p.openInventory(PreferencesMenu);
	}
	
}

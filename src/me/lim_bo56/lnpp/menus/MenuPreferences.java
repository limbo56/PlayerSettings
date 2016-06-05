package me.lim_bo56.lnpp.menus;

import org.bukkit.Bukkit;
import org.bukkit.DyeColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import me.lim_bo56.lnpp.MainPreferences;
import me.lim_bo56.lnpp.utils.AllStrings;
import me.lim_bo56.lnpp.utils.ItemFactory;

/**
 * 
 * @author lim_bo56
 *
 */

public class MenuPreferences extends AllStrings {

	MainPreferences plugin;

	private static Inventory PreferencesMenu;

	/**
	 * Create the main menu.
	 * 
	 * @param p
	 */
	public static void openPreferencesMenu(Player p) {
		PreferencesMenu = Bukkit.createInventory(null, 27, getInstance().MenuPreferencesName);

		if (MainPreferences.getInstance().getConfig().getBoolean("MainMenu." + "Glass")) {
			for (int i = 0; i < 27; i++) {
				@SuppressWarnings("deprecation")
				ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.RED.getWoolData());
				PreferencesMenu.setItem(i, ItemFactory.getInstance().setGlass(glass, "§7"));
			}
		}
		// Player Preferences Item \\
		PreferencesMenu.setItem(11,
				ItemFactory.getInstance().setItemNoLore(Material.getMaterial(getInstance().PlayerPreferencesMenuItem),
						1, 0, getInstance().PlayerPreferencesMenuName));
						// Player Preferences Item \\

		// Lobby Preferences Item \\
		PreferencesMenu.setItem(15,
				ItemFactory.getInstance().setItemNoLore(Material.getMaterial(getInstance().LobbyPreferencesMenuItem), 1,
						0, getInstance().LobbyPreferencesMenuName));
		// Lobby Preferences Item \\

		p.openInventory(PreferencesMenu);
	}

}

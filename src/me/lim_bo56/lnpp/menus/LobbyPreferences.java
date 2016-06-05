package me.lim_bo56.lnpp.menus;

import java.util.ArrayList;

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

public class LobbyPreferences extends AllStrings {

	public static Inventory LobbyPreferences;

	/**
	 * Create the lobby preferences menu.
	 * 
	 * @param p
	 */
	public static void openLobbyPreferences(Player p) {
		LobbyPreferences = Bukkit.createInventory(null, 36, getInstance().LobbyPreferencesName);

		if (MainPreferences.getInstance().getConfig().getBoolean("LobbyPreferences." + "Glass") == true) {
			for (int i = 0; i < 36; i++) {
				@SuppressWarnings("deprecation")
				ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.YELLOW.getWoolData());
				LobbyPreferences.setItem(i, ItemFactory.getInstance().setGlass(glass, "§7"));
			}
		}
		// Stacker Item \\
		if (MainPreferences.getInstance().Stacker.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OffLore);
			LobbyPreferences.setItem(11, ItemFactory.getInstance().setItemNoLore(
					Material.getMaterial(getInstance().StackerItem), 1, 0, getInstance().StackerItemName));
			LobbyPreferences.setItem(20,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 10, getInstance().On, l));
			LobbyPreferences.setItem(11, ItemFactory.getInstance().glow.addGlow(LobbyPreferences.getItem(11)));
		} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OnLore);
			LobbyPreferences.setItem(11, ItemFactory.getInstance().setItemNoLore(
					Material.getMaterial(getInstance().StackerItem), 1, 0, getInstance().StackerItemName));
			LobbyPreferences.setItem(20,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 8, getInstance().Off, l));
		}
		// Stacker Item \\

		// Visibility Item \\
		if (MainPreferences.getInstance().Visibility.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OffLore);
			LobbyPreferences.setItem(13, ItemFactory.getInstance().setItemNoLore(
					Material.getMaterial(getInstance().VisibilityItem), 1, 0, getInstance().VisibilityItemName));
			LobbyPreferences.setItem(22,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 10, getInstance().On, l));
			LobbyPreferences.setItem(13, ItemFactory.getInstance().glow.addGlow(LobbyPreferences.getItem(13)));
		} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OnLore);
			LobbyPreferences.setItem(13, ItemFactory.getInstance().setItemNoLore(
					Material.getMaterial(getInstance().VisibilityItem), 1, 0, getInstance().VisibilityItemName));
			LobbyPreferences.setItem(22,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 8, getInstance().Off, l));
		}
		// Visibility Item \\

		// Chat Item \\
		if (MainPreferences.getInstance().Chat.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OffLore);
			LobbyPreferences.setItem(15, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().ChatItem), 1, 0, getInstance().ChatItemName));
			LobbyPreferences.setItem(24,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 10, getInstance().On, l));
			LobbyPreferences.setItem(15, ItemFactory.getInstance().glow.addGlow(LobbyPreferences.getItem(15)));
		} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OnLore);
			LobbyPreferences.setItem(15, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().ChatItem), 1, 0, getInstance().ChatItemName));
			LobbyPreferences.setItem(24,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 8, getInstance().Off, l));
		}
		// Chat Item \\

		// Back Item \\
		LobbyPreferences.setItem(27, ItemFactory.getInstance()
				.setItemNoLore(Material.getMaterial(getInstance().BackItem), 1, 0, getInstance().BackItemName));
		// Back Item \\
		p.openInventory(LobbyPreferences);
	}
}

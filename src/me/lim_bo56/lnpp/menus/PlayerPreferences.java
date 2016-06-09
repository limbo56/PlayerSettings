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

public class PlayerPreferences extends AllStrings {

	public static Inventory PlayerPreferences;

	/**
	 * Create the player preferences menu.
	 * 
	 * @param p
	 */
	public static void openPlayerPreferences(Player p) {
		PlayerPreferences = Bukkit.createInventory(null, 36, getInstance().PlayerPreferencesName);

		if (MainPreferences.getInstance().getConfig().getBoolean("PlayerPreferences." + "Glass")) {
			for (int i = 0; i < 36; i++) {
				@SuppressWarnings("deprecation")
				ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getWoolData());
				PlayerPreferences.setItem(i, ItemFactory.getInstance().setGlass(glass, "§7"));
			}
		}

		// Speed Item\\
		if (MainPreferences.getInstance().Speed.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OffLore);
			PlayerPreferences.setItem(10, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().SpeedItem), 1, 0, getInstance().SpeedItemName));
			PlayerPreferences.setItem(19,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 10, getInstance().On, l));
			PlayerPreferences.setItem(10, ItemFactory.getInstance().glow.addGlow(PlayerPreferences.getItem(10)));
		} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OnLore);
			PlayerPreferences.setItem(10, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().SpeedItem), 1, 0, getInstance().SpeedItemName));
			PlayerPreferences.setItem(19,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 8, getInstance().Off, l));
		}
		// Speed Item\\

		// Jump Item\\
		if (MainPreferences.getInstance().Jump.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OffLore);
			PlayerPreferences.setItem(12, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().JumpItem), 1, 0, getInstance().JumpItemName));
			PlayerPreferences.setItem(21,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 10, getInstance().On, l));
			PlayerPreferences.setItem(12, ItemFactory.getInstance().glow.addGlow(PlayerPreferences.getItem(12)));
		} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OnLore);
			PlayerPreferences.setItem(12, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().JumpItem), 1, 0, getInstance().JumpItemName));
			PlayerPreferences.setItem(21,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 8, getInstance().Off, l));
		}
		// Jump Item\\

		// Fly Item\\
		if (MainPreferences.getInstance().Fly.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OffLore);
			PlayerPreferences.setItem(14, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().FlyItem), 1, 0, getInstance().FlyItemName));
			PlayerPreferences.setItem(23,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 10, getInstance().On, l));
			PlayerPreferences.setItem(14, ItemFactory.getInstance().glow.addGlow(PlayerPreferences.getItem(14)));
		} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OnLore);
			PlayerPreferences.setItem(14, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().FlyItem), 1, 0, getInstance().FlyItemName));
			PlayerPreferences.setItem(23,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 8, getInstance().Off, l));
		}
		// Fly Item\\

		// Vanish Item\\
		if (MainPreferences.getInstance().Vanish.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OffLore);
			PlayerPreferences.setItem(16, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().VanishItem), 1, 0, getInstance().VanishItemName));
			PlayerPreferences.setItem(25,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 10, getInstance().On, l));
			PlayerPreferences.setItem(16, ItemFactory.getInstance().glow.addGlow(PlayerPreferences.getItem(16)));
		} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(getInstance().OnLore);
			PlayerPreferences.setItem(16, ItemFactory.getInstance()
					.setItemNoLore(Material.getMaterial(getInstance().VanishItem), 1, 0, getInstance().VanishItemName));
			PlayerPreferences.setItem(25,
					ItemFactory.getInstance().setItemWithLore(Material.INK_SACK, 1, 8, getInstance().Off, l));
		}
		// Vanish Item\\

		// Back Item \\
		PlayerPreferences.setItem(35, ItemFactory.getInstance()
				.setItemNoLore(Material.getMaterial(getInstance().BackItem), 1, 0, getInstance().BackItemName));
		// Back Item \\

		p.openInventory(PlayerPreferences);
	}
}

package Menus;

import java.util.ArrayList;

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

public class PlayerPreferences extends AllStrings {
   
	public static Inventory PlayerPreferences;
	
		public static void openPlayerPreferences(Player p) {
			PlayerPreferences = Bukkit.createInventory(null, 36, PlayerPreferencesName);
			
			if(MainPreferences.getInstance().getConfig().getBoolean("PlayerPreferences." + "Glass")) {
				for(int i = 0; i < 36; i++) {
					ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.LIGHT_BLUE.getWoolData());
					PlayerPreferences.setItem(i, MainPreferences.Glass(glass, "§7"));
				}
 			}
			
			//Speed Item\\
			if(MainPreferences.Speed.contains(p)) {
				ArrayList<String> l = new ArrayList<String>();
				l.add("");
				l.add(OffLore);
				PlayerPreferences.setItem(10, MainPreferences.nolore(Material.getMaterial(SpeedItem), 1, 0, SpeedItemName));
				PlayerPreferences.setItem(19, MainPreferences.siLore(Material.INK_SACK, 1, 10, On, l));
				PlayerPreferences.setItem(10, MainPreferences.glow.addGlow(PlayerPreferences.getItem(10)));
			} else {
				ArrayList<String> l = new ArrayList<String>();
				l.add("");
				l.add(OnLore);
				PlayerPreferences.setItem(10, MainPreferences.nolore(Material.getMaterial(SpeedItem), 1, 0, SpeedItemName));
				PlayerPreferences.setItem(19, MainPreferences.siLore(Material.INK_SACK, 1, 8, Off, l));
			}
			//Speed Item\\
			
			//Jump Item\\
			if(MainPreferences.Jump.contains(p)) {
				ArrayList<String> l = new ArrayList<String>();
				l.add("");
				l.add(OffLore);
				PlayerPreferences.setItem(12, MainPreferences.nolore(Material.getMaterial(JumpItem), 1, 0, JumpItemName));
				PlayerPreferences.setItem(21, MainPreferences.siLore(Material.INK_SACK, 1, 10, On, l));
				PlayerPreferences.setItem(12, MainPreferences.glow.addGlow(PlayerPreferences.getItem(12)));
			} else {
				ArrayList<String> l = new ArrayList<String>();
				l.add("");
				l.add(OffLore);
				PlayerPreferences.setItem(12, MainPreferences.nolore(Material.getMaterial(JumpItem), 1, 0, JumpItemName));
				PlayerPreferences.setItem(21, MainPreferences.siLore(Material.INK_SACK, 1, 8, Off, l));
			}
			//Jump Item\\		
			
			//Fly Item\\ 
			if(MainPreferences.Fly.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(OffLore);
			PlayerPreferences.setItem(14, MainPreferences.nolore(Material.getMaterial(FlyItem), 1, 0, FlyItemName));
			PlayerPreferences.setItem(23, MainPreferences.siLore(Material.INK_SACK, 1, 10, On, l));
			PlayerPreferences.setItem(14, MainPreferences.glow.addGlow(PlayerPreferences.getItem(14)));
			} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(OnLore);
			PlayerPreferences.setItem(14, MainPreferences.nolore(Material.getMaterial(FlyItem), 1, 0, FlyItemName));
			PlayerPreferences.setItem(23, MainPreferences.siLore(Material.INK_SACK, 1, 8, Off, l));
			}
			//Fly Item\\
			
			//Vanish Item\\
			if(MainPreferences.Vanish.contains(p)) {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(OffLore);
			PlayerPreferences.setItem(16, MainPreferences.nolore(Material.getMaterial(VanishItem), 1, 0, VanishItemName));
			PlayerPreferences.setItem(25, MainPreferences.siLore(Material.INK_SACK, 1, 10, On, l));
			PlayerPreferences.setItem(16, MainPreferences.glow.addGlow(PlayerPreferences.getItem(16)));
			} else {
			ArrayList<String> l = new ArrayList<String>();
			l.add("");
			l.add(OnLore);
			PlayerPreferences.setItem(16, MainPreferences.nolore(Material.getMaterial(VanishItem), 1, 0, VanishItemName));
			PlayerPreferences.setItem(25, MainPreferences.siLore(Material.INK_SACK, 1, 8, Off, l));
			}
			//Vanish Item\\
			
			PlayerPreferences.setItem(35, MainPreferences.nolore(Material.getMaterial(BackItem), 1, 0, BackItemName));
						
			p.openInventory(PlayerPreferences);
		}
}

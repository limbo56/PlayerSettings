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

public class LobbyPreferences extends AllStrings {

	 public static Inventory LobbyPreferences;
     public static void openLobbyPreferences(Player p) {
    	 LobbyPreferences = Bukkit.createInventory(null, 36, LobbyPreferencesName);
    	 
    	 if(MainPreferences.getInstance().getConfig().getBoolean("LobbyPreferences." + "Glass") == true) {
    	 for(int i = 0; i < 36; i++) {
    		 @SuppressWarnings("deprecation")
			ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, DyeColor.YELLOW.getWoolData());
    		 LobbyPreferences.setItem(i, MainPreferences.Glass(glass, "§7"));
    	 }
    	 }
    	 
    	 if(MainPreferences.Stacker.contains(p)) {
    		 ArrayList<String> l = new ArrayList<String>();
    		 l.add("");
    		 l.add(OffLore);
    		 LobbyPreferences.setItem(11, MainPreferences.nolore(Material.getMaterial(StackerItem), 1, 0, StackerItemName));
    		 LobbyPreferences.setItem(20, MainPreferences.siLore(Material.INK_SACK, 1, 10, On, l));
    		 LobbyPreferences.setItem(11, MainPreferences.glow.addGlow(LobbyPreferences.getItem(11)));
    	 } else {
    		 ArrayList<String> l = new ArrayList<String>();
    		 l.add("");
    		 l.add(OnLore);
    		 LobbyPreferences.setItem(11, MainPreferences.nolore(Material.getMaterial(StackerItem), 1, 0, StackerItemName));
    		 LobbyPreferences.setItem(20, MainPreferences.siLore(Material.INK_SACK, 1, 8, Off, l));
    	 }
    	 
    	 if(MainPreferences.Visibility.contains(p)) {
    		 ArrayList<String> l = new ArrayList<String>();
    		 l.add("");
    		 l.add(OffLore);
    		 LobbyPreferences.setItem(13, MainPreferences.nolore(Material.getMaterial(VisibilityItem), 1, 0, VisibilityItemName));
    		 LobbyPreferences.setItem(22, MainPreferences.siLore(Material.INK_SACK, 1, 10, On, l));
    		 LobbyPreferences.setItem(13, MainPreferences.glow.addGlow(LobbyPreferences.getItem(13)));
    	 } else {
    		 ArrayList<String> l = new ArrayList<String>();
    		 l.add("");
    		 l.add(OnLore);
    		 LobbyPreferences.setItem(13, MainPreferences.nolore(Material.getMaterial(VisibilityItem), 1, 0, VisibilityItemName));
    		 LobbyPreferences.setItem(22, MainPreferences.siLore(Material.INK_SACK, 1, 8, Off, l));
    	 }
    	 
    	 if(MainPreferences.Chat.contains(p)) {
    		 ArrayList<String> l = new ArrayList<String>();
    		 l.add("");
    		 l.add(OffLore);
    		 LobbyPreferences.setItem(15, MainPreferences.nolore(Material.getMaterial(ChatItem), 1, 0, ChatItemName));
    		 LobbyPreferences.setItem(24, MainPreferences.siLore(Material.INK_SACK, 1, 10, On, l));
    		 LobbyPreferences.setItem(15, MainPreferences.glow.addGlow(LobbyPreferences.getItem(15)));
    	 } else {
    		 ArrayList<String> l = new ArrayList<String>();
    		 l.add("");
    		 l.add(OnLore);
    		 LobbyPreferences.setItem(15, MainPreferences.nolore(Material.getMaterial(ChatItem), 1, 0, ChatItemName));
    		 LobbyPreferences.setItem(24, MainPreferences.siLore(Material.INK_SACK, 1, 8, Off, l));
    	 }
    	 
    	 LobbyPreferences.setItem(27, MainPreferences.nolore(Material.getMaterial(BackItem), 1, 0, BackItemName));
    	 
    	 p.openInventory(LobbyPreferences);
	}
}
